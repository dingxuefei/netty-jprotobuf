package com.iscas.protobuf.dto;

import com.baidu.bjf.remoting.protobuf.FieldType;
import com.baidu.bjf.remoting.protobuf.annotation.Protobuf;
import lombok.Data;

@Data
public class HeadDTO {
    @Protobuf(fieldType = FieldType.FIXED32, order = 1, required = true)
    public Integer length;
    @Protobuf(fieldType = FieldType.FIXED32, order = 2, required = true)
    public Integer messageType;
}
