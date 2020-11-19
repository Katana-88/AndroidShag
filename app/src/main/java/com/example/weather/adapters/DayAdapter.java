package com.example.weather.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.weather.R;
import com.example.weather.models.DayForecast;
import java.util.ArrayList;

public class DayAdapter extends RecyclerView.Adapter<DayAdapter.ViewHolder>{

    private LayoutInflater inflater;
    private ArrayList<DayForecast> days;

    public DayAdapter(@NonNull Context context, ArrayList<DayForecast> list) {
        this.inflater = LayoutInflater.from(context);
        this.days = list;
    }

    @Override
    public DayAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = inflater.inflate(R.layout.day_forecast, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(DayAdapter.ViewHolder holder, int position) {
        DayForecast p = days.get(position);
        holder.day_of_the_week.setText(p.day);
        holder.tem_min.setText(p.tempMin);
        holder.tem_max.setText(p.tempMax);
        holder.humidity.setText(p.humidity);
        holder.wind.setText(p.wind);
    }

    @Override
    public int getItemCount() {
        return days.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        final TextView day_of_the_week, tem_min, tem_max, humidity, wind;
        ViewHolder(View v) {
            super(v);
            day_of_the_week = v.findViewById(R.id.day_of_the_week);
            tem_min = v.findViewById(R.id.tem_min);
            tem_max = v.findViewById(R.id.tem_max);
            humidity = v.findViewById(R.id.humidity);
            wind = v.findViewById(R.id.wind);
        }
    }
}