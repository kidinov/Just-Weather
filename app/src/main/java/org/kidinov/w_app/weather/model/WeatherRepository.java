package org.kidinov.w_app.weather.model;

import android.support.annotation.NonNull;

import org.kidinov.w_app.common.injection.annotation.Local;
import org.kidinov.w_app.common.injection.annotation.Remote;
import org.kidinov.w_app.util.RxUtil;
import org.kidinov.w_app.weather.model.local.City;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Maybe;
import io.reactivex.Observable;
import io.reactivex.Single;

public class WeatherRepository implements WeatherDataSource {
    @NonNull
    private final WeatherDataSource localDataSource;
    @NonNull
    private final WeatherDataSource remoteDataSource;

    @Inject
    public WeatherRepository(@NonNull @Local WeatherDataSource localDataSource,
                             @NonNull @Remote WeatherDataSource remoteDataSource) {
        this.localDataSource = localDataSource;
        this.remoteDataSource = remoteDataSource;
    }

    @Override
    public Single<City> saveOrUpdateCity(City city) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Single<City> addCity(String cityName) {
        return remoteDataSource.addCity(cityName)
                .compose(RxUtil.applySingleSchedulers())
                .flatMap(localDataSource::saveOrUpdateCity);
    }

    @Override
    public Single<City> addCity(double lat, double lon) {
        return remoteDataSource.addCity(lat, lon)
                .compose(RxUtil.applySingleSchedulers())
                .flatMap(city -> {
                    city.setCurrentLocationCity(true);
                    return localDataSource.saveOrUpdateCity(city);
                });
    }

    @Override
    public Single<List<City>> updateWeatherInfo() {
        return localDataSource.getAddedCitiesWeather()
                .flatMapObservable(Observable::fromIterable)
                //Current location city will be updated when location will be determined
                .filter(city -> !city.isCurrentLocationCity())
                .flatMap(city -> addCity(city.getName())
                        .toObservable())
                .toList();
    }

    @Override
    public Maybe<List<City>> getAddedCitiesWeather() {
        return localDataSource.getAddedCitiesWeather();
    }
}
