package com.iscas.base.biz.util;

import cn.hutool.cache.Cache;
import cn.hutool.cache.CacheUtil;
import cn.hutool.core.date.DateUnit;

import java.util.UUID;

/**
 * //TODO
 *
 * @author zhuquanwen
 * @vesion 1.0
 * @date 2018/11/9 9:25
 * @since jdk1.8
 */
public class LoginCacheUtils {
    private static Cache<String,String> fifoCache = CacheUtil.newFIFOCache(1000);

    public static String put(String secretKey) {
        UUID uuid = UUID.randomUUID();
        fifoCache.put(uuid.toString(), secretKey, DateUnit.SECOND.getMillis() * 600);
        return uuid.toString();
    }
    public static String get(String uuid) {
        return fifoCache.get(uuid);
    }

    public static void remove(String uuid) {
        fifoCache.remove(uuid);
    }


}

