package com.iscas.common.tools.file;

import org.junit.Assert;
import org.junit.Test;

import java.io.UnsupportedEncodingException;

/**
 * jar包路径测试类
 *
 * @author zhuquanwen
 * @vesion 1.0
 * @date 2018/7/13
 * @since jdk1.8
 */
public class JarPathUtilsTests {
    /**
     * 获得class文件存放的位置
     * */
    @Test
    public void test() throws UnsupportedEncodingException {
        String basePath = JarPathUtils.getJarPath(JarPathUtils.class);
        System.out.println(basePath);
        Assert.assertNotNull(basePath);
    }
}
