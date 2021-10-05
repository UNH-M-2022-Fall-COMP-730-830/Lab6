package com.example.weather.data;

public class ForecastDetails {
    private String description;
    private String value;

    public ForecastDetails(String description, String value) {
        this.description = description;
        this.value = value;
    }

    public String getDescription() {
        return description;
    }

    public String getValue() {
        return value;
    }
}
