package com.iscas.biz.config;

import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * //TODO
 *
 * @author zhuquanwen
 * @vesion 1.0
 * @date 2019/3/4 8:25
 * @since jdk1.8
 */
public class StaticData {

    /**
     * 接收数据的队列
     * */
    public static final Map<String, BlockingQueue<Object>> QUEUE_MAP = new ConcurrentHashMap<>();

    /*线程池*/
    public static final ExecutorService THREAD_POOL = Executors.newCachedThreadPool();

}
