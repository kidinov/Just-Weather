package org.kidinov.just_weather.weather;


import org.kidinov.just_weather.common.base.BasePresenter;
import org.kidinov.just_weather.common.base.BaseView;
import org.kidinov.just_weather.weather.model.local.City;

import java.util.List;

public interface WeatherContract {
    interface View extends BaseView<Presenter> {
        void showProgress();

        void showData(List<City> cities);

        void showError();

        void hideItemAtPosition(int position);

        void showEmptyState();

        void showNetworkErrorNotification();

        void showCurrentCityDeletionErrorNotification();
    }

    interface Presenter extends BasePresenter {
        void updateData(boolean refresh);

        void addCityByCoordinates(double latitude, double longitude);

        void addCityByName(String name);

        void itemRemovedAtPosition(int swipedPosition);
    }
}
