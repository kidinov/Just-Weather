package org.kidinov.w_app.weather.view;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import org.kidinov.w_app.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class WeatherAddCityDialog extends DialogFragment {
    @BindView(R.id.city_name_edit_text)
    EditText cityNameEt;

    public static WeatherAddCityDialog newInstance() {
        return new WeatherAddCityDialog();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.weather_add_city_dialog, null);
        ButterKnife.bind(this, view);

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(R.string.add_city);
        builder.setNegativeButton(android.R.string.cancel, null);
        builder.setPositiveButton(android.R.string.ok, (dialogInterface, i) ->
                ((WeatherActivity) getActivity()).addCityByName(cityNameEt.getText().toString()));
        builder.setView(view);
        Dialog dialog = builder.create();

        dialog.setOnShowListener(x -> {
            InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.showSoftInput(cityNameEt, InputMethodManager.SHOW_IMPLICIT);
        });

        return dialog;
    }

}
