package com.blueshak.app.blueshak.util;

import android.util.Log;

/**
 * Created by lsingh013 on 25/11/17.
 */

public class BlueShakLog {

    private static String mBlueShakLog = "BlueShak_";

    public static void logDebug(String tag, String log_value) {
        if (BlueShakLog.MODE.DEBUG)
            Log.d(mBlueShakLog, tag + ">> " + log_value);
    }

    public static void logError(String tag, String log_value) {
        if (BlueShakLog.MODE.DEBUG) {
            Log.e(mBlueShakLog, tag + ">> " + log_value);
        }

    }

    public static void logError(String tag, String log_value, Throwable throwable) {
        if (BlueShakLog.MODE.DEBUG) {
            Log.e(mBlueShakLog, tag + ">> " + log_value, throwable);
        }
    }

    public static void logDebug(String tag, Exception e) {
        if (BlueShakLog.MODE.DEBUG) {
            String logStr = "Exception -> " + e.getClass().getSimpleName();
            String localMgs = e.getLocalizedMessage();
            if (localMgs != null) {
                logStr += " \n >> localMsg --> " + localMgs;
            }
            String msg = e.getMessage();
            if (msg != null) {
                logStr += "\n msg  --> " + msg;
            }
            Log.d(mBlueShakLog, tag + ">> " + logStr);
        }
    }

    public static class MODE {
        public static boolean DEBUG = true; // app is in debug mode
    }
}
