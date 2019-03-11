package com.iscas.protobuf;

import com.baidu.bjf.remoting.protobuf.ProtobufIDLProxy;
import lombok.Cleanup;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

/**
 * .proto转为JavaBean的代码生成器
 *
 * @author zhuquanwen
 * @vesion 1.0
 * @date 2019/3/6 22:25
 * @since jdk1.8
 */
public class ProtoToBeanGegerator {
    /**
     * proto文件名称
     * */
    private static String protoName = "Message.proto";

    /**
     * 生成的java目标目录路径
     * */
    private static String targetPath = "D:/test";

    public static void main(String[] args) throws IOException {
        @Cleanup InputStream fis = ProtoToBeanGegerator.class.getResourceAsStream("/" + protoName);
        ProtobufIDLProxy.generateSource(fis, new File(targetPath));

    }
}
