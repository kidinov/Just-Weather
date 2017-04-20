package org.kidinov.w_app.weather.view;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.BaseTransientBottomBar;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.ViewFlipper;

import com.google.android.gms.location.LocationRequest;
import com.patloew.rxlocation.RxLocation;
import com.tbruyelle.rxpermissions2.RxPermissions;

import org.kidinov.w_app.App;
import org.kidinov.w_app.R;
import org.kidinov.w_app.util.RxUtil;
import org.kidinov.w_app.weather.DaggerWeatherComponent;
import org.kidinov.w_app.weather.WeatherContract;
import org.kidinov.w_app.weather.model.local.City;
import org.kidinov.w_app.weather.presentation.WeatherPresenter;
import org.kidinov.w_app.weather.presentation.WeatherPresenterModule;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.Observable;
import io.reactivex.disposables.CompositeDisposable;
import timber.log.Timber;

public class WeatherActivity extends AppCompatActivity implements WeatherContract.View {
    private CompositeDisposable compositeDisposable = new CompositeDisposable();

    @Inject
    WeatherPresenter presenter;
    @Inject
    WeatherAdapter adapter;
    @Inject
    RxPermissions rxPermissions;
    @Inject
    RxLocation rxLocation;

    @BindView(R.id.swipe_refresh_layout)
    SwipeRefreshLayout swipeRefreshLayout;
    @BindView(R.id.swipe_refresh_empty_layout)
    SwipeRefreshLayout swipeRefreshEmptyLayout;
    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;
    @BindView(R.id.view_flipper)
    ViewFlipper viewFlipper;
    @BindView(R.id.add_city_button)
    FloatingActionButton addCityButton;

    public static Intent startActivityForTesting() {
        Intent intent = new Intent();
        intent.putExtra("test", true);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        DaggerWeatherComponent.builder()
                .repositoryComponent(((App) getApplication()).getComponent())
                .weatherPresenterModule(new WeatherPresenterModule(this))
                .weatherActivityModule(new WeatherActivityModule(this))
                .build()
                .inject(this);
        setContentView(R.layout.weather_activity);

        ButterKnife.bind(this);

        initViews();
        presenter.subscribe();
        presenter.updateData(true);

        if (!getIntent().getBooleanExtra("test", false)) {
            getLocation();
        }
    }

    private void initViews() {
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);

        swipeRefreshLayout.setOnRefreshListener(this::fetchAllData);
        swipeRefreshEmptyLayout.setOnRefreshListener(this::fetchAllData);

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                if (dy > 0) {
                    if (addCityButton.isShown()) {
                        addCityButton.hide();
                    }
                } else if (dy < 0 && !addCityButton.isShown()) {
                    addCityButton.show();
                }
            }
        });
    }

    private void fetchAllData() {
        getLocation();
        presenter.updateData(true);
    }

    private void getLocation() {
        compositeDisposable.add(rxPermissions
                .request(Manifest.permission.ACCESS_COARSE_LOCATION)
                .flatMap(granted -> {
                    LocationRequest locationRequest = LocationRequest.create()
                            .setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
                    if (granted) {
                        //noinspection MissingPermission
                        return rxLocation
                                .location()
                                .updates(locationRequest)
                                .firstElement()
                                .toObservable();
                    } else {
                        return Observable.empty();
                    }
                })
                .doFinally(() -> swipeRefreshEmptyLayout.setRefreshing(false))
                .subscribe(location -> {
                    Timber.d("location - %s", location);
                    presenter.addCityByCoordinates(location.getLatitude(), location.getLongitude());
                }, Timber::e));
    }

    private void changeScreenState(int state) {
        viewFlipper.setDisplayedChild(viewFlipper.indexOfChild(viewFlipper.findViewById(state)));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        RxUtil.unsubscribe(compositeDisposable);
        presenter.unsubscribe();
    }

    @OnClick(R.id.retry_button)
    void clickOnRetryButton() {
        presenter.updateData(false);
    }

    @OnClick(R.id.add_city_button)
    void clickOnAddCityButton() {
        WeatherAddCityDialog.newInstance().show(getSupportFragmentManager(), "WeatherAddCityDialog");
    }

    public void addCityByName(String name) {
        presenter.addCityByName(name);
    }

    @Override
    public void showProgress() {
        changeScreenState(R.id.progress_view);
    }

    @Override
    public void showData(List<City> cities) {
        Timber.d("showData - %d", cities.size());
        swipeRefreshLayout.setRefreshing(false);
        changeScreenState(R.id.swipe_refresh_layout);
        adapter.setWeatherInCities(cities);
    }

    @Override
    public void showError() {
        swipeRefreshLayout.setRefreshing(false);
        changeScreenState(R.id.error_view);
    }

    @Override
    public void showEmptyState() {
        swipeRefreshLayout.setRefreshing(false);
        changeScreenState(R.id.swipe_refresh_empty_layout);
    }

    @Override
    public void showErrorNotification() {
        Snackbar.make(viewFlipper, R.string.error_text, BaseTransientBottomBar.LENGTH_LONG).show();
    }
}
