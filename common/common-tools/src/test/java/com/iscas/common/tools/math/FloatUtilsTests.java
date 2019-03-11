package com.iscas.common.tools.math;

import org.junit.Assert;
import org.junit.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Random;

/**
 * float操作工具类测试
 * @author zhuquanwen
 * @date: 2018/7/13 18:16
 **/
public class FloatUtilsTests {
    //将float数字转为字节数组
    @Test
    public void converFloatToBytesTest(){
        float f = 5.878f;
        byte[] bytes = FloatUtils.float2byte(f);
        String s = Arrays.toString(bytes);
        Assert.assertEquals("[-109, 24, -68, 64]", s);
    }

    //将字节数组转为float
    @Test
    public void converBytesToFloatTest(){
        byte[] bytes = new byte[]{12,11,-109, 24, -68, 64,55};
        float f = FloatUtils.byte2float(bytes, 2);
        Assert.assertEquals("5.878", String.valueOf(f));
    }
    //将float的二维数组转为一个file
    @Test
    public void convertToFile() throws IOException {
        File file = new File("d:/floatUtils_test.txt");
        Random random = new Random();
        float[][] data = new float[1024][1024];
        for (int i = 0; i < 1024; i++) {
            for (int j = 0; j < 1024 ; j++) {
                data[i][j] = random.nextFloat();
            }
        }
        FloatUtils.convertFData2File(data, file);
    }
    //将文件转为float二维数组
    @Test
    public void convertToArray() throws IOException {
        File file = new File("d:/floatUtils_test.txt");
        float[][] data = FloatUtils.convertFile2FArray(file,1024,1024);
        Assert.assertNotNull(data);
    }

    //将输入流转为float二维数组
    @Test
    public void convertToArray1() throws IOException {
        File file = new File("d:/floatUtils_test.txt");
        InputStream is = new FileInputStream(file);
        float[][] data = FloatUtils.convertIS2FArray(is,1024,1024);
        Assert.assertNotNull(data);
    }
}
