package org.kidinov.just_weather.weather.presentation;

import org.junit.Before;
import org.junit.Test;
import org.kidinov.just_weather.WeatherTestUtil;
import org.kidinov.just_weather.util.TestUtil;
import org.kidinov.just_weather.weather.WeatherContract;
import org.kidinov.just_weather.weather.model.WeatherRepository;
import org.kidinov.just_weather.weather.model.local.City;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;

import io.reactivex.Maybe;
import io.reactivex.Single;

import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.internal.verification.VerificationModeFactory.times;

public class WeatherPresenterTest {
    private WeatherPresenter presenter;

    @Mock
    WeatherRepository repository;

    @Mock
    WeatherContract.View view;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);

        presenter = new WeatherPresenter(repository, view);
    }

    @Test
    public void updateDataShouldShowProgressIfNotRefresh() {
        City city = WeatherTestUtil.generateCity();
        List<City> cities = Arrays.asList(city);
        when(repository.updateWeatherInfo()).thenReturn(Single.just(cities));
        when(repository.getAddedCitiesWeather()).thenReturn(Maybe.just(cities));
        presenter.updateData(false);
        verify(view, times(1)).showProgress();
    }

    @Test
    public void updateDataShouldShowDataFromRepository() {
        City city = WeatherTestUtil.generateCity();
        List<City> cities = Arrays.asList(city);
        when(repository.updateWeatherInfo()).thenReturn(Single.just(cities));
        when(repository.getAddedCitiesWeather()).thenReturn(Maybe.just(cities));
        presenter.updateData(true);
        verify(view, never()).showProgress();
        verify(view, times(1)).showData(cities);
    }

    @Test
    public void addCityByCoordinatesShouldShowThatCity() {
        City city = WeatherTestUtil.generateCity();
        List<City> cities = Arrays.asList(city);
        double lan = TestUtil.generateDouble();
        double lon = TestUtil.generateDouble();
        when(repository.addCity(lan, lon)).thenReturn(Single.just(city));
        when(repository.getAddedCitiesWeather()).thenReturn(Maybe.just(cities));
        presenter.addCityByCoordinates(lan, lon);
        verify(view, times(1)).showData(cities);
    }

    @Test
    public void addCityByCoordinatesShouldShowErrorIfNoLocalDataAndError() {
        City city = WeatherTestUtil.generateCity();
        List<City> cities = Arrays.asList(city);
        double lan = TestUtil.generateDouble();
        double lon = TestUtil.generateDouble();
        when(repository.addCity(lan, lon)).thenReturn(Single.error(new Exception()));
        when(repository.getAddedCitiesWeather()).thenReturn(Maybe.empty());
        presenter.addCityByCoordinates(lan, lon);
        verify(view, times(1)).showError();
        verify(view, never()).showData(cities);
    }

    @Test
    public void addCityByNameShouldShowThatCity() {
        City city = WeatherTestUtil.generateCity();
        List<City> cities = Arrays.asList(city);
        when(repository.addCity(city.getName())).thenReturn(Single.just(city));
        when(repository.getAddedCitiesWeather()).thenReturn(Maybe.just(cities));
        presenter.addCityByName(city.getName());
        verify(view, times(1)).showData(cities);
    }

    @Test
    public void addCityByNameShouldShowErrorIfNoLocalDataAndError() {
        City city = WeatherTestUtil.generateCity();
        List<City> cities = Arrays.asList(city);
        when(repository.addCity(city.getName())).thenReturn(Single.error(new Exception()));
        when(repository.getAddedCitiesWeather()).thenReturn(Maybe.empty());
        presenter.addCityByName(city.getName());
        verify(view, never()).showData(cities);
    }

}