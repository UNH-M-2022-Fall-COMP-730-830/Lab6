package com.example.weather.preferences;

import android.content.SharedPreferences;

import com.example.weather.Application;

public class Preferences {
    private final String CITY_KEY = "CITY";
    private final String CITY_DEFAULT = "Manchester, NH";
    private final String UNITS_KEY = "UNITS";
    private final String UNITS_DEFAULT = "I";

    private static Preferences INSTANCE;

    private Preferences() {
    }

    // synchronized method to control simultaneous access
    synchronized public static Preferences getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new Preferences();
        }
        return INSTANCE;
    }

    public String getCity() {
        return Application.getSharedPreferences().getString(CITY_KEY, CITY_DEFAULT);
    }

    public void setCity(String value) {
        SharedPreferences pref = Application.getSharedPreferences();
        pref.edit().putString(CITY_KEY, value).apply();
    }

    public String getUnits() {
        return Application.getSharedPreferences().getString(UNITS_KEY, UNITS_DEFAULT);
    }

    public void setUnits(String value) {
        SharedPreferences pref = Application.getSharedPreferences();
        pref.edit().putString(UNITS_KEY, value).apply();
    }
}
