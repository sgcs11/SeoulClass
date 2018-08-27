package com.sc.jn.seoulclass.Util;

import android.content.Context;
import android.content.SharedPreferences;

public class ManageSharedPreference {

    public static void insertPreference(String key, String value, Context context){
        SharedPreferences pref = context.getSharedPreferences("pref",Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString(key,value);
        editor.commit();
    }
    public static String getPreference(String key, Context context){
        SharedPreferences pref = context.getSharedPreferences("pref",Context.MODE_PRIVATE);
        return pref.getString(key, "");
    }
}
