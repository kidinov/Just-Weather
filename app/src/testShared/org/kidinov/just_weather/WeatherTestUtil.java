package org.kidinov.just_weather;


import org.kidinov.just_weather.util.TestUtil;
import org.kidinov.just_weather.weather.model.local.City;
import org.kidinov.just_weather.weather.model.local.Main;
import org.kidinov.just_weather.weather.model.local.Sys;
import org.kidinov.just_weather.weather.model.local.Weather;

import io.realm.RealmList;

public class WeatherTestUtil {
    public static City generateCity() {
        City city = new City();
        city.setId(TestUtil.generateId());
        city.setCurrentLocationCity(TestUtil.generateBoolean());
        city.setDt(TestUtil.generateSmallInt());
        city.setName(TestUtil.generateString(5));

        Sys sys = new Sys();
        sys.setId(TestUtil.generateId());
        sys.setCountry(TestUtil.generateString(2));
        city.setSys(sys);

        Main main = new Main();
        main.setTemp(TestUtil.generateDouble());
        city.setMain(main);

        RealmList<Weather> listOfWeather = new RealmList<>();
        Weather weather = new Weather();
        weather.setId(TestUtil.generateId());
        weather.setIcon(TestUtil.generateString(3));
        city.setWeather(listOfWeather);
        return city;
    }
}
