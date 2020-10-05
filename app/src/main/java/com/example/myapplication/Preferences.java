package com.example.myapplication;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

import android.util.ArrayMap;
import android.util.Log;


import java.util.Map;

public class Preferences {
    static SharedPreferences settings;

    Preferences(Context context){
        if(settings==null) {
        settings = context.getSharedPreferences("FileName", Activity.MODE_PRIVATE);
        }
    }


    public static String getString(String key){
        return settings.getString(key,"");
    }
    public static void saveString(String key, String value){
        SharedPreferences.Editor editor = settings.edit();
        editor.putString(key,value);
        editor.commit();
    }
    public static Boolean getBoolean(String key){
        return settings.getBoolean(key,false);
    }
    public static void saveBoolean(String key, Boolean value){
        SharedPreferences.Editor editor = settings.edit();
        editor.putBoolean(key,value);
        editor.commit();
    }
    public static Integer getInt(String key){
        return settings.getInt(key,0);
    }
    public static void saveInt(String key, Integer value){
        SharedPreferences.Editor editor = settings.edit();
        editor.putInt(key,value);
        editor.commit();
    }
    public static void loadAllListApps(){
        MainActivity.listApp = (Map<String, Integer>) settings.getAll();
        for(Map.Entry<String, Integer> app: MainActivity.listApp.entrySet()){
            Log.e("SEXXA",app.getValue()+"----"+app.getKey());
        }
    }
}
