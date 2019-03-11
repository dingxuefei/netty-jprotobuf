package com.iscas.base.biz.config.okhttp;

import com.iscas.base.biz.service.common.OkHttpCustomClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 *
 * <p>okhttp 自动配置</p>
 **/
@Configuration//声明配置类
@EnableConfigurationProperties(OkHttpProps.class)
@ConditionalOnClass(OkHttpCustomClient.class)
@ConditionalOnProperty(prefix="okhttp",value="enabled",matchIfMissing=true)//
public class OkHttpClientAutoConfiguration {
    private Logger log = LoggerFactory.getLogger(OkHttpClientAutoConfiguration.class);
    @Autowired
    private OkHttpProps okHttpConfig;

    @Bean
    @ConditionalOnMissingBean(OkHttpCustomClient.class)//容器中没有AuthorService的Bean的条件下配置该Bean
    public OkHttpCustomClient myOkHttpClient(){
        if(log.isInfoEnabled()){
            log.info("------初始化自定义的OkHttpClient--------");
        }
        OkHttpCustomClient okHttpClient = new OkHttpCustomClient(okHttpConfig);
        return okHttpClient;
    }

}
