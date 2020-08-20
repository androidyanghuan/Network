package com.example.classroom.entitys;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.classroom.R;
import com.example.classroom.activitys.ImageBrowserActivity;
import com.example.classroom.utils.ToastUtil;

import java.io.Serializable;
import java.util.List;

import androidx.core.app.ActivityOptionsCompat;
import lombok.Data;

/**
 * Version: v1.0
 * Author: YangHuan
 * Date: 2020/8/17
 * Description 妹纸实体类
 */

@Data
public class GirlInfo {

    private String _id;
    private String author;
    private String category;
    private String createdAt;
    private String desc;
    private List<String> images;
    private int likeCounts;
    private String publishedAt;
    private int stars;
    private String title;
    private String type;
    private String url;
    private int views;

    public void onItemClick(View view){
        Context context = view.getContext();
        Bundle args = new Bundle();
        args.putString("url",url);
        args.putString("desc",desc);
        Intent intent = new Intent(context, ImageBrowserActivity.class);
        intent.putExtras(args);
        //  context.startActivity(intent, ActivityOptions.makeSceneTransitionAnimation((Activity) context).toBundle());
        context.startActivity(intent,
                ActivityOptionsCompat.makeSceneTransitionAnimation((Activity) context,
                        view.findViewById(R.id.fragment_welfare_pv),
                        context.getString(R.string.transition_movie_img)).toBundle());
    }

}
