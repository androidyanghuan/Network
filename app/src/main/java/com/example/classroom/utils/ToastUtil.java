package com.example.classroom.utils;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.classroom.R;

/**
 * Version: v1.0
 * Author: YangHuan
 * Date: 2020/8/10
 * Description 自定义Toast工具类
 */
public class ToastUtil {
    private static Toast toast = null;

    private ToastUtil() {
    }

    public static void showToast(Context context, int resId) {
        showToast(context,context.getString(resId), Toast.LENGTH_LONG);
    }

    public static void showToast(Context context, String msg) {
        if (EmptyUtil.isEmpty(msg)) return;
        showToast(context,msg,Toast.LENGTH_LONG);
    }

    public static void showToast(Context context, int resId, int duration){
        showToast(context, context.getString(resId),duration);
    }

    public static void showToast(Context context, String msg, int duration) {
        if (toast != null) {
            toast.cancel();
            toast = null;
        }
        toast = Toast.makeText(context, msg, duration);
        toast.show();

    }

    public static void networkErrorShowMessage(Context context, boolean isNetworkException, String errorMessage) {
        if (isNetworkException) {
            ToastUtil.showToast(context, R.string.network_error);
        } else {
            if (MyLog.isLog) {
                ToastUtil.showToast(context, errorMessage);
            }
        }
    }

    private static Toast mToast = null;
    public static void showCenterToast(Context context, int resId, int color) {
        View view = LayoutInflater.from(context).inflate(R.layout.toast_layout, null);
        TextView message = view.findViewById(R.id.toast_msg);
        if (mToast == null) {
            mToast = new Toast(context);
        }
        message.setText(resId);
        if (color != -1 ) {
            message.setTextColor(color);
        }
        mToast.setView(view);
        mToast.setGravity(Gravity.CENTER, 0, 0);
        mToast.show();
    }
    public static void showCenterToast(Context context, String msg, int color) {
        View view = LayoutInflater.from(context).inflate(R.layout.toast_layout, null);
        TextView message = view.findViewById(R.id.toast_msg);
        if (mToast == null) {
            mToast = new Toast(context);
        }
        message.setText(msg);
        if (color != -1) {
            message.setTextColor(color);
        }
        mToast.setView(view);
        mToast.setGravity(Gravity.CENTER, 0, 0);
        mToast.setDuration(Toast.LENGTH_LONG);
        mToast.show();
    }

}
