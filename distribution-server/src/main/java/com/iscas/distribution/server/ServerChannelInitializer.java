package com.iscas.distribution.server;

import com.iscas.distribution.server.handler.IdleServerHandler;
import com.iscas.netty.coder.CustomProtobufDecoder;
import com.iscas.netty.coder.CustomProtobufEncoder;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.timeout.IdleStateHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

/**
 * //TODO
 *
 * @author zhuquanwen
 * @vesion 1.0
 * @date 2018/7/19 16:09
 * @since jdk1.8
 */
@Component
@Qualifier("serverChannelInitializer")
public class ServerChannelInitializer extends ChannelInitializer<SocketChannel> {
    private final static int READER_IDLE_TIME_SECONDS = 30;//读操作空闲20秒
    private final static int WRITER_IDLE_TIME_SECONDS = 30;//写操作空闲20秒
    private final static int ALL_IDLE_TIME_SECONDS = 60;//读写全部空闲40秒

    @Autowired
    @Qualifier("authServerHandler")
    private ChannelInboundHandlerAdapter authServerHandler;

    @Autowired
    @Qualifier("logicServerHandler")
    private ChannelInboundHandlerAdapter logicServerHandler;

    @Override
    protected void initChannel(SocketChannel socketChannel) throws Exception {
        ChannelPipeline p = socketChannel.pipeline();


        p.addLast("idleStateHandler", new IdleStateHandler(READER_IDLE_TIME_SECONDS
                , WRITER_IDLE_TIME_SECONDS, ALL_IDLE_TIME_SECONDS, TimeUnit.SECONDS));
        p.addLast("decoder",new CustomProtobufDecoder());
        p.addLast("encoder",new CustomProtobufEncoder());

//        p.addLast("linebaseFrame",new LineBasedFrameDecoder(1024*1024*1024));
//        p.addLast("msgDecorder",new MsgDecoder());
//        p.addLast("msgEncorder",new MsgEncoder());
        //        p.addLast(new ProtobufVarint32FrameDecoder());
//        p.addLast(new ProtobufDecoder(Message.MessageBase.getDefaultInstance()));
//
//        p.addLast(new ProtobufVarint32LengthFieldPrepender());
//        p.addLast(new ProtobufEncoder());

        p.addLast("authServerHandler", authServerHandler);
        p.addLast("hearableServerHandler", logicServerHandler);
        p.addLast("idleTimeoutHandler", new IdleServerHandler());
    }
}
