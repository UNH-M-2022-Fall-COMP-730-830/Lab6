package com.example.weather.data;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Forecast implements Serializable {

    private final Date date;
    private final double temp;
    private final double highTemp;
    private final double lowTemp;
    private final String description;
    private final String icon;
    private final double windSpeed;
    private final double precipitationP;
    private final double precipitationA;
    private final double humidity;

    private Forecast(Date date, double temp, double highTemp, double lowTemp, String description, String icon, double windSpeed, double precipitationP, double precipitationA, double humidity) {
        this.date = date;
        this.temp = temp;
        this.highTemp = highTemp;
        this.lowTemp = lowTemp;
        this.description = description;
        this.icon = icon;
        this.windSpeed = windSpeed;
        this.precipitationP = precipitationP;
        this.precipitationA = precipitationA;
        this.humidity = humidity;
    }

    public Date getDate() {
        return date;
    }

    public double getTemp() {
        return temp;
    }

    public double getHighTemp() {
        return highTemp;
    }

    public double getLowTemp() {
        return lowTemp;
    }

    public String getDescription() {
        return description;
    }

    public String getIcon() {
        return icon;
    }

    public double getWindSpeed() {
        return windSpeed;
    }

    public double getPrecipitationP() {
        return precipitationP;
    }

    public double getPrecipitationA() {
        return precipitationA;
    }

    public double getHumidity() {
        return humidity;
    }

    public String getDateString(String pattern) {
        return new SimpleDateFormat(pattern).format(date);
    }

    public static Forecast fromJson(JSONObject json) throws ParseException, JSONException {
        String dateString = json.getString("valid_date");
        Date date = new SimpleDateFormat("yyyy-MM-dd").parse(dateString);

        double temp = json.getDouble("temp");
        double highTemp = json.getDouble("high_temp");
        double lowTemp = json.getDouble("low_temp");

        double windSpeed = json.getDouble("wind_spd");
        double precipitationP = json.getDouble("pop");
        double precipitationM = json.getDouble("precip");
        double humidity = json.getDouble("rh");

        JSONObject weather = json.getJSONObject("weather");
        String icon = weather.getString("icon");
        String description = weather.getString("description");
        return new Forecast(date, temp, highTemp, lowTemp, description, icon, windSpeed, precipitationP, precipitationM, humidity);
    }

    public List<ForecastDetails> getDetails() {
        List<ForecastDetails> details = new ArrayList<>();
        details.add(new ForecastDetails("High Temperature", getHighTemp() + "\u00b0"));
        details.add(new ForecastDetails("Low Temperature", getLowTemp() + "\u00b0"));
        details.add(new ForecastDetails("Wind Speed", getWindSpeed() + ""));
        details.add(new ForecastDetails("Probability of Precipitation", getPrecipitationP() + "%"));
        details.add(new ForecastDetails("Accumulated Precipitation", getPrecipitationA() + ""));
        details.add(new ForecastDetails("Humidity", getHumidity() + "%"));
        return details;
    }
}
