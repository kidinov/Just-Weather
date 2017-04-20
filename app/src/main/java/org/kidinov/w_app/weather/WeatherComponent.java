package org.kidinov.w_app.weather;


import org.kidinov.w_app.common.injection.annotation.ActivityScope;
import org.kidinov.w_app.common.injection.component.RepositoryComponent;
import org.kidinov.w_app.weather.presentation.WeatherPresenterModule;
import org.kidinov.w_app.weather.view.WeatherActivity;
import org.kidinov.w_app.weather.view.WeatherActivityModule;

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
