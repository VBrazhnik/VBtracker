package com.vb.tracker.free;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.vb.tracker.R;

public class Utility {

    final public static int THEME_BLACK_WHITE = 1;
    final public  static int THEME_WHITE_BLACK = 2;

    static void setTheme(Context context, int theme) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        prefs.edit().putInt(context.getString(R.string.prefs_theme_key), theme).apply();
    }

    public static int getTheme(Context context) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        return prefs.getInt(context.getString(R.string.prefs_theme_key), -1);
    }

    public static void updateTheme(Context context) {
        if (Utility.getTheme(context) <= THEME_BLACK_WHITE) {
            context.setTheme(R.style.AppTheme_BlackWhite);
        } else if (Utility.getTheme(context) == THEME_WHITE_BLACK) {
            context.setTheme(R.style.AppTheme_WhiteBlack);
        }
    }
}
