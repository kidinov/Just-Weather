package org.kidinov.just_weather.common.data.remote;

import org.kidinov.just_weather.weather.model.local.City;

import io.reactivex.Single;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ServerInterface {
    @GET("weather")
    Single<City> getWeather(@Query("q") String cityName);

    @GET("weather")
    Single<City> getWeather(@Query("lat") double lat, @Query("lon") double lon);
}
