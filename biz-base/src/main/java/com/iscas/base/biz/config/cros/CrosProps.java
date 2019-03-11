package com.iscas.base.biz.config.cros;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.Arrays;
import java.util.List;


@ConfigurationProperties(prefix = "cros")
@Data
@NoArgsConstructor
public class CrosProps {
    private String origin = "*";
    private String credentials = "true";
    private String methods = "POST, GET, PUT, DELETE, OPTIONS";
    private String headers = "Content-Type, Data-Type, Access-Control-Allow-Headers, Authorization, X-Requested-With, Accept, DataType, responseType";
    private List<String> ignoreUrls = Arrays.asList("/webSocketServer/*");
}
