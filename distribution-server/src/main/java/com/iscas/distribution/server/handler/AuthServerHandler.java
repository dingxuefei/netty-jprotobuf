package com.iscas.distribution.server.handler;

import com.iscas.distribution.server.ChannelRepository;
import com.iscas.netty.coder.util.DataUtils;
import com.iscas.protobuf.dto.MessageDTO;
import io.netty.channel.*;
import io.netty.util.Attribute;
import io.netty.util.AttributeKey;
import io.netty.util.ReferenceCountUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * //TODO
 *
 * @author zhuquanwen
 * @vesion 1.0
 * @date 2018/7/19 16:13
 * @since jdk1.8
 */
@Component
@Qualifier("authServerHandler")
@ChannelHandler.Sharable
@Slf4j
public class AuthServerHandler extends ChannelInboundHandlerAdapter {
    private static final AtomicInteger integer = new AtomicInteger(0);
    private final AttributeKey<String> clientInfo = AttributeKey.valueOf("clientInfo");

    @Autowired
    @Qualifier("channelRepository")
    private ChannelRepository channelRepository;
    @Value("${clientIds}")
    private String clientIds;

    @SuppressWarnings("deprecation")
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        if (msg instanceof MessageDTO) {
            MessageDTO msgBase = (MessageDTO) msg;
            String clientId = msgBase.getClientId();
            /*认证处理*/
            if (msgBase.getCmd().equals(MessageDTO.CommandType.AUTH)) {
                log.info("----验证处理----");
                boolean flag = false;
                for (String id : clientIds.split(",")) {
                    if (id.equals(clientId)) {
                        flag = true;
                        break;
                    }
                }
                if (!flag) {
                    channelRepository.remove(clientId);
                    log.info("----验证失败即将断开连接----");
                    ChannelFuture channelFuture = ctx.writeAndFlush(DataUtils.createData(clientId, MessageDTO.CommandType.AUTH_BACK, "fail"));
                    channelFuture.addListener(new ChannelFutureListener() {
                        @Override
                        public void operationComplete(ChannelFuture future) throws Exception {
                            future.channel().close();
                        }
                    });
                } else {
                    log.info("---验证成功---");
                    Attribute<String> attr = ctx.attr(clientInfo);
                    attr.set(clientId);
                    channelRepository.put(clientId, ctx.channel());
                    msgBase.setData("success");
                    msgBase.setCmd(MessageDTO.CommandType.AUTH_BACK);
                    ctx.writeAndFlush( msgBase);
                }

            } else if (msgBase.getCmd().equals(MessageDTO.CommandType.PING)) {
                //处理ping消息
                ctx.writeAndFlush(DataUtils.createData(clientId, MessageDTO.CommandType.PONG, "This is pong data"));
            } else {
                Channel ch = channelRepository.get(clientId);
                if (null == ch || !ch.isOpen()) {
                    channelRepository.remove(clientId);
                    log.info("----没有权限，即将断开连接----");
                    ChannelFuture channelFuture = ctx.writeAndFlush(DataUtils.createData(clientId, MessageDTO.CommandType.AUTH_BACK, "fail"));
                    channelFuture.addListener(new ChannelFutureListener() {
                        @Override
                        public void operationComplete(ChannelFuture future) throws Exception {
                            future.channel().close();
                        }
                    });
                } else {
                    //触发下一个handler
                    ctx.fireChannelRead(msg);
                    log.info("----将进入业务入处理逻辑-----");
                }
            }
            ReferenceCountUtil.release(msg);
        } else {
            //触发下一个handler
            ctx.fireChannelRead(msg);
            log.info("----将进入业务入处理逻辑-----");
            ReferenceCountUtil.release(msg);
        }

    }


}
