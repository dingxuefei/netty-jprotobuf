package com.iscas.biz.service.general;

import com.iscas.biz.config.distribute.DistributeClientProps;
import com.iscas.biz.netty.DistributeClient;
import com.iscas.biz.netty.handler.LogicClientHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

/**
 * //TODO
 *
 * @author zhuquanwen
 * @vesion 1.0
 * @date 2019/3/1 17:29
 * @since jdk1.8
 */
@Service
public class CommunicationService {
    @Autowired
    private LogicClientHandler logicClientHandler;
    @Autowired
    private DistributeClientProps distributeClientProps;

    public void reconnect() {
        if (DistributeClient.clientChannel == null || !DistributeClient.clientChannel.isActive()) {
            //尝试建立连接
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
        }
    }
    public boolean pushDataToModel(Object data) {
        boolean flag = false;
        if (DistributeClient.clientChannel == null || !DistributeClient.clientChannel.isActive()) {
            reconnect();
            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        try {
            DistributeClient.clientChannel.writeAndFlush(data);
        } catch (Exception e) {
            e.printStackTrace();
            reconnect();
            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
            try {
                DistributeClient.clientChannel.writeAndFlush(data);
                flag = true;
            } catch (Exception ey) {
                ey.printStackTrace();
            }
        }
        return flag;
    }
}
