package com.iscas.base.biz.util;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @program: stc-pub
 * @description: 日期时间工具类
 * @author: LiangJian
 * @create: 2018-09-03 13:41
 **/
public class DateTimeUtils {
    public static String getDateStr(Date date){
        if(null == date){
            return null;
        }
        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(date);
    }

    @SuppressWarnings("AlibabaUndefineMagicConstant")
    public static String getDateStr(String date){
        if(date == null || date.isEmpty()){
            return null;
        }
        if(date.contains(" ")) {
            return date.split(" ")[0];
        }
        return date;
    }

    @SuppressWarnings("AlibabaUndefineMagicConstant")
    public static String removeDateDot(String date){
        if(date == null || date.isEmpty()){
            return null;
        }
        if(date.contains(".")) {
            return date.split("\\.")[0];
        }
        return date;
    }

    public static Date str2Date(String dateStr){
        if(dateStr == null || dateStr.isEmpty()){
            return null;
        }
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = null;
        try {
             date = df.parse(dateStr);
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return date;
    }
}
