package com.iscas.base.biz.aop.enable;

import com.iscas.base.biz.config.log.VisitLogRecordConfig;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * //TODO
 *
 * @author zhuquanwen
 * @vesion 1.0
 * @date 2018/8/30 18:11
 * @since jdk1.8
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Import(VisitLogRecordConfig.class)
public @interface EnableVisitLog {
}
