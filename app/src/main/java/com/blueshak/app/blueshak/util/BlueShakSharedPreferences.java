package com.blueshak.app.blueshak.util;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by lsingh013 on 07/01/18.
 */

public class BlueShakSharedPreferences {

    static String salesPreferences = "SalesData";
    static String chatPreferences = "ChatData";

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

    public static void setChatProductId(Context context, String chatIdValue) {
        String chatId = "ChatId";
        android.content.SharedPreferences prefs = context.getSharedPreferences(chatPreferences, Activity.MODE_PRIVATE);
        android.content.SharedPreferences.Editor editor = prefs.edit();
        editor.putString(chatId, chatIdValue);
        editor.apply();
    }

    public static String getChatProductId(Context context) {
        String chatId = "ChatId";
        android.content.SharedPreferences prefs = context.getSharedPreferences(salesPreferences, Activity.MODE_PRIVATE);
        return prefs.getString(chatId, "");
    }

    public static void setChatProductName(Context context, String nameValue) {
        String chat = "ChatProductName";
        android.content.SharedPreferences prefs = context.getSharedPreferences(chatPreferences, Activity.MODE_PRIVATE);
        android.content.SharedPreferences.Editor editor = prefs.edit();
        editor.putString(chat, nameValue);
        editor.apply();
    }

    public static String getChatProductName(Context context) {
        String chat = "ChatProductName";
        android.content.SharedPreferences prefs = context.getSharedPreferences(chatPreferences, Activity.MODE_PRIVATE);
        return prefs.getString(chat, "");
    }

    public static void setChatProductCurrency(Context context, String chatIdValue) {
        String chat = "ChatProductCurrency";
        android.content.SharedPreferences prefs = context.getSharedPreferences(chatPreferences, Activity.MODE_PRIVATE);
        android.content.SharedPreferences.Editor editor = prefs.edit();
        editor.putString(chat, chatIdValue);
        editor.apply();
    }

    public static String getChatProductCurrency(Context context) {
        String chat = "ChatProductCurrency";
        android.content.SharedPreferences prefs = context.getSharedPreferences(chatPreferences, Activity.MODE_PRIVATE);
        return prefs.getString(chat, "");
    }

    public static void setChatProductPrice(Context context, String chatIdValue) {
        String chat = "ChatProductPrice";
        android.content.SharedPreferences prefs = context.getSharedPreferences(chatPreferences, Activity.MODE_PRIVATE);
        android.content.SharedPreferences.Editor editor = prefs.edit();
        editor.putString(chat, chatIdValue);
        editor.apply();
    }

    public static String getChatProductPrice(Context context) {
        String chat = "ChatProductPrice";
        android.content.SharedPreferences prefs = context.getSharedPreferences(chatPreferences, Activity.MODE_PRIVATE);
        return prefs.getString(chat, "");
    }

    public static void removeChatId(Context mContext) {
        String chatId = "ChatId";
        if (mContext != null) {
            SharedPreferences mSharedPreferences = mContext.getSharedPreferences(chatPreferences, Activity.MODE_PRIVATE);
            if (mSharedPreferences != null)
                mSharedPreferences.edit().remove(chatId).commit();
        }
    }
    public static void removeChatName(Context mContext) {
        String chatId = "ChatProductName";
        if (mContext != null) {
            SharedPreferences mSharedPreferences = mContext.getSharedPreferences(chatPreferences, Activity.MODE_PRIVATE);
            if (mSharedPreferences != null)
                mSharedPreferences.edit().remove(chatId).commit();
        }
    }
    public static void removeChatCurrency(Context mContext) {
        String chatId = "ChatProductCurrency";
        if (mContext != null) {
            SharedPreferences mSharedPreferences = mContext.getSharedPreferences(chatPreferences, Activity.MODE_PRIVATE);
            if (mSharedPreferences != null)
                mSharedPreferences.edit().remove(chatId).commit();
        }
    }
    public static void removeChatPrice(Context mContext) {
        String chatId = "ChatProductPrice";
        if (mContext != null) {
            SharedPreferences mSharedPreferences = mContext.getSharedPreferences(chatPreferences, Activity.MODE_PRIVATE);
            if (mSharedPreferences != null)
                mSharedPreferences.edit().remove(chatId).commit();
        }
    }

    public static void setProductName(Context context, String name) {
        String chat = "ProductName";
        android.content.SharedPreferences prefs = context.getSharedPreferences(salesPreferences, Activity.MODE_PRIVATE);
        android.content.SharedPreferences.Editor editor = prefs.edit();
        editor.putString(chat, name);
        editor.apply();
    }

    public static String getProductName(Context context) {
        String chat = "ProductName";
        android.content.SharedPreferences prefs = context.getSharedPreferences(salesPreferences, Activity.MODE_PRIVATE);
        return prefs.getString(chat, "");
    }
}
