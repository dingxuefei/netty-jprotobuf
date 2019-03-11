package com.iscas.common.tools.security;


import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;

/**
 *
 * <b>这里只测试了部分函数，还有些函数的使用见{@link com.iscas.common.tools.security.Base64Utils}<b/>
 *
 * @author: zhuquanwen
 * @date: 2018/7/13 16:37
 **/
public class Base64JavaUtilsTests {
    /**
    * 判断是不是base64编码
    * */
    @Test
    public void isBase64_1() throws IOException {
        //字符串
        String str1 = "111";
        boolean flag1 = Base64Utils.isBase64(str1);
        String str2 = "d3dlZ3dlZ3dlZw==";
        boolean flag2 = Base64Utils.isBase64(str2);
        Assert.assertEquals(flag1,false);
        Assert.assertEquals(flag2,true);

        //字符串带编码格式
        String str11 = "111";
        boolean flag11 = Base64Utils.isBase64(str11, "utf-8");
        String str21 = "d3dlZ3dlZ3dlZw==";
        boolean flag21 = Base64Utils.isBase64(str21,"utf-8");
        Assert.assertEquals(flag11,false);
        Assert.assertEquals(flag21,true);

    }

    /**
     * 字符串编码
     * */
    @Test
    public void encode(){
        String encodeStr = "wwegwegweg";
        String result = Base64Utils.encode(encodeStr);
        Assert.assertEquals("d3dlZ3dlZ3dlZw==", result);
    }

    /**
     * 字符串解码
     * */
    @Test
    public void decode(){
        String decodeStr = "d3dlZ3dlZ3dlZw==";
        String result = Base64Utils.decode(decodeStr);
        Assert.assertEquals("wwegwegweg", result);
    }

}
