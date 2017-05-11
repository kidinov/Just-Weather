package org.kidinov.just_weather.weather.model;

import org.kidinov.just_weather.weather.model.local.City;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Maybe;
import io.reactivex.Single;

public interface WeatherDataSource {
    Single<List<City>> updateWeatherInfo();

    Maybe<List<City>> getAddedCitiesWeather();

    Single<City> saveOrUpdateCity(City city);

    Single<City> addCity(String cityName);

    Single<City> addCity(double lat, double lon);

    Completable removeCityById(int id);
}
