package com.example.classroom.entitys;

import lombok.Data;

/**
 * Version: v1.0
 * Author: YangHuan
 * Date: 2020/8/12
 * Description 实体基类
 */
@Data
public class BaseEntity<T> {
    private int state;
    private String code;
    private T data;
}
