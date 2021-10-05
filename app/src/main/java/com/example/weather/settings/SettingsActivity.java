package com.example.weather.settings;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.weather.R;
import com.example.weather.preferences.Preferences;

public class SettingsActivity extends AppCompatActivity {

    private TextView cityName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        initToolbar();
        initCityView();
        initUnits();
    }

    private void initToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setNavigationOnClickListener(v -> {
            finish();
        });
    }

    private void initCityView() {
        cityName = findViewById(R.id.cityName);
        cityName.setText(Preferences.getInstance().getCity());

        View cityView = findViewById(R.id.cityContainer);
        cityView.setOnClickListener(v -> {
            showCityDialog();
        });
    }

    private void initUnits() {
        String unitKey = Preferences.getInstance().getUnits();
        int checkedId;
        switch (unitKey) {
            case "M":
                checkedId = R.id.unitMetric;
                break;
            case "S":
                checkedId = R.id.unitScientific;
                break;
            case "I":
            default:
                checkedId = R.id.unitImperial;
        }

        RadioGroup unitsGroup = findViewById(R.id.unitsGroup);
        unitsGroup.check(checkedId);
        unitsGroup.setOnCheckedChangeListener((group, id) -> {
            String unit = null;
            switch (id) {
                case R.id.unitMetric:
                    unit = "M";
                    break;
                case R.id.unitScientific:
                    unit = "S";
                    break;
                case R.id.unitImperial:
                    unit = "I";
                    break;
            }
            if (unit != null) {
                Preferences.getInstance().setUnits(unit);
            }
        });
    }

    private void showCityDialog() {
        View view = LayoutInflater.from(this).inflate(R.layout.dialog_city, null);
        EditText cityNameView = view.findViewById(R.id.cityInput);
        new AlertDialog.Builder(this)
            .setTitle(R.string.city_dialog_title)
            .setView(view)
            .setCancelable(true)
            .setPositiveButton(R.string.city_dialog_ok, (dialog, which) -> {
                String city = cityNameView.getText().toString();
                Preferences.getInstance().setCity(city);
                dialog.dismiss();
            })
            .setNegativeButton(R.string.city_dialog_cancel, (dialog, which) -> dialog.dismiss())
            .show();
    }
}

