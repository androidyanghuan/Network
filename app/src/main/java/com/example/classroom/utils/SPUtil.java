package com.example.classroom.utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Version: v1.0
 * Author: YangHuan
 * Date: 2020/8/18
 * Description
 */
public class SPUtil {

    private static SPUtil instance;
    private static final String SP_NAME = "com.example.classroom.information_preference";
    private SharedPreferences sp;

    private SPUtil(Context context) {
        sp = context.getApplicationContext().getSharedPreferences(SP_NAME,Context.MODE_PRIVATE);
    }

    public static SPUtil getInstance(Context context) {
        if (instance == null) {
            synchronized (SPUtil.class) {
                if (instance == null) {
                    instance = new SPUtil(context);
                }
            }
        }
        return instance;
    }


    public void saveUserPhoto(String uri) {
        SharedPreferences.Editor edit = sp.edit();
        edit.putString("userIcon",uri);
        edit.apply();
    }

    public String getUserPhoto() {
        return sp.getString("userIcon","");
    }

}
