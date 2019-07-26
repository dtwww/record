package com.example.shouye.weather;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;

import com.example.shouye.R;

public class WeatherMain extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather_main);
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        //如果用户上一次已经选择了地区 就从sp数据库中读取缓存信息直接显示此地区的详细信息
        if(prefs.getString("weather", null) != null){
            Intent intent = new Intent(this, WeatherActivity.class);
            startActivity(intent);
            finish();
        }
    }
}