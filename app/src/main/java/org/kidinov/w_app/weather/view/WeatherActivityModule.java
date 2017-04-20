package org.kidinov.w_app.weather.view;

import com.patloew.rxlocation.RxLocation;
import com.tbruyelle.rxpermissions2.RxPermissions;

import dagger.Module;
import dagger.Provides;

@Module
public class WeatherActivityModule {
    private final WeatherActivity activity;

    public WeatherActivityModule(WeatherActivity activity) {
        this.activity = activity;
    }

    @Provides
    RxPermissions provideRxPermissions() {
        return new RxPermissions(activity);
    }

    @Provides
    RxLocation provideRxLocation() {
        return new RxLocation(activity);
    }
}
