package com.example.shouye;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by ThinkPad on 2018/1/3.
 */

public class NightModeConfig {
    private SharedPreferences mSharedPreference;
    private static final String NIGHT_MODE = "Night_Mode";
    public static final String IS_NIGHT_MODE = "Is_Night_Mode";

    private boolean mIsNightMode;

    private  SharedPreferences.Editor mEditor;

    private static NightModeConfig sModeConfig;

    public static NightModeConfig getInstance(){

        return sModeConfig !=null?sModeConfig : new NightModeConfig();
    }

    //现在是不是夜间模式
    public boolean getNightMode(Context context){

        if (mSharedPreference == null) {
            mSharedPreference = context.getSharedPreferences(NIGHT_MODE,context.MODE_PRIVATE);
        }
        mIsNightMode = mSharedPreference.getBoolean(IS_NIGHT_MODE, false);
        return mIsNightMode;
    }

    //保存夜间模式
    public void setNightMode(Context context, boolean isNightMode){
        if (mSharedPreference == null) {
            mSharedPreference = context.getSharedPreferences(NIGHT_MODE,context.MODE_PRIVATE);
        }
        mEditor = mSharedPreference.edit();

        mEditor.putBoolean(IS_NIGHT_MODE,isNightMode);
        mEditor.commit();
    }
}
