package org.kidinov.w_app.weather.presentation;

import org.kidinov.w_app.util.RxUtil;
import org.kidinov.w_app.weather.WeatherContract;
import org.kidinov.w_app.weather.model.WeatherRepository;

import javax.inject.Inject;

import io.reactivex.Observable;
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
                            view.showErrorNotification();
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
                            view.showErrorNotification();
                        }));
    }

    private void getDataFromDb(boolean error) {
        subscriptions.add(repository.getAddedCitiesWeather()
                .flatMapObservable(Observable::fromIterable)
                .toSortedList((w1, w2) -> {
                    if (w1.isCurrentLocationCity()) {
                        return -1;
                    }
                    if (w2.isCurrentLocationCity()) {
                        return 1;
                    }
                    return w1.getName().compareTo(w2.getName());
                })
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

    @Override
    public void subscribe() {
    }

    @Override
    public void unsubscribe() {
        RxUtil.unsubscribe(subscriptions);
    }
}
