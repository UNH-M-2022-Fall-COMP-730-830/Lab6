package com.example.weather.main;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.RecyclerView;

import com.example.weather.R;
import com.example.weather.data.Forecast;
import com.example.weather.details.DetailsActivity;
import com.example.weather.network.NetworkRequestCallback;
import com.example.weather.network.WeatherAPI;
import com.example.weather.preferences.PreferenceObserver;
import com.example.weather.preferences.Preferences;
import com.example.weather.settings.SettingsActivity;
import com.google.android.material.button.MaterialButton;

import java.util.List;

public class MainActivity extends AppCompatActivity implements
    NetworkRequestCallback<List<Forecast>>,
    OnForecastCLickListener,
    PreferenceObserver {

    private Toolbar toolbar;
    private RecyclerView listView;
    private LinearLayout errorView;
    private MaterialButton retryButton;
    private ProgressBar progress;
    private ForecastAdapter adapter;

    private MainActivityState state;

    public void setState(MainActivityState state) {
        this.state = state;
        state.enter();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initToolbar();
        initListView();
        initProgress();
        initErrorView();

        setState(new Loading());

        Preferences.getInstance().registerObserver(this);
    }

    private void initToolbar() {
        toolbar = findViewById(R.id.toolbar);
        String cityName = Preferences.getInstance().getCity();
        toolbar.setTitle(cityName);
        toolbar.setOnMenuItemClickListener(item -> {
            if (item.getItemId() == R.id.settings) {
                Intent activity = new Intent(this, SettingsActivity.class);
                startActivity(activity);
                return true;
            }
            return false;
        });
    }

    private void initListView() {
        listView = findViewById(R.id.listView);
        adapter = new ForecastAdapter();
        adapter.setForecastClickListener(this);
        listView.setAdapter(adapter);
    }

    private void initProgress() {
        progress = findViewById(R.id.progress);
    }

    private void initErrorView() {
        errorView = findViewById(R.id.errorView);
        retryButton = findViewById(R.id.retryButton);
        retryButton.setOnClickListener(v -> refreshForecast());
    }

    private void refreshForecast() {
        // Delegate refresh to current state
        state.refresh();
    }

    @Override
    public void onResult(List<Forecast> data) {
        // Delegate data handling to current state
        state.newData(data);
    }

    @Override
    public void onError(Exception e) {
        // Delegate error handling to current state
        state.error(e);
    }

    @Override
    public void onForecastClick(Forecast item) {
        Intent activity = new Intent(this, DetailsActivity.class);
        activity.putExtra(DetailsActivity.FORECAST_EXTRA, item);
        startActivity(activity);
    }

    @Override
    public void preferenceChanged(String key, Object value) {
        refreshForecast();
        if (key.equals(Preferences.CITY_KEY) && value instanceof String) {
            toolbar.setTitle((String) value);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Preferences.getInstance().unregisterObserver(this);
    }

    // MainActivity States. Implemented as inner classes to get full access to the MainActivity methods and attributes.

    class Loading implements MainActivityState {
        @Override
        public void enter() {
            progress.setVisibility(View.VISIBLE);
            errorView.setVisibility(View.GONE);
            listView.setVisibility(View.GONE);

            WeatherAPI.requestForecast(MainActivity.this);
        }

        @Override
        public void refresh() {
            // Already loading
        }

        @Override
        public void newData(List<Forecast> data) {
            setState(new Loaded(data));
        }

        @Override
        public void error(Exception e) {
            setState(new Error(e));
        }
    }

    class Loaded implements MainActivityState {
        private List<Forecast> data;

        public Loaded(List<Forecast> data) {
            this.data = data;
        }

        @Override
        public void enter() {
            adapter.setData(data);

            progress.setVisibility(View.GONE);
            errorView.setVisibility(View.GONE);
            listView.setVisibility(View.VISIBLE);

        }

        @Override
        public void refresh() {
            setState(new Loading());
        }

        @Override
        public void newData(List<Forecast> data) {
            // Receiving new data in this state is disabled
        }

        @Override
        public void error(Exception e) {
            // Receiving errors in this state is disabled
        }
    }

    class Error implements MainActivityState {
        private Exception error;

        public Error(Exception error) {
            this.error = error;
        }

        @Override
        public void enter() {
            error.printStackTrace();
            progress.setVisibility(View.GONE);
            errorView.setVisibility(View.VISIBLE);
            listView.setVisibility(View.GONE);
        }

        @Override
        public void refresh() {
            setState(new Loading());
        }

        @Override
        public void newData(List<Forecast> data) {
            // Receiving new data in this state is disabled
        }

        @Override
        public void error(Exception e) {
            // Already got an error
        }
    }
}