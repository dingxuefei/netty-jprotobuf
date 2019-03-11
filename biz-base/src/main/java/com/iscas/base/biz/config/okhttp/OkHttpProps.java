package com.iscas.base.biz.config.okhttp;//package com.iscas.cmi.config.okhttp;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 自动配置属性
 **/
@ConfigurationProperties(prefix = "okhttp")
@Data
public class OkHttpProps {
    private int readTimeout = 2000; //读取超时时间毫秒
    private int writeTimeout = 2000 ; //写数据超时时间毫秒
    private int connectTimeout = 2000; //连接超时时间毫秒
    private int maxIdleConnection = 15 ; //最大空闲数目
    private long keepAliveDuration = 5 ; //keep alive保持时间 分钟


}
