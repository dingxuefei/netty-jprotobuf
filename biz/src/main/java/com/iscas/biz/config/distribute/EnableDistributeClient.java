package com.iscas.biz.config.distribute;


import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * //TODO
 *
 * @author zhuquanwen
 * @vesion 1.0
 * @date 2018/7/19 16:47
 * @since jdk1.8
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Import(DistributeClientConfig.class)
public @interface EnableDistributeClient {
}
