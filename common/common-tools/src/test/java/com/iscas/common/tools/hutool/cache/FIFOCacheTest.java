package com.iscas.common.tools.hutool.cache;

import cn.hutool.cache.Cache;
import cn.hutool.cache.CacheUtil;
import cn.hutool.core.date.DateUnit;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

/**
 * 先进先出缓存测试
 *
 * @author zhuquanwen
 * @vesion 1.0
 * @date 2018/10/8 14:53
 * @since jdk1.8
 */
@RunWith(JUnit4.class)
public class FIFOCacheTest {
    @Test
    public void test1(){
        Cache<String,String> fifoCache = CacheUtil.newFIFOCache(3);
        fifoCache.put("key1", "value1", DateUnit.SECOND.getMillis() * 3);
        fifoCache.put("key2", "value2", DateUnit.SECOND.getMillis() * 3);
        fifoCache.put("key3", "value3", DateUnit.SECOND.getMillis() * 3);
        fifoCache.put("key4", "value4", DateUnit.SECOND.getMillis() * 3);

        //由于缓存容量只有3，当加入第四个元素的时候，根据FIFO规则，最先放入的对象将被移除
        String value1 = fifoCache.get("key1");
        String value4 = fifoCache.get("key4");
        Assert.assertTrue(null == value1);
        Assert.assertEquals("value4", value4);
    }
}
