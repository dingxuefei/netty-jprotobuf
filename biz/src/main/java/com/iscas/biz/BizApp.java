package com.iscas.biz;


import com.iscas.base.biz.aop.enable.*;
import com.iscas.biz.config.distribute.EnableDistributeClient;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * //TODO
 *
 * @author zhuquanwen
 * @vesion 1.0
 * @date 2018/10/10 17:45
 * @since jdk1.8
 */
@Configuration
@EnableAutoConfiguration(/*exclude = {DruidDataSourceAutoConfigure.class, DataSourceAutoConfiguration.class}*/)
@ComponentScan(basePackages = {"com.iscas.base.biz","com.iscas.biz"})
@MapperScan({"com.iscas.biz.mapper*","com.iscas.base.biz.common.mapper*",
        "com.iscas.base.biz.mapper*","com.iscas.base.biz.test.mapper*"})
@EnableCaching //开启缓存
@EnableTransactionManagement //开启事务支持
@EnableRateLimiter //开启自定义的限流支持
@EnableAuth //开启自定义的用户认证，权限校验
@EnableWebsocketStomp //开启websocketstomp支持
@EnableLog //允许日志记录
@EnableVisitLog //允许访问Data日志记录
@ServletComponentScan
@EnableDistributeClient //允许分发客户端开启
public class BizApp extends SpringBootServletInitializer {
    public static void main(String[] args) {
        SpringApplication.run(BizApp.class, args);
    }
    /**
     *重写configure
     * @param builder
     * @return
     */
    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
        return builder.sources(BizApp.class);
    }

}
