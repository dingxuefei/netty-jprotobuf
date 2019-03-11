package com.iscas.protobuf.dto;

import com.baidu.bjf.remoting.protobuf.FieldType;
import com.baidu.bjf.remoting.protobuf.EnumReadable;
import com.baidu.bjf.remoting.protobuf.annotation.Protobuf;
import lombok.Data;

@Data
public class MessageDTO {
    @Protobuf(fieldType = FieldType.STRING, order = 1, required = true)
    public String clientId;
    @Protobuf(fieldType = FieldType.ENUM, order = 2, required = true)
    public CommandType cmd;
    @Protobuf(fieldType = FieldType.STRING, order = 3, required = false)
    public String data;

    public static enum CommandType implements EnumReadable {
        AUTH(1), PING(2), PONG(3), UPLOAD_DATA(4), PUSH_DATA(5), MODEL_DATA(6), AUTH_BACK(11);
        private final int value;

        CommandType(int value) {
            this.value = value;
        }

        public int value() {
            return value;
        }
    }
}
