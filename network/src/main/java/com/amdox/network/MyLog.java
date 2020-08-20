package com.amdox.network;

import android.util.Log;

public class MyLog {
    public static boolean isLog = false;

    private MyLog() {
    }

    public static void log(String tag, String log){
        if (isLog) {
            Log.e(tag, log);
        }
    }

    public static void log(String tag, String message, Throwable tr){
        if (isLog) {
            Log.e(tag, message, tr);
        }
    }
}
