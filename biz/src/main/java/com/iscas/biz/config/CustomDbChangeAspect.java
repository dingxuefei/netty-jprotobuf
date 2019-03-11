package com.iscas.biz.config;

import com.iscas.base.biz.config.db.DBTypeEnum;
import com.iscas.base.biz.config.db.DbContextHolder;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

/**
 * //TODO
 *
 * @author zhuquanwen
 * @vesion 1.0
 * @date 2019/3/1 10:11
 * @since jdk1.8
 */
@Component
@Aspect
@Order(-100)
@Slf4j
public class CustomDbChangeAspect {
    @Pointcut("execution(* com.iscas.biz.service.mysql.*.*(..))")
    private void mysqlAspect() {
    }

    @Pointcut("execution(* com.iscas.biz.service.sqlite.*.*(..))")
    private void sqliteAspect() {
    }

    @Before( "mysqlAspect()" )
    public void db1() {
        log.info("正在访问mysql数据源...");
        DbContextHolder.setDbType(DBTypeEnum.db1);
    }

    @Before("sqliteAspect()" )
    public void db2 () {
        log.info("正在访问sqlite数据源...");
        DbContextHolder.setDbType(DBTypeEnum.db2);
    }

}
