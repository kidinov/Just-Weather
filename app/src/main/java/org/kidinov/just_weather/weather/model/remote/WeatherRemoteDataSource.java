package org.kidinov.just_weather.weather.model.remote;


import android.support.annotation.NonNull;

import org.kidinov.just_weather.common.data.remote.ServerInterface;
import org.kidinov.just_weather.weather.model.WeatherDataSource;
import org.kidinov.just_weather.weather.model.local.City;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Maybe;
import io.reactivex.Single;

public class WeatherRemoteDataSource implements WeatherDataSource {
    @NonNull
    private final ServerInterface serverInterface;

    public WeatherRemoteDataSource(@NonNull ServerInterface serverInterface) {
        this.serverInterface = serverInterface;
    }

    @Override
    public Single<City> saveOrUpdateCity(City city) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Single<City> addCity(String cityName) {
        return serverInterface.getWeather(cityName);
    }

    @Override
    public Single<City> addCity(double lat, double lon) {
        return serverInterface.getWeather(lat, lon);
    }

    @Override
    public Single<List<City>> updateWeatherInfo() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Maybe<List<City>> getAddedCitiesWeather() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Completable removeCityById(int id) {
        throw new UnsupportedOperationException();
    }
}
