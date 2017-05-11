package org.kidinov.just_weather.weather.presentation;

import org.kidinov.just_weather.util.RxUtil;
import org.kidinov.just_weather.weather.WeatherContract;
import org.kidinov.just_weather.weather.model.WeatherRepository;
import org.kidinov.just_weather.weather.model.local.City;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Completable;
import io.reactivex.Observable;
import io.reactivex.Single;
import io.reactivex.disposables.CompositeDisposable;
import timber.log.Timber;

public class WeatherPresenter implements WeatherContract.Presenter {
    private CompositeDisposable subscriptions = new CompositeDisposable();
    private WeatherRepository repository;
    private WeatherContract.View view;

    @Inject
    public WeatherPresenter(WeatherRepository repository, WeatherContract.View view) {
        this.repository = repository;
        this.view = view;
    }

    @Override
    public void updateData(boolean refresh) {
        if (!refresh) {
            view.showProgress();
        }
        subscriptions.add(repository.updateWeatherInfo()
                .subscribe(
                        x -> {
                            Timber.d("weather updated");
                            getDataFromDb(false);
                        },
                        e -> {
                            Timber.e(e);
                            getDataFromDb(true);
                            view.showNetworkErrorNotification();
                        }));
    }

    @Override
    public void addCityByCoordinates(double latitude, double longitude) {
        subscriptions.add(repository.addCity(latitude, longitude)
                .subscribe(
                        weatherInCity -> {
                            Timber.d("city added - %s", weatherInCity);
                            getDataFromDb(false);
                        }, e -> {
                            Timber.e(e);
                            getDataFromDb(true);
                        }));
    }

    @Override
    public void addCityByName(String name) {
        view.showProgress();
        subscriptions.add(repository.addCity(name)
                .subscribe(
                        weatherInCity -> {
                            Timber.d("city added - %s", weatherInCity);
                            getDataFromDb(false);
                        }, e -> {
                            Timber.e(e);
                            getDataFromDb(true);
                            view.showNetworkErrorNotification();
                        }));
    }

    @Override
    public void itemRemovedAtPosition(int swipedPosition) {
        subscriptions.add(getSortedListOfCities()
                .flatMapCompletable(cities -> {
                    if (!cities.isEmpty()) {
                        City cityToDelete = cities.get(swipedPosition);
                        if (cityToDelete.isCurrentLocationCity()) {
                            view.showCurrentCityDeletionErrorNotification();
                            return Completable.complete();
                        }
                        return repository.removeCityById(cityToDelete.getId());
                    }
                    return Completable.complete();
                })
                .subscribe(
                        () -> {
                            Timber.d("city removed");
                            getDataFromDb(false);
                        }, e -> {
                            Timber.e(e);
                            getDataFromDb(true);
                            view.showNetworkErrorNotification();
                        }));
    }

    private void getDataFromDb(boolean error) {
        subscriptions.add(getSortedListOfCities()
                .subscribe(cities -> {
                    if (cities.isEmpty()) {
                        if (error) {
                            view.showError();
                        } else {
                            view.showEmptyState();
                        }
                    } else {
                        view.showData(cities);
                    }
                }, Timber::e));
    }

    private Single<List<City>> getSortedListOfCities() {
        return repository.getAddedCitiesWeather()
                .flatMapObservable(Observable::fromIterable)
                .toSortedList((w1, w2) -> {
                    if (w1.isCurrentLocationCity()) {
                        return -1;
                    }
                    if (w2.isCurrentLocationCity()) {
                        return 1;
                    }
                    return w1.getName().compareTo(w2.getName());
                });
    }

    @Override
    public void subscribe() {
    }

    @Override
    public void unsubscribe() {
        RxUtil.unsubscribe(subscriptions);
    }
}
