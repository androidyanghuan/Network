package com.example.classroom.entitys;

import lombok.Data;

/**
 * Version: v1.0
 * Author: YangHuan
 * Date: 2020/8/12
 * Description 版本信息
 */
@Data
public class UpdateInfo {
    private int id;
    private String versionCode;
    private String versionName;
    private String description;
    private String downloadUrl;
    private String createTime;
}
