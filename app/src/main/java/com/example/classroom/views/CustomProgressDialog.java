package com.example.classroom.views;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.ContextWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.classroom.R;


/**
 * Version: v1.0
 * Author: YangHuan
 * Date: 2020/8/12
 * Description  自定义等待对话框
 */

public class CustomProgressDialog {
    private static Dialog mDialog;

    /**
     * 创建一个默认的等待对话框
     */
    public static void createDefaultLoadingDialog(Context context, boolean cancelable){
        View view = LayoutInflater.from(context).inflate(R.layout.custom_progress_dialog,null);
        ImageView loadingView = view.findViewById(R.id.progress_view);
        // 加载动画
        Animation animation = AnimationUtils.loadAnimation(context,R.anim.loading_animation);
        loadingView.startAnimation(animation);

        if (mDialog == null) {
            mDialog = new Dialog(context, R.style.LoadingDialog);
        }
        mDialog.setContentView(view);
        mDialog.setCancelable(cancelable);
        mDialog.setCanceledOnTouchOutside(cancelable);
        mDialog.setOnCancelListener(dialog -> mDialog = null);
        //在dialog show之前判断一下
        Activity activity = getActivity(mDialog.getContext());
        if (activity != null && !activity.isFinishing()) {
            mDialog.show();
        }
    }

    /**
     * 创建一个指定风格的等待对话框
     */
    public static void createCustomLoadingDialog(Context context, boolean cancelable, String message, int theme){
        View view = LayoutInflater.from(context).inflate(R.layout.custom_progress_dialog,null);
        ImageView loadingView = view.findViewById(R.id.progress_view);
        TextView loadingDescribe = view.findViewById(R.id.progress_message);
        // 加载动画
        Animation animation = AnimationUtils.loadAnimation(context,R.anim.loading_animation);
        loadingView.startAnimation(animation);
        loadingDescribe.setText(message);
        if (mDialog == null) {
            mDialog = new Dialog(context, theme);
        }
        mDialog.setContentView(view);
        mDialog.setCancelable(cancelable);
        mDialog.setCanceledOnTouchOutside(cancelable);
        mDialog.setOnCancelListener(dialog -> mDialog = null);
        //在dialog show之前判断一下
        Activity activity = getActivity(mDialog.getContext());
        if (activity != null && !activity.isFinishing()) {
            mDialog.show();
        }
    }

    private static Activity getActivity(Context context) {
        if (!(context instanceof Activity) && context instanceof ContextWrapper) {
            context = ((ContextWrapper) context).getBaseContext();
        }
        if (context instanceof Activity) {
            return (Activity) context;
        }
        return null;
    }

    public static void cancel(){
        if (mDialog != null && mDialog.isShowing()) {
            mDialog.cancel();
            mDialog = null;
        }
    }

}
