package com.iscas.biz.netty.handler;

import com.iscas.biz.config.StaticData;
import com.iscas.biz.config.distribute.DistributeClientProps;
import com.iscas.protobuf.dto.MessageDTO;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * //TODO
 *
 * @author zhuquanwen
 * @vesion 1.0
 * @date 2018/7/19 16:37
 * @since jdk1.8
 */
@Slf4j
@Component
public class LogicClientHandler extends SimpleChannelInboundHandler<Object> {
    private AtomicInteger integer = new AtomicInteger(0);
    @Autowired
    private DistributeClientProps distributeClientProps;

    // 连接成功后，向server发送消息
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
//        Message.MessageBase.Builder authMsg = Message.MessageBase.newBuilder();
//        authMsg.setClientId(distributeClientProps.getClientId());
//        authMsg.setCmd(Command.CommandType.AUTH);
//        authMsg.setData("This is auth data");
//        ctx.writeAndFlush(authMsg.build());
        MessageDTO authMsg = new MessageDTO();
        authMsg.setClientId(distributeClientProps.getClientId());
        authMsg.setCmd(MessageDTO.CommandType.AUTH);
        authMsg.setData("This is auth data");
        ctx.writeAndFlush(authMsg);

    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        log.debug("连接断开 ");
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Object obj) throws Exception {
        try {
            if (obj instanceof MessageDTO) {
                MessageDTO msg = (MessageDTO) obj;
                if(msg.getCmd().equals(MessageDTO.CommandType.AUTH_BACK)){
                    String data = msg.getData();
                    if("success".equals(data)){
                        log.info("数据分发验证成功");
                    }else{
                        log.error("数据分发验证失败，连接即将断开");
                    }

                } else if(msg.getCmd().equals(MessageDTO.CommandType.PING)){
                    //接收到server发送的ping指令
                    log.debug("PING Netty:" + msg.getData());

                } else if(msg.getCmd().equals(MessageDTO.CommandType.PONG)){
                    //接收到server发送的pong指令
                    log.debug("Netty PONG:" + msg.getData());

                } else if(msg.getCmd().equals(MessageDTO.CommandType.PUSH_DATA)){
                    log.info("Netty xinxi:" + msg.getData());
                    TimeUnit.SECONDS.sleep(1);

                }else {
                    //将接收的数据根据不同的想定放入不同的队列
                    //TODO 将真实的ID与队列绑定
                    BlockingQueue<Object> blockingQueue = StaticData.QUEUE_MAP.get("这是一个想定或其他的标识");
                    if (blockingQueue != null) {
                        blockingQueue.put(msg);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }
}
