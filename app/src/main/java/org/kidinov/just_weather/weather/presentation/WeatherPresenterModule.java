package org.kidinov.just_weather.weather.presentation;


import org.kidinov.just_weather.common.injection.annotation.ActivityScope;
import org.kidinov.just_weather.weather.WeatherContract;
import org.kidinov.just_weather.weather.view.WeatherActivity;

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
