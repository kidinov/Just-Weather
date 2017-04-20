package org.kidinov.w_app.util.remote;

public class RemoteUtil {
    public static String formatWeatherIconLink(String iconNum) {
        return String.format("http://openweathermap.org/img/w/%s.png", iconNum);
    }
}
