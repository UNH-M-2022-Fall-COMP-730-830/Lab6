package com.example.weather.network;

import android.net.Uri;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import com.example.weather.data.Forecast;
import com.example.weather.preferences.Preferences;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.net.URL;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

public final class WeatherAPI {
    private static final String TAG = "WeatherAPI";

    private static final String BASE_URL = "https://api.weatherbit.io/";
    private static final String VERSION = "v2.0";
    private static final String FORECAST_ENDPOINT = "forecast";
    private static final String DAILY_ENDPOINT = "daily";

    private static final String API_KEY_PARAM = "key";
    private static final String CITY_PARAM = "city";
    private static final String DAYS_PARAM = "days";
    private static final String UNITS_PARAM = "units";
    private static final String DAYS = "5";

    private static final String API_KEY = "API_KEY";

    private static URL buildDailyForecastUrl() {
        String units = Preferences.getInstance().getUnits();
        String city = Preferences.getInstance().getCity();

        Uri weatherQueryUri = Uri.parse(BASE_URL).buildUpon()
            .appendPath(VERSION)
            .appendPath(FORECAST_ENDPOINT)
            .appendPath(DAILY_ENDPOINT)
            .appendQueryParameter(API_KEY_PARAM, API_KEY)
            .appendQueryParameter(DAYS_PARAM, DAYS)
            .appendQueryParameter(UNITS_PARAM, units)
            .appendQueryParameter(CITY_PARAM, city)
            .build();

        try {
            URL weatherQueryUrl = new URL(weatherQueryUri.toString());
            Log.v(TAG, "URL: " + weatherQueryUrl);
            return weatherQueryUrl;
        } catch (MalformedURLException e) {
            e.printStackTrace();
            return null;
        }
    }

    private static List<Forecast> parseForecast(String json) throws JSONException, ParseException {
        List<Forecast> data = new ArrayList<>();

        JSONObject response = new JSONObject(json);
        JSONArray forecastArray = response.getJSONArray("data");
        for (int i = 0; i < forecastArray.length(); i++) {
            JSONObject forecastObject = forecastArray.getJSONObject(i);
            Forecast forecast = Forecast.fromJson(forecastObject);
            data.add(forecast);
        }
        return data;
    }

    public static void requestForecast(NetworkRequestCallback<List<Forecast>> callback) {
        new Thread() {
            public void run() {
                Handler resultHandler = new Handler(Looper.getMainLooper());
                try {
                    URL url = WeatherAPI.buildDailyForecastUrl();
                    String json = NetworkUtils.getResponseFromHttpUrl(url);
                    Log.v(TAG, json);
                    List<Forecast> data = parseForecast(json);
                    resultHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            callback.onResult(data);
                        }
                    });
                } catch (Exception e) {
                    resultHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            callback.onError(e);
                        }
                    });
                }
            }
        }.start();
    }
}
