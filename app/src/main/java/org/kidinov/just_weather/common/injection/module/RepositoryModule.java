package org.kidinov.just_weather.common.injection.module;

import org.kidinov.just_weather.common.injection.annotation.Local;
import org.kidinov.just_weather.common.injection.annotation.Remote;
import org.kidinov.just_weather.weather.model.WeatherDataSource;
import org.kidinov.just_weather.weather.model.WeatherRepository;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class RepositoryModule {

    @Singleton
    @Provides
    WeatherRepository provideWeatherRepository(@Local WeatherDataSource localDataSource,
                                               @Remote WeatherDataSource remoteDataSource) {
        return new WeatherRepository(localDataSource, remoteDataSource);
    }
}
