package com.iscas.biz.netty;

import com.iscas.biz.config.distribute.DistributeClientProps;
import com.iscas.biz.netty.handler.IdleClientHandler;
import com.iscas.biz.netty.handler.LogicClientHandler;
import com.iscas.netty.coder.CustomProtobufDecoder;
import com.iscas.netty.coder.CustomProtobufEncoder;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.timeout.IdleStateHandler;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.TimeUnit;

/**
 * //TODO
 *
 * @author zhuquanwen
 * @vesion 1.0
 * @date 2018/7/19 16:33
 * @since jdk1.8
 */

@Slf4j
public class DistributeClient {
    public static Channel clientChannel;
    private LogicClientHandler logicClientHandler;
    private DistributeClientProps distributeClientProps;

    public DistributeClient(LogicClientHandler logicClientHandler,
                            DistributeClientProps distributeClientProps) {
        this.logicClientHandler = logicClientHandler;
        this.distributeClientProps = distributeClientProps;
    }

//    private final static String HOST = "127.0.0.1";
//    private final static int PORT = 8090;
//    private final static int READER_IDLE_TIME_SECONDS = 20;//读操作空闲20秒
//    private final static int WRITER_IDLE_TIME_SECONDS = 20;//写操作空闲20秒
//    private final static int ALL_IDLE_TIME_SECONDS = 40;//读写全部空闲40秒

    private EventLoopGroup loop = new NioEventLoopGroup();

//    public static void main(String[] args) throws Exception {
//        DistributeClient client = new DistributeClient();
//        client.run();
//    }

    public void run() throws Exception {
        try {
            doConnect(new Bootstrap(), loop);
        }catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * netty client 连接，连接失败10秒后重试连接
     */
    public Bootstrap doConnect(Bootstrap bootstrap, EventLoopGroup eventLoopGroup) {
        try {
            if (bootstrap != null) {
                bootstrap.group(eventLoopGroup);
                bootstrap.channel(NioSocketChannel.class);
                bootstrap.option(ChannelOption.SO_KEEPALIVE, true);
                bootstrap.handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    public void initChannel(SocketChannel ch) throws Exception {
                        ChannelPipeline p = ch.pipeline();

                        p.addLast("idleStateHandler", new IdleStateHandler(distributeClientProps.getReadIdleTimeSeconds()
                                , distributeClientProps.getWriteIdleTimeSeconds(), distributeClientProps.getAllIdleTimeSeconds(), TimeUnit.SECONDS));


//                        p.addLast(new ProtobufVarint32FrameDecoder());
//                        p.addLast(new ProtobufDecoder(Message.MessageBase.getDefaultInstance()));
//
//                        p.addLast(new ProtobufVarint32LengthFieldPrepender());
//                        p.addLast(new ProtobufEncoder());
                        p.addLast("decoder",new CustomProtobufDecoder());
                        p.addLast("encoder",new CustomProtobufEncoder());
                        p.addLast("clientHandler", logicClientHandler);
                        p.addLast("idleTimeoutHandler", new IdleClientHandler(DistributeClient.this,
                                distributeClientProps.getClientId()));
                    }
                });
                bootstrap.remoteAddress(distributeClientProps.getRemoteHost(), distributeClientProps.getRemotePort());
                ChannelFuture f = bootstrap.connect().addListener((ChannelFuture futureListener)->{
                    final EventLoop eventLoop = futureListener.channel().eventLoop();
                    if (!futureListener.isSuccess()) {
                        log.warn("Failed to connect to server, try connect after 10s");
                        futureListener.channel().eventLoop().schedule(() -> doConnect(new Bootstrap(), eventLoop), 10, TimeUnit.SECONDS);
                    }
                });
                clientChannel = f.channel();

                f.channel().closeFuture().sync();
                eventLoopGroup.shutdownGracefully();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return bootstrap;
    }
}
