package org.kidinov.just_weather.weather.model.remote;

import org.junit.Before;
import org.junit.Test;
import org.kidinov.just_weather.WeatherTestUtil;
import org.kidinov.just_weather.common.data.remote.ServerInterface;
import org.kidinov.just_weather.util.TestUtil;
import org.kidinov.just_weather.weather.model.local.City;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import io.reactivex.Single;
import io.reactivex.observers.TestObserver;

import static org.mockito.Mockito.when;

public class WeatherRemoteDataSourceTest {
    private WeatherRemoteDataSource dataSource;

    @Mock
    ServerInterface serverInterface;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);

        dataSource = new WeatherRemoteDataSource(serverInterface);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void saveOrUpdateCityShouldThrowException() {
        dataSource.saveOrUpdateCity(WeatherTestUtil.generateCity()).blockingGet();
    }

    @Test
    public void addCityByNameShouldReturnCityReturnedByServer() {
        City city = WeatherTestUtil.generateCity();
        when(serverInterface.getWeather(city.getName())).thenReturn(Single.just(city));
        TestObserver<City> testObserver = dataSource.addCity(city.getName()).test();
        testObserver.assertNoErrors();
        testObserver.assertValue(city);
    }

    @Test
    public void addCityByCoordinatesShouldReturnCityReturnedByServer() {
        City city = WeatherTestUtil.generateCity();
        double lat = TestUtil.generateDouble();
        double lon = TestUtil.generateDouble();
        when(serverInterface.getWeather(lat, lon)).thenReturn(Single.just(city));
        TestObserver<City> testObserver = dataSource.addCity(lat, lon).test();
        testObserver.assertNoErrors();
        testObserver.assertValue(city);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void updateWeatherInfoShouldThrowException() {
        dataSource.updateWeatherInfo().blockingGet();
    }

    @Test(expected = UnsupportedOperationException.class)
    public void getAddedCitiesWeatherShouldThrowException() {
        dataSource.getAddedCitiesWeather().blockingGet();
    }

}