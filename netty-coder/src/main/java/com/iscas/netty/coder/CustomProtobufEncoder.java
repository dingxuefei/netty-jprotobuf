package com.iscas.netty.coder;

import com.google.protobuf.MessageLite;
import com.iscas.netty.coder.util.DataUtils;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

/**
 * //TODO
 *
 * @author zhuquanwen
 * @vesion 1.0
 * @date 2018/7/21 21:25
 * @since jdk1.8
 */
//@ChannelHandler.Sharable
//public class CustomProtobufEncoder extends MessageToByteEncoder<MessageLite> {
//
//    @Override
//    protected void encode(
//            ChannelHandlerContext ctx, MessageLite msg, ByteBuf out) throws Exception {
//        byte[] body = msg.toByteArray();
////        byte[] header = encodeHeader(msg, body.length);
//        byte[] header = DataUtils.encodeHeader(msg, body.length);
//        out.writeBytes(header);
//        out.writeBytes(body);
//        return;
//    }
//}
@ChannelHandler.Sharable
public class CustomProtobufEncoder extends MessageToByteEncoder<Object> {

    @Override
    protected void encode(
            ChannelHandlerContext ctx, Object msg, ByteBuf out) throws Exception {
//        byte[] body = msg.toByteArray();
        DataUtils.encodeHeaderAndBody(msg,out);
//        out.writeBytes(header);
//        out.writeBytes(body);
        return;
    }
}