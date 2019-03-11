package com.iscas.biz.service.general;

import com.iscas.base.biz.exception.BaseException;
import com.iscas.biz.config.StaticData;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * //TODO
 *
 * @author zhuquanwen
 * @vesion 1.0
 * @date 2019/3/1 17:30
 * @since jdk1.8
 */
@Service
@Slf4j
public class MessageService {
    /**
     * key是这此推演的一个标识，需要与{@link com.iscas.biz.netty.handler.LogicClientHandler #channelRead0}中的队列的key对应
     * */
    public synchronized void init(String key)  throws  BaseException {

        if (key == null) {
            throw new BaseException("XXX不能为空");
        }
        //如果正在演习，停掉
        StaticData.QUEUE_MAP.remove(key);
        //TODO 停掉其他的相关初始化信息

        BlockingQueue<Object> messageBaseBlockingQueue = new LinkedBlockingQueue<>();
        StaticData.QUEUE_MAP.put(key, messageBaseBlockingQueue);
        StaticData.THREAD_POOL.submit(() -> {
            try {
                //TODO 在这里进行业务处理
            } catch (Exception e) {
                log.error(e.getMessage(), e);
                e.printStackTrace();
            }
        });

    }
}
