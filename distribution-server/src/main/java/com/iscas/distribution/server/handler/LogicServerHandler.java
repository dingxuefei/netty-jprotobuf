package com.iscas.distribution.server.handler;

import com.iscas.distribution.server.ChannelRepository;
import com.iscas.protobuf.dto.MessageDTO;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.Attribute;
import io.netty.util.AttributeKey;
import io.netty.util.ReferenceCountUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 分发服务业务处理
 *
 * @author zhuquanwen
 * @vesion 1.0
 * @date 2018/7/19 16:20
 * @since jdk1.8
 */
@Component
@Qualifier("logicServerHandler")
@ChannelHandler.Sharable
@Slf4j
public class LogicServerHandler extends ChannelInboundHandlerAdapter {
    private AtomicInteger integer = new AtomicInteger(0);

    private final AttributeKey<String> clientInfo = AttributeKey.valueOf("clientInfo");

    @Value("${modelId}")
    private String modelId; //模型的ID

    @Qualifier("channelRepository")
    @Autowired
    private ChannelRepository channelRepository;


    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        if (msg != null) {
            if (msg instanceof MessageDTO) {
                log.info("-----：" + msg);
                MessageDTO msgBase = (MessageDTO) msg;
                MessageDTO.CommandType cmd = msgBase.getCmd();
                if(cmd.equals(MessageDTO.CommandType.MODEL_DATA)){
                    //如果是MODEL_DATA,认为是C++模型推送过来的数据。
                    //除了自己的ClientId, 分别分发给其他客户端
                    String clientId = msgBase.getClientId();
                    if(clientId != null){
                        Set<String> keys = channelRepository.keys();
                        msgBase.setCmd(MessageDTO.CommandType.PUSH_DATA);
                        channelRepository.get("web-app").writeAndFlush(msgBase);
                    }
                }
                ReferenceCountUtil.release(msg);
            }
        }
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {

    }

    @SuppressWarnings("deprecation")
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        Attribute<String> attr = ctx.attr(clientInfo);
        String clientId = attr.get();
        log.error("Connection closed, client is " + clientId);
        cause.printStackTrace();
        ctx.channel().close();
        channelRepository.remove(clientId);

    }
}
