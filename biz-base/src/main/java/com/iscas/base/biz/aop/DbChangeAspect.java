package com.iscas.base.biz.aop;

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
//@Component
//@Aspect
//@Order(-90)
//@Slf4j
public class DbChangeAspect {
    @Pointcut("execution(* com.iscas.base.biz.service..*.*(..))")
    private void mysqlAspect() {
    }

//    @Pointcut("execution(* com.iscas.base.biz.service..*.*(..))")
//    private void sqlliteAspect() {
//    }

    @Before( "mysqlAspect()" )
    public void db1() {
//        log.info("正在访问mysql数据源...");
        DbContextHolder.setDbType(DBTypeEnum.db1);
    }

//    @Before("sqlliteAspect()" )
//    public void db2 () {
//        log.info("正在访问sqllite数据源...");
//        DbContextHolder.setDbType(DBTypeEnum.db2);
//    }

}
