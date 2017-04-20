package org.kidinov.w_app.weather;


import org.kidinov.w_app.common.base.BasePresenter;
import org.kidinov.w_app.common.base.BaseView;
import org.kidinov.w_app.weather.model.local.City;

import java.util.List;

public interface WeatherContract {
    interface View extends BaseView<Presenter> {
        void showProgress();

        void showData(List<City> cities);

        void showError();

        void showEmptyState();

        void showErrorNotification();
    }

    interface Presenter extends BasePresenter {
        void updateData(boolean refresh);

        void addCityByCoordinates(double latitude, double longitude);

        void addCityByName(String name);
    }
}
