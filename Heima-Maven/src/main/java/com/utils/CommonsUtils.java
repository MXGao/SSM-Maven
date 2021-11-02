package com.utils;

import java.util.UUID;

/**
 * 生成uuid的工具类
 */
public class CommonsUtils {

    public static String getUUID(){
        return UUID.randomUUID().toString();
    }
}
