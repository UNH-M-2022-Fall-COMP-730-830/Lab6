package com.example.weather.preferences;

public interface PreferenceObserver {
    void preferenceChanged(String key, Object value);
}
