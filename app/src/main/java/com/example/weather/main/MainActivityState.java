package com.example.weather.main;

import com.example.weather.data.Forecast;

import java.util.List;

public interface MainActivityState {
    void enter();
    void refresh();
    void newData(List<Forecast> data);
    void error(Exception e);
}
