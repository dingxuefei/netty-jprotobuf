package com.iscas.netty.coder.util;

import com.baidu.bjf.remoting.protobuf.Codec;
import com.baidu.bjf.remoting.protobuf.ProtobufProxy;
import com.iscas.protobuf.dto.HeadDTO;
import com.iscas.protobuf.dto.MessageDTO;
import io.netty.buffer.ByteBuf;

import java.io.IOException;


/**
 * 构建发送请求的工具类
 *
 * @author zhuquanwen
 * @vesion 1.0
 * @date 2018/7/22 16:55
 * @since jdk1.8
 */
public class DataUtils {
    private DataUtils(){}
    public static MessageDTO createData(String clientId, MessageDTO.CommandType cmd, String data){
        MessageDTO messageDTO = new MessageDTO();
        messageDTO.setClientId(clientId);
        messageDTO.setCmd(cmd);
        messageDTO.setData(data);
        return messageDTO;
    }

//    public static  byte[] encodeHeader(MessageLite msg, int bodyLength) {
//        byte messageType = 0x0f;
//        if (msg instanceof Message.MessageBase) {
//            messageType = 0x00;
//        }
//        Header.Head head = Header.Head.newBuilder().setLength(bodyLength).setMessageType(messageType).build();
//        byte[] headerBytes = head.toByteArray();
//
//        return headerBytes;
//
//    }

    public static  void encodeHeaderAndBody(Object msg, ByteBuf out) throws IOException {
        Integer bodyLength = 0;
        Integer messageType = 0;
        if (msg != null) {
            byte[] body = null;
            if (msg instanceof MessageDTO) {
                messageType = 0;
                MessageDTO messageDTO = (MessageDTO) msg;
                //message消息
                Codec<MessageDTO> messageDTOCodec = ProtobufProxy
                        .create(MessageDTO.class);
                body = messageDTOCodec.encode(messageDTO);
                bodyLength = body.length;
            }
            HeadDTO headDTO = new HeadDTO();
            headDTO.setLength(bodyLength);
            headDTO.setMessageType(messageType);
            Codec<HeadDTO> headDTOCodec = ProtobufProxy
                    .create(HeadDTO.class);
            byte[] header = headDTOCodec.encode(headDTO);
            out.writeBytes(header);
            out.writeBytes(body);
        }


    }
//    private static byte[] encodeHeader(int bodyLength) {
//        byte messageType = 0x00;
//        Header.Head head = Header.Head.newBuilder().setLength(bodyLength).setMessageType(messageType).build();
//        byte[] headerBytes = head.toByteArray();
//        return headerBytes;
//
//    }

}
