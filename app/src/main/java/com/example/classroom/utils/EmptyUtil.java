package com.example.classroom.utils;

import java.util.List;

/**
 * Version: v1.0
 * Author: YangHuan
 * Date: 2020/8/10
 * Description 判空工具类
 */
public class EmptyUtil {

    private EmptyUtil() {
    }

    public static boolean isEmpty(String msg) {
        return msg == null || msg.length() == 0 || msg.equals("null");
    }

    public static boolean equals(CharSequence a, CharSequence b) {
        if (a == b) {
            return true;
        }
        if (a != null && b != null && a.length() == b.length()) {
            if (a instanceof String && b instanceof String) {
                return a.equals(b);
            } else {
                for (int i = 0; i < a.length(); i++) {
                    if (a.charAt(i) != b.charAt(i)) return false;
                }
                return true;
            }
        }
        return false;
    }

}
