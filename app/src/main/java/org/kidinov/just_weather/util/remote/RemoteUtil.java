package org.kidinov.just_weather.util.remote;

public class RemoteUtil {
    public static String formatWeatherIconLink(String iconNum) {
        return String.format("http://openweathermap.org/img/w/%s.png", iconNum);
    }
}
