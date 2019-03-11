package com.iscas.base.biz.filter;

import com.google.common.util.concurrent.RateLimiter;
import com.google.gson.Gson;
import com.iscas.templet.common.ResponseEntity;
import com.iscas.base.biz.config.ratelimiter.RateLimiterProps;
import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * RateLimiter 应用总体限流过滤器
 *
 * @author zhuquanwen
 **/
public class RateLimiterFilter extends OncePerRequestFilter {

    private Logger log = LoggerFactory.getLogger(getClass());
    private RateLimiterProps rateLimiterProps;
    private RateLimiter rateLimiter = null;
    private List<String> staticUrls;

    public RateLimiterFilter(RateLimiterProps rateLimiterProps){
        this.rateLimiterProps = rateLimiterProps;
        this.staticUrls = rateLimiterProps.getStaticUrl();
        rateLimiter = RateLimiter.create(rateLimiterProps.getPermitsPerSecond());
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String contextPath = request.getContextPath();
        AntPathMatcher antPathMatcher = new AntPathMatcher();

        if(!CollectionUtils.isEmpty(staticUrls)){
            for (String url: staticUrls) {
                if(antPathMatcher.match(contextPath  + url, request.getRequestURI())){
                    filterChain.doFilter(request, response);
                    return;
                }
            }
        }


        if (!rateLimiter.tryAcquire(rateLimiterProps.getMaxWait(), TimeUnit.MILLISECONDS)) {
            //获取令牌失败，并且超过超时时间
            log.warn(request.getRemoteAddr() + "访问" + request.getRequestURI() + "获取令牌失败");
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
        }else{
            filterChain.doFilter(request, response);
        }

    }
}
