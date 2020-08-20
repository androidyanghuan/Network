package com.example.classroom;

import java.math.BigDecimal;

/**
 * Version: v1.0
 * Author: YangHuan
 * Date: 2020/8/7
 * Description
 */
public class Test {
    public static void main(String[] args) {
        BigDecimal bg = new BigDecimal(0.1f);
        BigDecimal bg2 = new BigDecimal("0.1");
        BigDecimal bg3 = BigDecimal.valueOf(0.1f);
        BigDecimal bg4 = BigDecimal.valueOf(0.1);
        System.out.println("bg:" + bg + ",bg2:" + bg2 + ",bg3:" + bg3 + ",bg4:" + bg4);
    }
}
