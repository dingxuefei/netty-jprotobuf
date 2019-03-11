package com.iscas.biz.config.distribute;

import com.iscas.biz.netty.DistributeClient;
import com.iscas.biz.netty.handler.LogicClientHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;

/**
 * //TODO
 *
 * @author zhuquanwen
 * @vesion 1.0
 * @date 2018/7/19 16:42
 * @since jdk1.8
 */
public class DistributeClientConfig {
    @Autowired
    private LogicClientHandler logicClientHandler;
    @Autowired
    private DistributeClientProps distributeClientProps;
    @Bean
    public DistributeClient distributeClient () throws Exception {
        final DistributeClient client = new DistributeClient(logicClientHandler, distributeClientProps);
        //启动netty客户端
        Runnable runnable = () -> {
            try {
                client.run();
            } catch (Exception e) {
                e.printStackTrace();
            }
        };
        new Thread(runnable).start();

        return client;
    }
}
