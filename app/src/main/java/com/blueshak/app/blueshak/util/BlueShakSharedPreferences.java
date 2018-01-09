package com.blueshak.app.blueshak.util;

import android.app.Activity;
import android.content.Context;

/**
 * Created by lsingh013 on 07/01/18.
 */

public class BlueShakSharedPreferences {

    static String salesPreferences = "SalesData";

    public static void setSalesLatitude(Context context, String salesLatValue) {
        String saleLat = "SalesLat";
        android.content.SharedPreferences prefs = context.getSharedPreferences(salesPreferences, Activity.MODE_PRIVATE);
        android.content.SharedPreferences.Editor editor = prefs.edit();
        editor.putString(saleLat, salesLatValue);
        editor.apply();
    }

    public static String getSalesLatitude(Context context) {
        String langPref = "SalesLat";
        android.content.SharedPreferences prefs = context.getSharedPreferences(salesPreferences, Activity.MODE_PRIVATE);
        return prefs.getString(langPref, "");
    }

    public static void setSalesLongitude(Context context, String saleLongValue) {
        String langPref = "SalesLong";
        android.content.SharedPreferences prefs = context.getSharedPreferences(salesPreferences, Activity.MODE_PRIVATE);
        android.content.SharedPreferences.Editor editor = prefs.edit();
        editor.putString(langPref, saleLongValue);
        editor.apply();
    }

    public static String getSalesLongitude(Context context) {
        String langPref = "SalesLong";
        android.content.SharedPreferences prefs = context.getSharedPreferences(salesPreferences, Activity.MODE_PRIVATE);
        return prefs.getString(langPref, "");
    }

    public static void setSalesId(Context context, String saleIdValue) {
        String langPref = "SalesId";
        android.content.SharedPreferences prefs = context.getSharedPreferences(salesPreferences, Activity.MODE_PRIVATE);
        android.content.SharedPreferences.Editor editor = prefs.edit();
        editor.putString(langPref, saleIdValue);
        editor.apply();
    }

    public static String getSalesId(Context context) {
        String langPref = "SalesId";
        android.content.SharedPreferences prefs = context.getSharedPreferences(salesPreferences, Activity.MODE_PRIVATE);
        return prefs.getString(langPref, "");
    }

}
