package com.iscas.distribution.server;

import io.netty.channel.Channel;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * //TODO
 *
 * @author zhuquanwen
 * @vesion 1.0
 * @date 2018/7/19 16:11
 * @since jdk1.8
 */
public class ChannelRepository {
    private final static Map<String, Channel> channelCache = new ConcurrentHashMap<String, Channel>();

    public void put(String key, Channel value) {
        channelCache.put(key, value);
    }

    public Channel get(String key) {
        return channelCache.get(key);
    }

    public void remove(String key) {
        channelCache.remove(key);
    }

    public int size() {
        return channelCache.size();
    }

    public Set<String> keys(){
        return channelCache.keySet();
    }
}
