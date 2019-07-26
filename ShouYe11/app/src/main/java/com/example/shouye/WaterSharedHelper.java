package com.example.shouye;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by ThinkPad on 2018/1/4.
 */

public class WaterSharedHelper {

    private Context mContext;

    public WaterSharedHelper(){

    }

    public WaterSharedHelper(Context mContext){
        this.mContext = mContext;
    }

    public void save(int number){
        SharedPreferences sp = mContext.getSharedPreferences("sp_water",Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putInt("number", number);
        editor.commit();
    }

    public int read() {
        SharedPreferences sp = mContext.getSharedPreferences("sp_water", Context.MODE_PRIVATE);
        int number = sp.getInt("number",1);
        return number;
    }
}
