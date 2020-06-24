package com.example.myapplication.utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * @author sly
 * @version 1.0
 * @date 2020/5/13
 * @description com.example.myapplication.utils
 */
public class SpUtils {
    public static void putString(Context context, String name,String key, String value) {
        SharedPreferences sp = context.getSharedPreferences(name, Context.MODE_PRIVATE);
        SharedPreferences.Editor edit = sp.edit();
        edit.putString(key, value);
        edit.commit();
    }

    public static String getString(Context context,String name, String key, String def) {
        SharedPreferences sp = context.getSharedPreferences(name, Context.MODE_PRIVATE);
        return sp.getString(key, def);
    }

}
