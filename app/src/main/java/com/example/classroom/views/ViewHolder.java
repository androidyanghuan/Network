package com.example.classroom.views;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.text.util.Linkify;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.widget.Checkable;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

/**
 * Created by on 2018/4/4 16:34.
 * Created by author YangHuan
 * e-mail:435025168@qq.com
 */

public class ViewHolder extends RecyclerView.ViewHolder {

    private SparseArray<View> mViews;
    private View mConvertView;
    private Context mContext;

    public ViewHolder(Context context, View itemView) {
        super(itemView);
        mConvertView = itemView;
        mContext = context;
        mViews = new SparseArray<>();
    }

    public static ViewHolder createViewHolder(Context context, View itemView){
        return new ViewHolder(context,itemView);
    }

    public static ViewHolder createViewHolder(Context context, ViewGroup parent, int layoutId){
        View itemView = LayoutInflater.from(context).inflate(layoutId, parent, false);
        return new ViewHolder(context,itemView);
    }

    /**
     * 通过viewId获取控件
     * @param viewId
     * @param <T>
     * @return
     */
    public <T extends View> T getView(int viewId){
        View view = mViews.get(viewId);
        if (view == null) {
            view = mConvertView.findViewById(viewId);
            mViews.put(viewId,view);
        }
        return (T) view;
    }

    public View getConvertView(){
        return mConvertView;
    }

    /**************以下为辅助方法******************/

    /**
     * 设置TextView的值
     * @param viewId
     * @param text
     * @return
     */
    public ViewHolder setText(int viewId, String text){
        TextView tv = getView(viewId);
        tv.setText(text);
        return this;
    }

    /**
     * 设置ImageView的资源id
     * @param viewId
     * @param resId
     * @return
     */
    public ViewHolder setImageResource(int viewId, int resId){
        ImageView iv = getView(viewId);
        iv.setImageResource(resId);
        return this;
    }

    /** 设置ImageView的Bitmap */
    public ViewHolder setImageBitmap(int viewId, Bitmap bitmap){
        ImageView iv = getView(viewId);
        iv.setImageBitmap(bitmap);
        return this;
    }

    /** 设置ImageView的Drawable */
    public ViewHolder setImageDrawable(int viewId, Drawable drawable){
        ImageView iv = getView(viewId);
        iv.setImageDrawable(drawable);
        return this;
    }

    /** 设置View的背景颜色 */
    public ViewHolder setBackgroundColor(int viewId, int color){
        View view = getView(viewId);
        view.setBackgroundColor(color);
        return this;
    }

    /** 设置View的背景资源id */
    public ViewHolder setBackgroundResource(int viewId, int resourceId){
        View view = getView(viewId);
        view.setBackgroundResource(resourceId);
        return this;
    }

    /** 设置TextView的颜色 */
    public ViewHolder setTextColor(int viewId, int textColor){
        TextView view = getView(viewId);
        view.setTextColor(textColor);
        return this;
    }

    /** 根据资源id设置TextView的颜色 */
    public ViewHolder setTextColorRes(int viewId, int textColorRes){
        TextView view = getView(viewId);
        view.setTextColor(mContext.getResources().getColor(textColorRes));
        return this;
    }

    /** 设置View的Alpha值 */
    @SuppressLint("NewApi")
    public ViewHolder setAlpha(int viewId, float value) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB)
            getView(viewId).setAlpha(value);
        else {
            // Pre-honeycomb hack to set Alpha value
            AlphaAnimation alpha = new AlphaAnimation(value, value);
            alpha.setDuration(0);
            alpha.setFillAfter(true);
            getView(viewId).startAnimation(alpha);
        }
        return this;
    }

    /** 设置View是否可见 */
    public ViewHolder setVisible(int viewId, boolean visible){
        View view = getView(viewId);
        view.setVisibility(visible ? View.VISIBLE : View.GONE);
        return this;
    }

    /** 设置TextView的连接类型为全部类型 */
    public ViewHolder linkify(int viewId) {
        TextView view = getView(viewId);
        Linkify.addLinks(view, Linkify.ALL);
        return this;
    }

    /** 设置TextView的Typeface */
    public ViewHolder setTypeface(Typeface typeface, int... viewIds){
        for (int viewId : viewIds){
            TextView view = getView(viewId);
            view.setTypeface(typeface);
            view.setPaintFlags(view.getPaintFlags() | Paint.SUBPIXEL_TEXT_FLAG);
        }
        return this;
    }

    /** 设置ProgressBar的progress */
    public ViewHolder setProgress(int viewId, int progress){
        ProgressBar view = getView(viewId);
        view.setProgress(progress);
        return this;
    }

    /** 设置ProgressBar的progress和最大值 */
    public ViewHolder setProgress(int viewId, int progress, int max){
        ProgressBar view = getView(viewId);
        view.setMax(max);
        view.setProgress(progress);
        return this;
    }

    /** 设置ProgressBar的最大值 */
    public ViewHolder setMax(int viewId, int max){
        ProgressBar view = getView(viewId);
        view.setMax(max);
        return this;
    }

    /** 设置RatingBar的等级 */
    public ViewHolder setRating(int viewId, float rating){
        RatingBar view = getView(viewId);
        view.setRating(rating);
        return this;
    }

    /** 设置RatingBar的等级和最高等级 */
    public ViewHolder setRating(int viewId, float rating, int max){
        RatingBar view = getView(viewId);
        view.setMax(max);
        view.setRating(rating);
        return this;
    }

    /** 设置View的tag */
    public ViewHolder setTag(int viewId, Object tag){
        View view = getView(viewId);
        view.setTag(tag);
        return this;
    }

    /** 根据key设置View的tag */
    public ViewHolder setTag(int viewId, int key, Object tag){
        View view = getView(viewId);
        view.setTag(key, tag);
        return this;
    }

    /** 设置复选框的状态(选中状态) */
    public ViewHolder setChecked(int viewId, boolean checked){
        Checkable view = getView(viewId);
        view.setChecked(checked);
        return this;
    }

    /**
     * 设置View的点击监听事件
     * @param viewId
     * @param listener
     * @return
     */
    public ViewHolder setOnClickListener(int viewId, View.OnClickListener listener){
        View view = getView(viewId);
        view.setOnClickListener(listener);
        return this;
    }

    /**
     * 设置View的触摸事件
     * @param viewId
     * @param listener
     * @return
     */
    public ViewHolder setOnTouchListener(int viewId, View.OnTouchListener listener){
        View view = getView(viewId);
        view.setOnTouchListener(listener);
        return this;
    }

    /**
     * 设置View的长按事件
     * @param viewId
     * @param listener
     * @return
     */
    public ViewHolder setOnLongClickListener(int viewId, View.OnLongClickListener listener){
        View view = getView(viewId);
        view.setOnLongClickListener(listener);
        return this;
    }

}
