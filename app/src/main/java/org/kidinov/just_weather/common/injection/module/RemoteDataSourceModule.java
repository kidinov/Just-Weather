package org.kidinov.just_weather.common.injection.module;


import android.support.annotation.NonNull;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

import org.kidinov.just_weather.BuildConfig;
import org.kidinov.just_weather.common.data.remote.ServerInterface;
import org.kidinov.just_weather.common.injection.annotation.Remote;
import org.kidinov.just_weather.util.remote.ApiKeyRequestInterceptor;
import org.kidinov.just_weather.weather.model.WeatherDataSource;
import org.kidinov.just_weather.weather.model.remote.WeatherRemoteDataSource;

import java.util.concurrent.TimeUnit;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

@Module
public class RemoteDataSourceModule {
    private static final String BASE_URL = "http://api.openweathermap.org/data/2.5/";
    private static final String API_KEY = "e746ffbb6021d1959acd9a5bc1986685";
    private static final String UNITS = "metric";

    @Singleton
    @Provides
    @Remote
    WeatherDataSource provideRemoteWeatherDataSource(ServerInterface serverInterface) {
        return new WeatherRemoteDataSource(serverInterface);
    }

    @Provides
    @Singleton
    OkHttpClient provideOkHttpClient() {
        return getOkHttpClient(getApiKeyRequestInterceptor());
    }

    @Provides
    @Singleton
    ServerInterface provideServerInterface(Gson gson, OkHttpClient okHttpClient) {
        return getServerInterface(getRetrofit(gson, okHttpClient));
    }

    @Provides
    @Singleton
    Gson provideGson() {
        return getGson();
    }

    @Provides
    @Singleton
    ApiKeyRequestInterceptor provideApiKeyRequestInterceptor() {
        return getApiKeyRequestInterceptor();
    }

    @NonNull
    public static Gson getGson() {
        return new GsonBuilder()
                .create();
    }

    @NonNull
    public static Retrofit getRetrofit(Gson gson, OkHttpClient okHttpClient) {
        return new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .client(okHttpClient)
                .build();
    }

    @NonNull
    public static OkHttpClient getOkHttpClient(ApiKeyRequestInterceptor apiKeyRequestInterceptor) {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();

        builder.addInterceptor(apiKeyRequestInterceptor);

        if (BuildConfig.DEBUG) {
            HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
            interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
            builder.addInterceptor(interceptor);
        }

        builder.connectTimeout(5, TimeUnit.SECONDS);
        builder.readTimeout(5, TimeUnit.SECONDS);

        return builder.build();
    }

    public static ServerInterface getServerInterface(Retrofit retrofit) {
        return retrofit.create(ServerInterface.class);
    }

    public static ApiKeyRequestInterceptor getApiKeyRequestInterceptor() {
        return new ApiKeyRequestInterceptor(API_KEY, UNITS);
    }

}
