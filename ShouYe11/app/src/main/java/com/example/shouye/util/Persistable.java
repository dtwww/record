package com.example.shouye.util;
import android.content.Context;

//记得保存
public abstract class Persistable {
    public void save(Context context) {
        Persister persister = Persister.getInstance(context);
        persister.save(this);
    }
}
