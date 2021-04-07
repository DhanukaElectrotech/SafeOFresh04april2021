package com.dhanuka.morningparcel.utils;


import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

/**
 * Created by Mr.Mad on 9/05/2019.
 */
public class AppThemePreferences {

    // App preference Name
    public static final String Prefsname = "safe_o_kart";


    public static final String KEY_THEME = "MORNING_PARCEL_theme";


    public static void setTheme(Context context, String theme) {
        SharedPreferences prefs = context.getSharedPreferences(Prefsname,
                context.MODE_PRIVATE);
        Editor editor = prefs.edit();
        editor.putString(KEY_THEME, theme);
        editor.commit();
    }

    public static String getTheme(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(Prefsname,
                context.MODE_PRIVATE);
        return prefs.getString(KEY_THEME, "light");
    }

    public static void clearPrefsdata(Context ctx) {
        SharedPreferences prefs = ctx.getSharedPreferences(Prefsname,
                ctx.MODE_PRIVATE);
        prefs.edit().clear().commit();
    }


}


