package com.example.classroom.entitys;

import java.util.List;

import lombok.Data;

/**
 * Version: v1.0
 * Author: YangHuan
 * Date: 2020/8/13
 * Description 分类实体类
 */

@Data
public class ClassifyInfo {

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

}
