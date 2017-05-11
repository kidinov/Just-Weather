package org.kidinov.just_weather.weather.model.local;


import com.google.gson.annotations.SerializedName;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class City extends RealmObject {
    @SerializedName("id")
    @PrimaryKey
    private Integer id;
    @SerializedName("weather")
    private RealmList<Weather> weather = new RealmList<>();
    @SerializedName("main")
    private Main main;
    @SerializedName("dt")
    private Integer dt;
    @SerializedName("sys")
    private Sys sys;
    @SerializedName("name")
    private String name;

    private boolean currentLocationCity;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public RealmList<Weather> getWeather() {
        return weather;
    }

    public void setWeather(RealmList<Weather> weather) {
        this.weather = weather;
    }

    public Main getMain() {
        return main;
    }

    public void setMain(Main main) {
        this.main = main;
    }

    public Integer getDt() {
        return dt;
    }

    public void setDt(Integer dt) {
        this.dt = dt;
    }

    public Sys getSys() {
        return sys;
    }

    public void setSys(Sys sys) {
        this.sys = sys;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isCurrentLocationCity() {
        return currentLocationCity;
    }

    public void setCurrentLocationCity(boolean currentLocationCity) {
        this.currentLocationCity = currentLocationCity;
    }

    @Override
    public String toString() {
        return "WeatherInCity{" +
                "id=" + id +
                ", weather=" + weather +
                ", main=" + main +
                ", dt=" + dt +
                ", sys=" + sys +
                ", name='" + name + '\'' +
                ", currentLocationCity=" + currentLocationCity +
                '}';
    }
}
