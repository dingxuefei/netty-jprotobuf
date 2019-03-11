package com.iscas.base.biz.util;

import org.junit.Assert;
import org.junit.Test;

import java.io.UnsupportedEncodingException;

/**
 * JWT测试
 *
 * @author zhuquanwen
 * @vesion 1.0
 * @date 2018/7/17 19:18
 * @since jdk1.8
 */
public class JWTUtilsTests {
    @Test
    public void test() throws UnsupportedEncodingException {
        String name = JWTUtils.createToken("zqw11", 11111111);
        System.out.println(name);
        Assert.assertNotNull(name);
    }
}
