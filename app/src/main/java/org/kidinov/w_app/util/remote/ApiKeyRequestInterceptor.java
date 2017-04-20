package org.kidinov.w_app.util.remote;

import java.io.IOException;

import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public class ApiKeyRequestInterceptor implements Interceptor {
    private String apiKey;
    private String units;

    public ApiKeyRequestInterceptor(String apiKey, String units) {
        this.apiKey = apiKey;
        this.units = units;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request original = chain.request();
        HttpUrl originalHttpUrl = original.url();

        HttpUrl url = originalHttpUrl.newBuilder()
                .addQueryParameter("APPID", apiKey)
                .addQueryParameter("units", units)
                .build();

        // Request customization: add request headers
        Request.Builder requestBuilder = original.newBuilder()
                .url(url);

        Request request = requestBuilder.build();
        return chain.proceed(request);
    }
}
