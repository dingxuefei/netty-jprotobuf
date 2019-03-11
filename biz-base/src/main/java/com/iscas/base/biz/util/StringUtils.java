package com.iscas.base.biz.util;

/**
 * //TODO
 *
 * @author zhuquanwen
 * @vesion 1.0
 * @date 2018/7/25 17:14
 * @since jdk1.8
 */
public class StringUtils {
    public static boolean isEmpty(Object str) {
        return (str == null || "".equals(str));
    }
    public static String upperFist(String str){
        if(str == null){
            throw new RuntimeException("str can not be null");
        }
        return str.substring(0,1).toUpperCase() + str.substring(1);
    }
    public static String lowerFist(String str){
        if(str == null){
            throw new RuntimeException("str can not be null");
        }
        return str.substring(0,1).toLowerCase() + str.substring(1);
    }
}
