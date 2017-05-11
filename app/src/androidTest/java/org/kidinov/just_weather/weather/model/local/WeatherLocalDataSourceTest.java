package org.kidinov.just_weather.weather.model.local;

import android.support.test.InstrumentationRegistry;
import android.support.test.annotation.UiThreadTest;
import android.support.test.runner.AndroidJUnit4;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.kidinov.just_weather.WeatherTestUtil;
import org.kidinov.just_weather.util.TestUtil;

import java.io.IOException;
import java.util.List;

import io.reactivex.observers.TestObserver;
import io.realm.Realm;
import io.realm.RealmConfiguration;

import static junit.framework.Assert.assertEquals;

@RunWith(AndroidJUnit4.class)
public class WeatherLocalDataSourceTest {
    private WeatherLocalDataSource dataSource;

    private Realm realm;

    @Before
    @UiThreadTest
    public void setup() throws IOException {
        Realm.init(InstrumentationRegistry.getTargetContext());
        RealmConfiguration config = new RealmConfiguration.Builder().inMemory().build();
        realm = Realm.getInstance(config);

        dataSource = new WeatherLocalDataSource(realm);
    }

    @After
    @UiThreadTest
    public void tearDown() {
        realm.close();
        Realm.deleteRealm(realm.getConfiguration());
    }

    @Test
    @UiThreadTest
    public void getAddedCitiesWeatherShouldReturnAllSavedCities() {
        City city = WeatherTestUtil.generateCity();
        saveCityInDb(city);

        TestObserver<List<City>> testObserver = dataSource.getAddedCitiesWeather().test();
        testObserver.assertNoErrors();
        testObserver.assertValueCount(1);
        assertEquals(city.getId(), testObserver.values().get(0).get(0).getId());
        assertEquals(city.getName(), testObserver.values().get(0).get(0).getName());
    }

    @Test
    @UiThreadTest
    public void getAddedCitiesWeatherShouldReturnEmptyIfNoSavedCities() {
        TestObserver<List<City>> testObserver = dataSource.getAddedCitiesWeather().test();
        testObserver.assertNoErrors();
        testObserver.assertNoValues();
        testObserver.assertComplete();
    }

    @Test
    @UiThreadTest
    public void saveOrUpdateCityShouldCreateNewObjectInDbIfItWasntThereAndIfItsNotCurrentLocalCity() {
        City city1 = WeatherTestUtil.generateCity();
        city1.setCurrentLocationCity(false);
        City city2 = WeatherTestUtil.generateCity();
        city2.setCurrentLocationCity(false);
        TestObserver<City> testObserver = dataSource.saveOrUpdateCity(city1).test();
        testObserver.assertNoErrors();

        testObserver = dataSource.saveOrUpdateCity(city2).test();
        testObserver.assertNoErrors();

        List<City> cities = getStoredCities();
        assertEquals(2, cities.size());
    }

    @Test
    @UiThreadTest
    public void saveOrUpdateCityShouldNotOverrideCurrentLocationFlag() {
        City currentCity = WeatherTestUtil.generateCity();
        currentCity.setCurrentLocationCity(true);

        saveCityInDb(currentCity);

        currentCity.setCurrentLocationCity(false);
        TestObserver<City> testObserver = dataSource.saveOrUpdateCity(currentCity).test();
        testObserver.assertNoErrors();

        List<City> cities = getStoredCities();
        assertEquals(1, cities.size());
        assertEquals(currentCity.getId(), cities.get(0).getId());
        assertEquals(currentCity.getName(), cities.get(0).getName());
        assertEquals(true, cities.get(0).isCurrentLocationCity());
    }

    @Test
    @UiThreadTest
    public void saveOrUpdateCityShouldUpdateCurrentLocationity() {
        City currentCity = WeatherTestUtil.generateCity();
        currentCity.setCurrentLocationCity(true);

        saveCityInDb(currentCity);

        City currentNewCity = WeatherTestUtil.generateCity();
        currentNewCity.setCurrentLocationCity(true);
        TestObserver<City> testObserver = dataSource.saveOrUpdateCity(currentNewCity).test();
        testObserver.assertNoErrors();

        List<City> cities = getStoredCities();
        assertEquals(1, cities.size());
        assertEquals(currentNewCity.getId(), cities.get(0).getId());
        assertEquals(currentNewCity.getName(), cities.get(0).getName());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void addCityByNameShouldThrowException() {
        dataSource.addCity(TestUtil.generateString(5)).blockingGet();
    }

    @Test(expected = UnsupportedOperationException.class)
    public void addCityByCoordinatesShouldThrowException() {
        dataSource.addCity(TestUtil.generateDouble(), TestUtil.generateDouble()).blockingGet();
    }

    @Test(expected = UnsupportedOperationException.class)
    public void updateWeatherInfoShouldThrowException() {
        dataSource.updateWeatherInfo().blockingGet();
    }

    private void saveCityInDb(City currentCity) {
        realm.executeTransaction(x -> realm.copyToRealm(currentCity));
    }

    private List<City> getStoredCities() {
        return realm.where(City.class).findAll();
    }


}