package org.kidinov.w_app.weather.presentation;


import org.kidinov.w_app.common.injection.annotation.ActivityScope;
import org.kidinov.w_app.weather.WeatherContract;
import org.kidinov.w_app.weather.view.WeatherActivity;

import dagger.Module;
import dagger.Provides;

@ActivityScope
@Module
public class WeatherPresenterModule {
    private final WeatherActivity view;

    public WeatherPresenterModule(WeatherActivity view) {
        this.view = view;
    }

    @Provides
    WeatherContract.View provideView() {
        return view;
    }
}
