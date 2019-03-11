package com.iscas.common.tools.hutool.bloomfilter;

import cn.hutool.bloomfilter.BitMapBloomFilter;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

/**
 * 布隆过滤器测试
 *
 * @author zhuquanwen
 * @vesion 1.0
 * @date 2018/10/8 14:24
 * @since jdk1.8
 */
@RunWith(JUnit4.class)
public class BitMapBloomFilterTest {
    @Test
    public void filterTest() {
        BitMapBloomFilter filter = new BitMapBloomFilter(10);
        filter.add("123");
        filter.add("abc");
        filter.add("ddd");


        Assert.assertTrue(filter.contains("abc"));
        Assert.assertTrue(filter.contains("ddd"));
        Assert.assertTrue(filter.contains("123"));
    }
}
