package org.kidinov.w_app.common.injection.component;


import org.kidinov.w_app.App;
import org.kidinov.w_app.common.injection.module.ApplicationModule;
import org.kidinov.w_app.common.injection.module.LocalDataSourceModule;
import org.kidinov.w_app.common.injection.module.RemoteDataSourceModule;
import org.kidinov.w_app.common.injection.module.RepositoryModule;
import org.kidinov.w_app.weather.model.WeatherRepository;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = {ApplicationModule.class, RepositoryModule.class, LocalDataSourceModule.class,
        RemoteDataSourceModule.class})
public interface RepositoryComponent {
    WeatherRepository getWeatherRepository();

    void inject(App o);
}
