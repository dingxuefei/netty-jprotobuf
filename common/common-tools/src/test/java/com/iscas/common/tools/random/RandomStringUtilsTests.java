package com.iscas.common.tools.random;

import org.junit.Assert;
import org.junit.Test;

/**
 * 随机字符串工具类
 * @author zhuquanwen
 * @date 2018/7/13 18:01
 **/
public class RandomStringUtilsTests {
    public RandomStringUtilsTests(){}

    /**
    * 测试获取随机数字字母字符串
    * */
    @Test
    public void  randomStr(){
        int length = 16;
        String result = RandomStringUtils.randomStr(length);
        Assert.assertNotNull(result);
        Assert.assertEquals(16, result.length());
    }
}
