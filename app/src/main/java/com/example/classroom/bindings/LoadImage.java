package com.example.classroom.bindings;

import android.graphics.drawable.Drawable;
import android.widget.ImageView;

import com.example.classroom.R;
import com.example.classroom.app.App;
import com.example.classroom.utils.GlideUtil;

import androidx.core.content.res.ResourcesCompat;
import androidx.databinding.BindingAdapter;

/**
 * Version: v1.0
 * Author: YangHuan
 * Date: 2020/8/13
 * Description 加载图片
 */
public class LoadImage {

    @BindingAdapter({"android:src"})
    public static void loadImage(ImageView imageView, String url) {
        GlideUtil.getInstance().loadImage(imageView.getContext(),url,imageView);
    }

    public static Drawable switchIcon(String type) {
        int icon;
        switch (type) {
            case "iOS":
                icon = R.drawable.ic_vector_title_ios;
                break;

            case "Flutter":
                icon = R.drawable.ic_vector_title_front;
                break;

            default:
                icon = R.drawable.ic_vector_title_android;
        }
        return ResourcesCompat.getDrawable(App.me.getResources(),icon,null);
    }

}
