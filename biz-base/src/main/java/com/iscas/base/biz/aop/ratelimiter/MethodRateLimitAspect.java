package com.iscas.base.biz.aop.ratelimiter;

import com.google.common.util.concurrent.RateLimiter;
import com.google.gson.Gson;
import com.iscas.base.biz.config.StaticInfo;
import com.iscas.templet.common.ResponseEntity;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

/*
* 方法级别限流
* */
@Aspect
@Component
public class MethodRateLimitAspect {
    private Logger logger = LoggerFactory.getLogger(MethodRateLimitAspect.class);
    private Map<String,RateLimiter> rateLimiterMap = new ConcurrentHashMap<>();
//    @Pointcut(("@annotation(com.iscas.quickframe.aop.MethodRateLimit)"))
//    public void rateLimit(){
//
//    }
    @Around("@annotation(methodRateLimit)")
    public Object before(final ProceedingJoinPoint joinPoint, MethodRateLimit methodRateLimit) throws Throwable {
        //如果未开启限流注解，不进行方法限流逻辑判断
        if(!StaticInfo.ENABLE_RATELIMITER){
            return joinPoint.proceed();
        }
        //获取request对象
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        String remoteAddr = request.getRemoteAddr();
        //获取当前的response对象，如果超过超时时间还得不到令牌，返回服务器繁忙的提示
        HttpServletResponse response = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getResponse();
        //生成一个唯一方法名的key,作为Map的键
        Signature signature = joinPoint.getSignature();
        String methodName = signature.getName();
        Class clazz = signature.getDeclaringType();
        String className = clazz.getName();
        String key = className + "." + methodName;
        RateLimiter rateLimiter = null;
        if(rateLimiterMap.get(key) == null){
            rateLimiter = RateLimiter.create(methodRateLimit.permitsPerSecond());
        }else{
            rateLimiter = rateLimiterMap.get(key);
            double rate = rateLimiter.getRate();
            //如果rate变化了，重新生成令牌桶
            if(rate != methodRateLimit.permitsPerSecond()){
                rateLimiter = RateLimiter.create(methodRateLimit.permitsPerSecond());
            }
        }
        //将令牌桶放入Map
        rateLimiterMap.put(key, rateLimiter);
        if (!rateLimiter.tryAcquire(methodRateLimit.maxWait(), TimeUnit.MILLISECONDS)) {
            //获取令牌失败，并且超过超时时间
            logger.warn(remoteAddr + "访问方法:" + key + ",短期无法获取令牌");
            ResponseEntity responseEntity = new ResponseEntity(500,"服务器繁忙,请求超时");
            Gson gson = new Gson();
            try {

                //返回服务器繁忙提示
                response.setContentType("application/json; charset=utf-8");
                response.setCharacterEncoding("UTF-8");
                response.setStatus(HttpStatus.REQUEST_TIMEOUT.value());
                ServletOutputStream pw = response.getOutputStream();
                pw.write(gson.toJson(responseEntity).getBytes("UTF-8"));
                pw.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }else{
            logger.debug(remoteAddr + "访问方法:" + key + ",获取令牌成功");
            return joinPoint.proceed();
        }

    }
}
