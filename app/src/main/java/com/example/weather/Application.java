package com.example.weather;

import android.content.Context;
import android.content.SharedPreferences;

public class Application extends android.app.Application {
    private static Application INSTANCE;

    public static Application getInstance() {
        return INSTANCE;
    }

    public static SharedPreferences getSharedPreferences() {
        return getInstance().getSharedPreferences(BuildConfig.APPLICATION_ID, Context.MODE_PRIVATE);
    }

    @Override
    public void onCreate() {
        INSTANCE = this;
        super.onCreate();
    }
}
