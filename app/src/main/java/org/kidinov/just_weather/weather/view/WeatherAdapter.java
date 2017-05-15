package org.kidinov.just_weather.weather.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import org.kidinov.just_weather.R;
import org.kidinov.just_weather.util.remote.UIUtil;
import org.kidinov.just_weather.weather.model.local.City;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

public class WeatherAdapter extends RecyclerView.Adapter<WeatherAdapter.WeatherInCityViewHolder> {
    private List<City> weatherInCities;

    @Inject WeatherAdapter() {
        weatherInCities = new ArrayList<>();
    }

    public void setWeatherInCities(List<City> weatherInCities) {
        this.weatherInCities = weatherInCities;
        notifyDataSetChanged();
    }

    @Override
    public WeatherInCityViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.weather_item, parent, false);
        return new WeatherInCityViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final WeatherInCityViewHolder holder, int position) {
        City city = weatherInCities.get(position);
        holder.bind(city);
    }

    @Override
    public int getItemCount() {
        return weatherInCities.size();
    }

    class WeatherInCityViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.pic_image_view)
        ImageView picIv;
        @BindView(R.id.location_name_text_view)
        TextView locationNameTv;
        @BindView(R.id.temperature_text_view)
        TextView temperatureTv;
        @BindView(R.id.date_text_view)
        TextView dateTv;

        WeatherInCityViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        @SuppressLint("DefaultLocale")
        void bind(City city) {
            Context context = itemView.getContext();
            if (!city.getWeather().isEmpty()) {
                picIv.setImageDrawable(UIUtil.getDrawableByFileName(context, "ic_" +
                        city.getWeather().get(0).getIcon()));
            }
            locationNameTv.setText(String.format("%s/%s", city.getName(), city.getSys().getCountry()));
            if (city.isCurrentLocationCity()) {
                locationNameTv.setCompoundDrawablesWithIntrinsicBounds(null, null, ContextCompat.getDrawable(context,
                        R.drawable.ic_my_location_gray_16dp), null);
            } else {
                locationNameTv.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
            }
            temperatureTv.setText(String.format("%d°", ((int) city.getMain().getTemp().doubleValue())));
            Date date = new Date((long) city.getDt() * 1000);
            dateTv.setText(String.format("%s %s", DateFormat.getDateInstance(DateFormat.DATE_FIELD).format(date),
                    DateFormat.getTimeInstance(DateFormat.SHORT).format(date)));
        }
    }
}
