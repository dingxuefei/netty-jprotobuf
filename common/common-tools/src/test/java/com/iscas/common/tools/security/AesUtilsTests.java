package com.iscas.common.tools.security;

import org.junit.Assert;
import org.junit.Test;

/**
 * AES加解密测试类
 * @author zhuquanwen
 **/
public class AesUtilsTests {

    /**
     * 测试AES 默认加密
     * */
    @Test
    public void aesEncrpty() throws Exception {
        String sec = AesUtils.aesEncrypt("admin");
        Assert.assertEquals("VftCp3FzK7UeUThCOI+3DQ==", sec);
    }

    /**
     *
     * 测试AES默认解密
     * */
    @Test
    public void aesDecrpty() throws Exception {
        String ori = AesUtils.aesDecrypt("VftCp3FzK7UeUThCOI+3DQ==");
        Assert.assertEquals("admin", ori);
    }

    /**
     * 测试AES 带KEY加密
    * */
    @Test
    public void aesEncrptyWithKey() throws Exception {
        String key = "6x9o67h5BO205Cfv";
        String sec = AesUtils.aesEncrypt("admin", key);
        Assert.assertEquals("q2F6LtquPxijSre3os07Dg==", sec);
    }

    /**
     * 测试AES 带KEY解密
     * */
    @Test
    public void aesDecrptyWithKey() throws Exception {
        String key = "6x9o67h5BO205Cfv";
        String ori = AesUtils.aesDecrypt("q2F6LtquPxijSre3os07Dg==", key);
        Assert.assertEquals("admin", ori);
    }

}
