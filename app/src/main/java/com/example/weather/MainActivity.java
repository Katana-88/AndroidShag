package com.example.weather;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.example.weather.adapters.DayAdapter;
import com.example.weather.models.CurrentDayForecast;
import com.example.weather.models.DayForecast;

import java.util.ArrayList;
import java.util.Arrays;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.widget.RadioGroup;

import com.example.weather.helper.DBForecastHelper;
import com.example.weather.models.Forecast;
import com.kwabenaberko.openweathermaplib.constants.Units;
import com.kwabenaberko.openweathermaplib.implementation.OpenWeatherMapHelper;
import com.kwabenaberko.openweathermaplib.implementation.callbacks.CurrentWeatherCallback;
import com.kwabenaberko.openweathermaplib.implementation.callbacks.ThreeHourForecastCallback;
import com.kwabenaberko.openweathermaplib.models.currentweather.CurrentWeather;
import com.kwabenaberko.openweathermaplib.models.threehourforecast.ThreeHourForecast;

public class MainActivity extends AppCompatActivity {
    String str = "";
    String cityName="Kherson";
    FragmentManager fragmentManager;
    FragmentTransaction fragmentTransaction;
    private OpenWeatherMapHelper weatherHelper;
    private DBForecastHelper dbForecastHelper;

//    ArrayList<DayForecast> weathers = new ArrayList<DayForecast>(Arrays.asList(
//            new DayForecast("Monday", "17", "23", "Windy", "3.6"),
//            new DayForecast("Tuesday", "17", "23", "Windy", "3.6"),
//            new DayForecast("Wednesday", "17", "23", "Windy", "3.6"),
//            new DayForecast("Thursday", "17", "23", "Windy", "3.6"),
//            new DayForecast("Friday", "17", "23", "Windy", "3.6"),
//            new DayForecast("Saturday", "17", "23", "Windy", "3.6"),
//            new DayForecast("Sunday", "17", "23", "Windy", "3.6")));

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViewById(R.id.llmain).setBackgroundColor(Color.parseColor("#DEB887"));
        fragmentManager = this.getFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();
        CurrentDayForecast fragment = new CurrentDayForecast();
        fragmentTransaction.add(R.id.flFragmentContainer, fragment);
        fragmentTransaction.commit();
        prepareRadioListeners();
        weatherHelper = new OpenWeatherMapHelper(getString(R.string.OPEN_WEATHER_MAP_API_KEY));
        weatherHelper.setUnits(Units.METRIC);
        dbForecastHelper =
                new DBForecastHelper(this);
        Forecast f = dbForecastHelper.getLastForecast();
        if (f != null) {
            setWeatherOnUi(f);
            UpdateWeather(WeatherType.Current);
        }

//        LinearLayoutManager layoutManager
//                = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
//        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.dayslist);
//        recyclerView.setLayoutManager(layoutManager);
//        DayAdapter adapter = new DayAdapter(getApplicationContext(), weathers);
//        recyclerView.setAdapter(adapter);
    }

    private void prepareRadioListeners() {
        RadioGroup.OnCheckedChangeListener listener = new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                WeatherType selectedType = GetSelectedWeatherType();
                UpdateWeather(selectedType);
            }
        };

        ((RadioGroup) findViewById(R.id.radios))
                .setOnCheckedChangeListener(listener);
    }
    private void UpdateWeather() {
        WeatherType type = GetSelectedWeatherType();
        UpdateWeather(type);
    }
    private WeatherType GetSelectedWeatherType() {
        int selectedId = ((RadioGroup) findViewById(R.id.radios)).getCheckedRadioButtonId();
        return GetSelectedWeatherType(selectedId);
    }
    private WeatherType GetSelectedWeatherType(int checkedRadioButtonId) {
        WeatherType selected;
        switch (checkedRadioButtonId) {
            case R.id.current:
                selected = WeatherType.Current;
                break;
            case R.id.hours:
                selected = WeatherType.Hourly;
                break;
//            case R.id.days:
//                selected = WeatherType.Daily;
//                break;
            default:
                throw new IllegalArgumentException(String.valueOf(checkedRadioButtonId));
        }
        return selected;
    }
    private void UpdateWeather(WeatherType selected) {

        //this.cityName = ((TextView) findViewById(R.id.cityName)).getText().toString();

        if (cityName.equals("")){
            Toast.makeText(getApplicationContext(), "No city provided", Toast.LENGTH_LONG).show();
            return;
        }

        switch (selected) {
            case Current:
                getCurrentWeather(cityName);
                break;
            case Hourly:
                getHoursWeather(cityName);
                break;
            case Daily:
                getDaysWeather(cityName);
                break;
            default:
                throw new IllegalArgumentException(String.valueOf(selected));
        }
    }

    private void getDaysWeather(String city) {
        setWeatherOnUi(new Forecast("Not Implemented Yet", "", "", "", ""));
    }

    private void getHoursWeather(String city) {
        weatherHelper.getThreeHourForecastByCityName(city, new ThreeHourForecastCallback() {
            @Override
            public void onSuccess(ThreeHourForecast threeHourForecast) {
                String str = Forecast.FromHoursForecast(threeHourForecast).temperature;
                String[] subStr;
                String delimeter = "/";
                subStr = str.split(delimeter);
                Fragment frag1 = getFragmentManager().findFragmentById(R.id.flFragmentContainer);
                ((TextView) frag1.getView().findViewById(R.id.tv_curr_min_temp))
                        .setText(subStr[0]);
                ((TextView) frag1.getView().findViewById(R.id.tv_curr_max_temp))
                        .setText(subStr[1]);
                ((TextView) frag1.getView().findViewById(R.id.tv_curr_description))
                        .setText(Forecast.FromHoursForecast(threeHourForecast).description);
                ((TextView) frag1.getView().findViewById(R.id.tv_curr_wind))
                        .setText(Forecast.FromHoursForecast(threeHourForecast).windSpeed);
                handleNewForecast(Forecast.FromHoursForecast(threeHourForecast));
            }

            @Override
            public void onFailure(Throwable throwable) {
                doOnWeatherFailure(throwable);
            }
        });
    }

    private void handleNewForecast(Forecast forecast) {
        setWeatherOnUi(forecast);
        dbForecastHelper.saveForecast(forecast);
    }


    private void getCurrentWeather(String city) {
        weatherHelper.getCurrentWeatherByCityName(city, new CurrentWeatherCallback() {
            @Override
            public void onSuccess(CurrentWeather currentWeather) {
                String str = Forecast.FromCurrentWeather(currentWeather).temperature;
                  String[] subStr;
                    String delimeter = "/";
                    subStr = str.split(delimeter);
                Fragment frag1 = getFragmentManager().findFragmentById(R.id.flFragmentContainer);
                ((TextView) frag1.getView().findViewById(R.id.tv_curr_min_temp))
                        .setText(subStr[0]);
                ((TextView) frag1.getView().findViewById(R.id.tv_curr_max_temp))
                        .setText(subStr[1]);
                ((TextView) frag1.getView().findViewById(R.id.tv_curr_description))
                        .setText(Forecast.FromCurrentWeather(currentWeather).description);
                ((TextView) frag1.getView().findViewById(R.id.tv_curr_wind))
                        .setText(Forecast.FromCurrentWeather(currentWeather).windSpeed);
                handleNewForecast(Forecast.FromCurrentWeather(currentWeather));
            }

            @Override
            public void onFailure(Throwable throwable) {
                doOnWeatherFailure(throwable);
            }
        });
    }

    private void doOnWeatherFailure(Throwable throwable) {
        ((TextView) findViewById(R.id.text)).setText(throwable.getMessage());
    }

    private void setWeatherOnUi(Forecast f) {
        ((TextView) findViewById(R.id.text_city)).setText(f.city);
        ((TextView) findViewById(R.id.text_country)).setText(f.country);
//        ((TextView) findViewById(R.id.text_wind)).setText(f.windSpeed);
//        ((TextView) findViewById(R.id.text_temp)).setText(f.temperature);
//        ((TextView) findViewById(R.id.text_description)).setText(f.description);
    }

    public void goWeather(View view) {
        UpdateWeather(GetSelectedWeatherType());
    }


    public enum WeatherType {
        Current,
        Hourly,
        Daily
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.change_theme:
                AlertDialog.Builder builder =
                        new AlertDialog.Builder(this, android.R.style.Theme_Holo_Light_Dialog);
                builder.setTitle("Choose theme:");
                LayoutInflater inflater = this.getLayoutInflater();
                final View view = inflater.inflate(
                        R.layout.dialog_change_theme,
                        null, false);
                builder.setView(view);
                AlertDialog dialog = builder.create();
                dialog.show();
                break;

            case R.id.change_temp:
            AlertDialog.Builder builder2 =
                    new AlertDialog.Builder(this, android.R.style.Theme_Holo_Light_Dialog);
            builder2.setTitle("Choose temp:");
                String[] items = {"F", "C"};
                int checkedItem = 0;
                builder2.setSingleChoiceItems(items, checkedItem, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case 0:
                                weatherHelper.setUnits(Units.IMPERIAL);
                                break;
                            case 1:
                                weatherHelper.setUnits(Units.METRIC);
                                break;
                        }
//                        dialog.dismiss();
                    }
                });
                builder2.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Toast.makeText(MainActivity.this, str, Toast.LENGTH_SHORT).show();
                    }
                });
                builder2.setNeutralButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                    }
                });

            AlertDialog dialog2 = builder2.create();
            dialog2.show();
            break;

            case R.id.change_location:
            AlertDialog.Builder builder3 =
                    new AlertDialog.Builder(this, android.R.style.Theme_Holo_Light_Dialog);
            builder3.setTitle("Choose location:");
            LayoutInflater inflater3 = this.getLayoutInflater();
            final View view3 = inflater3.inflate(
                    R.layout.dialog_change_location,
                    null, false);
            builder3.setView(view3);
            builder3.setPositiveButton("OK",
                        new DialogInterface.OnClickListener()
                        {
                            public void onClick(DialogInterface dialog, int id)
                            {
                                EditText etCity = (EditText) view3.findViewById(R.id.etCity);
                                String city = etCity.getText().toString();
                                cityName=city;
                            }
                        });
                builder3.setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener()
                        {
                            public void onClick(DialogInterface dialog, int id)
                            {
                            }
                        });
            AlertDialog dialog3 = builder3.create();
            dialog3.show();
            break;

            default:
                return super.onOptionsItemSelected(item);
        }
        return true;
    }

    public void onToggleClicked(View view) {
        boolean on = ((ToggleButton) view).isChecked();
        if (on) {
            SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
            int theme = sp.getInt("THEME", R.style.AppThemeLight);
            findViewById(R.id.llmain).setBackgroundColor(Color.parseColor("#DEB887"));
            setTheme(theme);

        } else {
            SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
            int theme = sp.getInt("THEME", R.style.AppThemeDark);
            findViewById(R.id.llmain).setBackgroundColor(Color.parseColor("#FF3700B3"));
            setTheme(theme);

        }
    }
}