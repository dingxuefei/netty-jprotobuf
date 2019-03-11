package com.iscas.common.tools.date;

import jdk.nashorn.internal.runtime.linker.Bootstrap;
import org.junit.Assert;
import org.junit.Test;

import java.text.ParseException;
import java.util.Date;

/**
 *<p>日期增强工具类测试</p>
 * @author zhuquanwen
 * @date 2018/7/16
 * @modified
 **/
public class DateRaiseUtilsTests {

    /**
    * 获取年份
    * */
    @Test
    public void getYear(){
        Date date = new Date();
        Integer x = DateRaiseUtils.getYear(date);
        System.out.println(x);
        Assert.assertNotNull(x);
    }

    /**
     * 获取月份
     * */
    @Test
    public void getMonth(){
        Date date = new Date();
        Integer month = DateRaiseUtils.getMonth(date);
        System.out.println(month);
        Assert.assertNotNull(month);
    }

    /**
     * 获取天
     * */
    @Test
    public void getDay(){
        Date date = new Date();
        Integer x = DateRaiseUtils.getDay(date);
        System.out.println(x);
        Assert.assertNotNull(x);
    }
    /**
    * 获取小时
    * */
    @Test
    public void getHour(){
        Date date = new Date();
        Integer x = DateRaiseUtils.getHour(date);
        System.out.println(x);
        Assert.assertNotNull(x);
    }
    /**
    * 获取分钟
    * */
    @Test
    public void getMinute(){
        Date date = new Date();
        Integer x = DateRaiseUtils.getMinute(date);
        System.out.println(x);
        Assert.assertNotNull(x);
    }
    /*
    * 获取秒
    * */
    @Test
    public void getSecond(){
        Date date = new Date();
        Integer x = DateRaiseUtils.getSecond(date);
        System.out.println(x);
        Assert.assertNotNull(x);
    }

    /**
     * 判断是否这个月份为季度末
    * */
    @Test
    public void isSeason(){
        Date date = new Date();
        Boolean flag = DateRaiseUtils.isSeason(date);
        System.out.println(flag);
    }
    /**
     * 从现在开始算偏移一定毫秒数后的时间
    * */
    @Test
    public void afterOffsetDate(){
        long offset = -1 * 24 * 3600 *1000;
        Date date = DateRaiseUtils.afterOffsetDate(offset);
        Assert.assertNotNull(date);
    }
    /**
    * 从现在开始算偏移一定毫秒数后的时间
    * */
    @Test
    public void afterOffsetDate2() throws ParseException {
        long offset = 1 * 24 * 3600 *1000;
        Date date = DateSafeUtils.parse("2018-11-11 12:12:12","yyyy-MM-dd HH:mm:ss");
        Date datex = DateRaiseUtils.afterOffsetDate(date, offset);
        Assert.assertNotNull(datex);
    }
}
