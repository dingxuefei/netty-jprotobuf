package com.iscas.common.tools.hutool.cache;

import cn.hutool.cache.file.LFUFileCache;
import cn.hutool.cache.file.LRUFileCache;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

/**
 * 文件缓存测试
 *
 * @author zhuquanwen
 * @vesion 1.0
 * @date 2018/10/8 15:12
 * @since jdk1.8
 */
@RunWith(JUnit4.class)
public class FileCacheTest {
    /**
     * LFU规则的文件缓存
     * */
    @Test
    @Ignore
    public void test(){
        LFUFileCache lfuFileCache = new LFUFileCache(1000,500,2000);
        byte[] fileBytes = lfuFileCache.getFileBytes("E:/aaa.xlsx");
        System.out.println(fileBytes.length);
    }

    /**
     * LRU规则的文件缓存
     * */
    @Test
    @Ignore
    public void test2(){
        LRUFileCache lruFileCache = new LRUFileCache(1000,500,2000);
        byte[] fileBytes = lruFileCache.getFileBytes("E:/aaa.xlsx");
        System.out.println(fileBytes.length);
    }
}
