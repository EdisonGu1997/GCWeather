package com.gsoft.gcweather.gson;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by edison on 2017/11/24 0024.
 */

public class Weather {

    public String status;

    public AQI aqi;

    public Basic basic;

    public Now now;

    public Suggestion suggestion;

    @SerializedName("daily_forecast")
    public List<Forecast> forecastList;

}
