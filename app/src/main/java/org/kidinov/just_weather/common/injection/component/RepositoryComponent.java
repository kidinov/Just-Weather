package org.kidinov.just_weather.common.injection.component;


import org.kidinov.just_weather.App;
import org.kidinov.just_weather.common.injection.module.ApplicationModule;
import org.kidinov.just_weather.common.injection.module.LocalDataSourceModule;
import org.kidinov.just_weather.common.injection.module.RemoteDataSourceModule;
import org.kidinov.just_weather.common.injection.module.RepositoryModule;
import org.kidinov.just_weather.weather.model.WeatherRepository;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = {ApplicationModule.class, RepositoryModule.class, LocalDataSourceModule.class,
        RemoteDataSourceModule.class})
public interface RepositoryComponent {
    WeatherRepository getWeatherRepository();

    void inject(App o);
}
