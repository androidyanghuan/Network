package com.example.classroom.entitys;

import android.content.Context;
import android.content.Intent;
import android.view.View;

import com.example.classroom.activitys.BroswerActivity;

import java.util.List;

import lombok.Data;

/**
 * Version: v1.0
 * Author: YangHuan
 * Date: 2020/8/18
 * Description 本周热门
 */
@Data
public class HotInfo {
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

    public void onItemClick(View view) {
        Context context = view.getContext();
        Intent intent = new Intent(context, BroswerActivity.class);
        intent.putExtra("title",title);
        intent.putExtra("url",url);
        context.startActivity(intent);
    }

}
