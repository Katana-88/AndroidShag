package com.example.weather.models;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import android.app.Fragment;

import com.example.weather.R;

public class CurrentDayForecast extends Fragment {
    private final static String TAG = "==================== FirstFragment";
    public TextView tempMin;
    public TextView tempMax;
    public TextView description;
    public TextView wind;

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.day_current, container, false);
        this.tempMin = (TextView)v.findViewById(R.id.tv_curr_min_temp);
        this.tempMax = (TextView)v.findViewById(R.id.tv_curr_max_temp);
        this.description = (TextView)v.findViewById(R.id.tv_curr_description);
        this.wind = (TextView)v.findViewById(R.id.tv_curr_wind);
        return v;
    }

}
