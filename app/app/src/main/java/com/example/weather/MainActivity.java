package com.example.weather;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.example.weather.adapters.DayAdapter;
import com.example.weather.models.DayForecast;

import java.util.ArrayList;
import java.util.Arrays;

public class MainActivity extends AppCompatActivity {
    String str = "";

    ArrayList<DayForecast> weathers = new ArrayList<DayForecast>(Arrays.asList(
            new DayForecast("Monday", "17", "23", "Windy", "3.6"),
            new DayForecast("Tuesday", "17", "23", "Windy", "3.6"),
            new DayForecast("Wednesday", "17", "23", "Windy", "3.6"),
            new DayForecast("Thursday", "17", "23", "Windy", "3.6"),
            new DayForecast("Friday", "17", "23", "Windy", "3.6"),
            new DayForecast("Saturday", "17", "23", "Windy", "3.6"),
            new DayForecast("Sunday", "17", "23", "Windy", "3.6")));

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        LinearLayoutManager layoutManager
                = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.days);
        recyclerView.setLayoutManager(layoutManager);
        DayAdapter adapter = new DayAdapter(getApplicationContext(), weathers);
        recyclerView.setAdapter(adapter);

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
                                str = "F";
                                break;
                            case 1:
                                str = "C";
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
                                TextView textView = findViewById(R.id.city);
                                textView.setText(city);
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
            setTheme(theme);

        } else {
            SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
            int theme = sp.getInt("THEME", R.style.AppThemeDark);
            setTheme(theme);

        }
    }
}