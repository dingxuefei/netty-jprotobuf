package com.iscas.biz.config;

import com.iscas.base.biz.config.StaticInfo;
import com.iscas.base.biz.util.SpringUtils;
import com.iscas.templet.common.ResponseEntity;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

/**
 * 这个类没有写在base-biz下的原因是AOP 切入点 对应表达式的包名 没法在父模块定义
 *
 * @author zhuquanwen
 * @vesion 1.0
 * @date 2018/10/9 13:56
 * @since jdk1.8
 */
@Aspect
@Component
@Slf4j
public class ResponseAspect {

    /**
     * 定义拦截规则：拦截com.iscas.biz.controller..*(..))包下面的所有类中
     */
    @Pointcut("execution(* com.iscas.biz.controller..*.*(..))")
    public void controllerMethodPointcut() {
    }
    @Around(value = "controllerMethodPointcut()")
    public Object around(final ProceedingJoinPoint joinPoint) throws Throwable {
        long start = System.currentTimeMillis();
        StaticInfo.START_TIME_THREAD_LOCAL.set(start);
        try{
            Object result = joinPoint.proceed();

            //如果是ResponseEntity类型那么直接注入值
            if(result != null && result instanceof ResponseEntity){
                ResponseEntity responseEntity = (ResponseEntity) result;
                responseEntity.setTookInMillis(System.currentTimeMillis() - start);

                //注入requestURL
                HttpServletRequest request = SpringUtils.getRequest();
                if(request != null) {
                    responseEntity.setRequestURL(request.getRequestURI());
                }
            }
            return result;
        }finally {

        }

    }
}
