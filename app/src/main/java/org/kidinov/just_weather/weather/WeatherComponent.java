package org.kidinov.just_weather.weather;


import org.kidinov.just_weather.common.injection.annotation.ActivityScope;
import org.kidinov.just_weather.common.injection.component.RepositoryComponent;
import org.kidinov.just_weather.weather.presentation.WeatherPresenterModule;
import org.kidinov.just_weather.weather.view.WeatherActivity;
import org.kidinov.just_weather.weather.view.WeatherActivityModule;

import dagger.Component;

@ActivityScope
@Component(dependencies = RepositoryComponent.class,
        modules = {
                WeatherPresenterModule.class,
                WeatherActivityModule.class
        })
public interface WeatherComponent {
    void inject(WeatherActivity o);
}
