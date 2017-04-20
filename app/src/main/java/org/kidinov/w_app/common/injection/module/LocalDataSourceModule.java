package org.kidinov.w_app.common.injection.module;


import android.content.Context;

import org.kidinov.w_app.common.data.local.RmMigration;
import org.kidinov.w_app.common.injection.annotation.Local;
import org.kidinov.w_app.weather.model.WeatherDataSource;
import org.kidinov.w_app.weather.model.local.WeatherLocalDataSource;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import io.realm.Realm;
import io.realm.RealmConfiguration;
import timber.log.Timber;

@Module
public class LocalDataSourceModule {
    private static final int DATABASE_VERSION = 1;

    @Singleton
    @Provides
    @Local
    WeatherDataSource provideWeatherDataSource(Realm realm) {
        return new WeatherLocalDataSource(realm);
    }

    @Provides
    @Singleton
    Realm provideRealm(RealmConfiguration config) {
        Realm.setDefaultConfiguration(config);
        try {
            return Realm.getDefaultInstance();
        } catch (Exception e) {
            Timber.e(e, "");
            Realm.deleteRealm(config);
            Realm.setDefaultConfiguration(config);
            return Realm.getDefaultInstance();
        }
    }

    @Provides
    @Singleton
    RealmConfiguration provideRealmConfig(Context context) {
        Realm.init(context);

        return new RealmConfiguration.Builder()
                .schemaVersion(DATABASE_VERSION)
                .migration(new RmMigration())
                .build();
    }
}
