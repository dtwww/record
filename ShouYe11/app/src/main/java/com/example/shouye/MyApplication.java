package com.example.shouye;

import android.app.Application;
import android.support.v7.app.AppCompatDelegate;

/**
 * Created by ThinkPad on 2018/1/3.
 */

public class MyApplication extends Application{
    @Override
    public void onCreate() {
        super.onCreate();

        //根据app上次退出的状态来判断是否需要设置夜间模式,提前在SharedPreference中存了一个是否是夜间模式的boolean值
        boolean isNightMode = NightModeConfig.getInstance().getNightMode(getApplicationContext());
        if (isNightMode) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        }else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }
    }
}
