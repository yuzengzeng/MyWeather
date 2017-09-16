package com.vivo.yzz.myweather.gson;

import java.util.List;

/**
 * Created by Administrator on 2017/9/16.
 */

public class Weather {
    public String status;

    public Basic basic;

    public AQI aqi;

    public Now now;

    public Suggestion suggestion;

    public List<Forecast> daily_forecast;

}
