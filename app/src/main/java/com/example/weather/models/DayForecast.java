package com.example.weather.models;

public class DayForecast {
    public String day;
    public String tempMin;
    public String tempMax;
    public String humidity;
    public String wind;

    public DayForecast(String day, String tempMin, String tempMax, String humidity, String wind) {
        this.day = day;
        this.tempMin = tempMin;
        this.tempMax = tempMax;
        this.humidity = humidity;
        this.wind = wind;
    }
}