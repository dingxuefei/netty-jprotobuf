package com.iscas.netty.coder;

import com.baidu.bjf.remoting.protobuf.Codec;
import com.baidu.bjf.remoting.protobuf.ProtobufProxy;
import com.iscas.protobuf.dto.HeadDTO;
import com.iscas.protobuf.dto.MessageDTO;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import io.netty.util.ReferenceCountUtil;

import java.util.List;

/**
 * //TODO
 *
 * @author zhuquanwen
 * @vesion 1.0
 * @date 2018/7/21 21:31
 * @since jdk1.8
 */
public class CustomProtobufDecoder extends ByteToMessageDecoder {

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        try {
            while (in.readableBytes() > 10) { // 如果可读长度小于包头长度，退出。
                in.markReaderIndex();

                byte[] bytes = new byte[10];
                in.readBytes(bytes);

                //替换为Jprotobuf模式
                Codec<HeadDTO> headDTOCodec = ProtobufProxy.create(HeadDTO.class);
                HeadDTO headDTO = headDTOCodec.decode(bytes);
                int length = headDTO.getLength();
                int dataType = headDTO.getMessageType();
//                Header.Head head = Header.Head.parseFrom(bytes);
//                int length = head.getLength();
//                byte dataType = (byte) head.getMessageType();

                // 如果可读长度小于body长度，恢复读指针，退出。
                if (in.readableBytes() < length) {
                    in.resetReaderIndex();
                    return;
                }

                // 读取body
                ByteBuf bodyByteBuf = in.readBytes(length);

                byte[] array;
                int offset;

                int readableLen = bodyByteBuf.readableBytes();
                if (bodyByteBuf.hasArray()) {
                    array = bodyByteBuf.array();
                    offset = bodyByteBuf.arrayOffset() + bodyByteBuf.readerIndex();
                } else {
                    array = new byte[readableLen];
                    bodyByteBuf.getBytes(bodyByteBuf.readerIndex(), array, 0, readableLen);
                    offset = 0;
                }

                //反序列化
                Object result = decodeBody(dataType, array, offset, readableLen);

//            ctx.fireChannelRead((Message.MessageBase)result);
                out.add(result);
                ReferenceCountUtil.release(bodyByteBuf);
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

//    public MessageLite decodeBody(byte dataType, byte[] array, int offset, int length) throws Exception {
//        if (dataType == 0x0) {
//            return Message.MessageBase.getDefaultInstance().
//        getParserForType().parseFrom(array, offset, length);
//        }
//        return null; // or throw exception
//    }

    public Object decodeBody(int dataType, byte[] array, int offset, int length) throws Exception {
        Object result = null;
        if (dataType == 0) {
            //Message类型
            byte[] bs = new byte[length];
            System.arraycopy(array, offset, bs, 0, length);
            Codec<MessageDTO> messageDTOCodec = ProtobufProxy.create(MessageDTO.class);
            result = messageDTOCodec.decode(bs);

        }
        return result; // or throw exception
    }


}
