package com.iscas.base.biz.config.ratelimiter;

import com.iscas.base.biz.config.StaticInfo;
import com.iscas.base.biz.filter.RateLimiterFilter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;

/**
 *
 *
 * @author zhuquanwen
 * @vesion 1.0
 * @date 2018/7/18 11:30
 * @since jdk1.8
 */
@Slf4j
@Configuration
@EnableConfigurationProperties(RateLimiterProps.class)
@ConditionalOnClass(RateLimiterFilter.class)
@ConditionalOnProperty(prefix = "rate.limiter",matchIfMissing = true,value = "enabled")
public class RateLimiterConfig {
    @Autowired
    private RateLimiterProps rateLimiterProps;

    @Bean
    public RateLimiterFilter rateLimiterFilter(){

        return new RateLimiterFilter(rateLimiterProps);
    }
    @Bean
//    @Order(Ordered.HIGHEST_PRECEDENCE + 2)
    public FilterRegistrationBean rateFilterRegistrationBean(){
        if(log.isInfoEnabled()){
            log.info("-----注册rateLimiter过滤器-------");
        }
        FilterRegistrationBean frb = new FilterRegistrationBean();
        frb.setOrder(Ordered.HIGHEST_PRECEDENCE + 2);
        frb.setFilter(rateLimiterFilter());
        frb.addUrlPatterns("/*");
        frb.setName("ratelimiter");

        //将开启限流标记为true，在方法级限流时使用
        StaticInfo.ENABLE_RATELIMITER = true;
        return frb;
    }
}
