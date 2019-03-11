package com.iscas.common.tools.security;

import org.junit.Assert;
import org.junit.Test;

import java.security.NoSuchAlgorithmException;

/**
 * MD5工具类测试
 * @author zhuquanwen
 **/

public class MD5UtilsTests {
    /**
     * 普通加密
     * */
    @Test
    public void MD51() throws NoSuchAlgorithmException {
        String sec = MD5Utils.md5("apply");
        Assert.assertNotNull(sec);
    }
    /**
     * 加盐加密
     * */
    @Test
    public void MD52() throws NoSuchAlgorithmException {
        String sec = MD5Utils.saltMD5("admin");
        System.out.println(sec);
        Assert.assertNotNull(sec);
    }
    /**
     * 加盐加密的校验
     * */
    @Test
    public void MD53() throws NoSuchAlgorithmException {
        boolean sec = MD5Utils.saltVerify("admin"
                ,"09e480814288b5245d39228681f23f311208e9a57170a990");
        Assert.assertEquals(true, sec);
    }
}
