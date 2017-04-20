package org.kidinov.w_app.common.data.remote;

import org.kidinov.w_app.weather.model.local.City;

import io.reactivex.Single;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ServerInterface {
    @GET("weather")
    Single<City> getWeather(@Query("q") String cityName);

    @GET("weather")
    Single<City> getWeather(@Query("lat") double lat, @Query("lon") double lon);
}
