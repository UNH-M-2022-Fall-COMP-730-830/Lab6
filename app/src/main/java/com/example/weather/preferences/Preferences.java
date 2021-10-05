package com.example.weather.preferences;

import android.content.SharedPreferences;

import com.example.weather.Application;

import java.util.ArrayList;
import java.util.List;

public class Preferences {
    public final static String CITY_KEY = "CITY";
    private final static String CITY_DEFAULT = "Manchester, NH";
    public final static String UNITS_KEY = "UNITS";
    private final static String UNITS_DEFAULT = "I";

    private static Preferences INSTANCE;

    // List of observers
    private final List<PreferenceObserver> observerCollection = new ArrayList<>();

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
        notifyPreferenceChanged(CITY_KEY, value); // Notify about city name change
    }

    public String getUnits() {
        return Application.getSharedPreferences().getString(UNITS_KEY, UNITS_DEFAULT);
    }

    public void setUnits(String value) {
        SharedPreferences pref = Application.getSharedPreferences();
        pref.edit().putString(UNITS_KEY, value).apply();
        notifyPreferenceChanged(UNITS_KEY, value); // Notify about units change
    }

    // Add new subscriber
    public void registerObserver(PreferenceObserver observer) {
        observerCollection.add(observer);
    }

    // Unsubscribe
    public void unregisterObserver(PreferenceObserver observer) {
        observerCollection.remove(observer);
    }

    // Notify all subscribers
    private void notifyPreferenceChanged(String key, Object value) {
        for (PreferenceObserver observer : observerCollection) {
            observer.preferenceChanged(key, value);
        }
    }
}
