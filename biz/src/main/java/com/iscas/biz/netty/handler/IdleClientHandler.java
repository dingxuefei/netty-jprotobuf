package com.iscas.biz.netty.handler;

import com.iscas.biz.netty.DistributeClient;
import com.iscas.protobuf.dto.MessageDTO;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.TimeUnit;

/**
 * //TODO
 *
 * @author zhuquanwen
 * @vesion 1.0
 * @date 2018/7/19 16:35
 * @since jdk1.8
 */
@Slf4j

public class IdleClientHandler extends SimpleChannelInboundHandler<MessageDTO> {
    private DistributeClient nettyClient;
    private int heartbeatCount = 0;
    private  String clientId = "123456789";

    /**
     * @param nettyClient
     */
    public IdleClientHandler(DistributeClient nettyClient, String clientId) {
        this.nettyClient = nettyClient;
        this.clientId = clientId;
    }

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if (evt instanceof IdleStateEvent) {
            IdleStateEvent event = (IdleStateEvent) evt;
            String type = "";
            if (event.state() == IdleState.READER_IDLE) {
                type = "read idle";
            } else if (event.state() == IdleState.WRITER_IDLE) {
                type = "write idle";
            } else if (event.state() == IdleState.ALL_IDLE) {
                type = "all idle";
                sendPingMsg(ctx);
            }
            log.info(ctx.channel().remoteAddress() + "超时类型：" + type);

        } else {
            super.userEventTriggered(ctx, evt);
        }
    }

    /**
     * 发送ping消息
     * @param context
     */
    protected void sendPingMsg(ChannelHandlerContext context) {
        MessageDTO messageDTO = new MessageDTO();
        messageDTO.setCmd(MessageDTO.CommandType.PING);
        messageDTO.setData("This is a ping msg");
        messageDTO.setClientId("web-app");
        context.writeAndFlush(messageDTO);
        heartbeatCount++;
        log.info("Client sent ping msg to " + context.channel().remoteAddress() + ", count: " + heartbeatCount);
    }

    /**
     * 处理断开重连
     */
    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        final EventLoop eventLoop = ctx.channel().eventLoop();
        eventLoop.schedule(() -> nettyClient.doConnect(new Bootstrap(), eventLoop), 10L, TimeUnit.SECONDS);
        super.channelInactive(ctx);
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, MessageDTO msg) throws Exception {

    }
}
