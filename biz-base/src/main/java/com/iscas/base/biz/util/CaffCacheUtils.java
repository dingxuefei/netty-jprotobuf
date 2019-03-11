package com.iscas.base.biz.util;

import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.benmanes.caffeine.cache.LoadingCache;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;

/**
 * 咖啡因缓存工具类
 *
 * @author: zhuquanwen
 * @date: 2018/3/21 15:09
 *
 **/
public class CaffCacheUtils {
    private CaffCacheUtils() { }
    private static Logger log = LoggerFactory.getLogger(CaffCacheUtils.class);
    //初始化
    private static int initialCapcity = 100;
    //最大容量
    private static int maximumSize = 1000;
    //目前没查到资料怎样使缓存用不失效，设置尽量一个大的值15天
    private static int expireAfterWrite = 15 * 24 * 60;
    private static volatile LoadingCache<String,Object> localCache = null;
    private static void initLocalCache() {
        if (localCache == null) {
            synchronized (CaffCacheUtils.class) {
                localCache = Caffeine.newBuilder()
                        .initialCapacity(initialCapcity)
                        .maximumSize(maximumSize)
                        .expireAfterWrite(expireAfterWrite, TimeUnit.MINUTES)
                        .build(s -> {
                            return null;
                        });
            }
        }
    }
    public static void set(String key, Object value) {
        initLocalCache();
        localCache.put(key, value);
    }

    public static void cleanup() {
        initLocalCache();
        localCache.invalidateAll();
    }

    @SuppressWarnings("AlibabaUndefineMagicConstant")
    public static Object get(String key) {
        initLocalCache();
        Object value = null;
        try {
            value = localCache.get(key);
            if ("null".equals(value)) {
                return null;
            }
            return value;
        } catch (Exception e) {
            log.error("localCache get error", e);
        }
        return null;
    }
    public static void remove(String key) {
        initLocalCache();
        if (key != null) {
            localCache.invalidate(key);
        }
    }
}
