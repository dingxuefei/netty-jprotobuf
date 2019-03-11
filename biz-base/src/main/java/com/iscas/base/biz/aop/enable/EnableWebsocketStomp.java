package com.iscas.base.biz.aop.enable;

import com.iscas.base.biz.config.stomp.WebSocketStompConfig;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * 开启websoket支持
 *
 * @author zhuquanwen
 * @vesion 1.0
 * @date 2018/7/18 14:24
 * @since jdk1.8
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Import(WebSocketStompConfig.class)
public @interface EnableWebsocketStomp {
}
