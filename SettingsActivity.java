package com.dvnech.randomalarm;

import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

/**
 * Created by Dmitry on 31.10.2016.
 */

//Настройки приложения
public class SettingsActivity extends AppCompatActivity {

    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_activity);

        toolbar = (Toolbar)findViewById(R.id.app_bar_settings);
        toolbar.setElevation(5);
        toolbar.setTitleTextColor(Color.WHITE);
        setSupportActionBar(toolbar);

        getFragmentManager().beginTransaction()
                .replace(R.id.container, new SettingsFragment())
                .commit();
    }

}
