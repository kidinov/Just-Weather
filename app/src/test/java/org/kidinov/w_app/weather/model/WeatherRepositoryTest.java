package org.kidinov.w_app.weather.model;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.kidinov.w_app.WeatherTestUtil;
import org.kidinov.w_app.util.RxTestUtil;
import org.kidinov.w_app.util.RxUtil;
import org.kidinov.w_app.util.TestUtil;
import org.kidinov.w_app.weather.model.local.City;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.util.Arrays;
import java.util.List;

import io.reactivex.Maybe;
import io.reactivex.Single;
import io.reactivex.observers.TestObserver;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


@RunWith(PowerMockRunner.class)
@PrepareForTest(RxUtil.class)
public class WeatherRepositoryTest {
    private WeatherRepository repository;

    @Mock
    WeatherDataSource localDataSource;

    @Mock
    WeatherDataSource remoteDataSource;

    private ArgumentCaptor<City> cityArgumentCaptor = ArgumentCaptor.forClass(City.class);

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);

        RxTestUtil.mockRxSchedulers();

        repository = new WeatherRepository(localDataSource, remoteDataSource);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void saveOrUpdateCityShouldThrowException() {
        repository.saveOrUpdateCity(WeatherTestUtil.generateCity()).blockingGet();
    }

    @Test
    public void addCityByNameShouldSaveReturnCityFromServer() {
        City city = WeatherTestUtil.generateCity();
        when(remoteDataSource.addCity(city.getName())).thenReturn(Single.just(city));
        when(localDataSource.saveOrUpdateCity(city)).thenReturn(Single.just(city));

        TestObserver<City> testObserver = repository.addCity(city.getName()).test();
        testObserver.assertNoErrors();
        verify(localDataSource, times(1)).saveOrUpdateCity(city);
    }

    @Test
    public void addCityByCoordinatesShouldSaveReturnCityFromServerWithCurrentLocationFlag() {
        City city = WeatherTestUtil.generateCity();
        city.setCurrentLocationCity(false);
        double log = TestUtil.generateDouble();
        double lat = TestUtil.generateDouble();
        when(remoteDataSource.addCity(log, lat)).thenReturn(Single.just(city));
        when(localDataSource.saveOrUpdateCity(cityArgumentCaptor.capture())).thenReturn(Single.just(city));

        TestObserver<City> testObserver = repository.addCity(log, lat).test();
        testObserver.assertNoErrors();
        assertEquals(city.getId(), cityArgumentCaptor.getValue().getId());
        assertEquals(city.getName(), cityArgumentCaptor.getValue().getName());
        assertEquals(true, cityArgumentCaptor.getValue().isCurrentLocationCity());
    }

    @Test
    public void updateWeatherInfoShouldCallRemoteUpdateForEachSavedCityExceptCurretLocation() {
        City city1 = WeatherTestUtil.generateCity();
        city1.setCurrentLocationCity(true);
        City city2 = WeatherTestUtil.generateCity();
        city2.setCurrentLocationCity(false);
        List<City> cities = Arrays.asList(city1, city2);

        when(localDataSource.getAddedCitiesWeather()).thenReturn(Maybe.just(cities));
        when(localDataSource.saveOrUpdateCity(city1)).thenReturn(Single.just(city1));
        when(localDataSource.saveOrUpdateCity(city2)).thenReturn(Single.just(city2));
        when(remoteDataSource.addCity(city1.getName())).thenReturn(Single.just(city1));
        when(remoteDataSource.addCity(city2.getName())).thenReturn(Single.just(city2));

        TestObserver<List<City>> testObserver = repository.updateWeatherInfo().test();
        testObserver.assertNoErrors();
        testObserver.assertValue(Arrays.asList(city2));
        verify(remoteDataSource, never()).addCity(city1.getName());
        verify(remoteDataSource, times(1)).addCity(city2.getName());
    }

    @Test
    public void getAddedCitiesWeatherShouldReturnLocalDataSourceResult() {
        City city1 = WeatherTestUtil.generateCity();
        City city2 = WeatherTestUtil.generateCity();
        List<City> cities = Arrays.asList(city1, city2);

        when(localDataSource.getAddedCitiesWeather()).thenReturn(Maybe.just(cities));
        TestObserver<List<City>> testObserver = repository.getAddedCitiesWeather().test();
        testObserver.assertNoErrors();
        testObserver.assertValue(cities);
        verify(remoteDataSource, never()).getAddedCitiesWeather();
    }

}