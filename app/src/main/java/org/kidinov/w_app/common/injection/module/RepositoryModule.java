package org.kidinov.w_app.common.injection.module;

import org.kidinov.w_app.common.injection.annotation.Local;
import org.kidinov.w_app.common.injection.annotation.Remote;
import org.kidinov.w_app.weather.model.WeatherDataSource;
import org.kidinov.w_app.weather.model.WeatherRepository;

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
