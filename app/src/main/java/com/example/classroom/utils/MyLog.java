package com.example.classroom.utils;

import android.util.Log;

import com.amdox.network.RetrofitUtil;

public class MyLog {
    public static final boolean isLog = false;

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
