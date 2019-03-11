package com.iscas.biz.config.distribute;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * //TODO
 *
 * @author zhuquanwen
 * @vesion 1.0
 * @date 2018/7/19 17:02
 * @since jdk1.8
 */
@Data
@Component
@ConfigurationProperties(prefix = "distribute.client")
public class DistributeClientProps {
    private String clientId;
    private int remotePort;
    private String remoteHost;
    private int readIdleTimeSeconds;
    private int writeIdleTimeSeconds;
    private int allIdleTimeSeconds;
}
