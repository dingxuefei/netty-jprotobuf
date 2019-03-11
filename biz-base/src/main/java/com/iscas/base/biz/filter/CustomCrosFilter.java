package com.iscas.base.biz.filter;


import com.iscas.base.biz.config.cros.CrosProps;
import lombok.AllArgsConstructor;
import org.springframework.util.Assert;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.CorsProcessor;
import org.springframework.web.cors.CorsUtils;
import org.springframework.web.cors.DefaultCorsProcessor;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * 自定义跨域过滤器，可以通过springboot auto config 配置
 *
 * @Author: zhuquanwen
 * @Description:
 * @Date: 2018/3/20 15:12
 * @Modified:
 **/
@AllArgsConstructor
public class CustomCrosFilter extends OncePerRequestFilter {
    private final CorsConfigurationSource configSource;
    private CorsProcessor processor = new DefaultCorsProcessor();
    private CrosProps crosProps;
    private Map<String, String> ignoreUrlAllMatchMap = new HashMap<>();
    List<String> ignoreUrlPrefixMapList = new ArrayList<>();
    public CustomCrosFilter(CorsConfigurationSource configSource, CrosProps crosProps) {
        Assert.notNull(configSource, "CorsConfigurationSource must not be null");
        this.configSource = configSource;
        this.crosProps = crosProps;
        if(crosProps.getIgnoreUrls() != null){
            for (String urlStr: crosProps.getIgnoreUrls()){
                if(urlStr.endsWith("/*")){
                    ignoreUrlPrefixMapList.add(urlStr.substring(0,urlStr.lastIndexOf("/*")));
                }else if("/**".equals(urlStr)){
                    ignoreUrlPrefixMapList.add(urlStr.substring(0,urlStr.lastIndexOf("/**")));
                }
                else{
                    ignoreUrlAllMatchMap.put(urlStr , urlStr);
                }
            }
        }
    }


    public void setCorsProcessor(CorsProcessor processor) {
        Assert.notNull(processor, "CorsProcessor must not be null");
        this.processor = processor;
    }


    @SuppressWarnings("AlibabaUndefineMagicConstant")
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        if (!ignoreMath(request) && CorsUtils.isCorsRequest(request)) {
            String origin = request.getHeader("Origin");
            if (origin == null || "null".equals(origin)) {
                origin = crosProps.getOrigin();
            }
            response.setHeader("Access-Control-Allow-Origin", origin);
            //服务器同意客户端发送cookies
            response.setHeader("Access-Control-Allow-Credentials", crosProps.getCredentials());

            response.setHeader("Access-Control-Allow-Methods", crosProps.getMethods());
            response.setHeader("Access-Control-Allow-Headers", crosProps.getHeaders());
            if (CorsUtils.isPreFlightRequest(request)) {
                return;
            }
        }
        filterChain.doFilter(request, response);
    }

    private boolean ignoreMath(HttpServletRequest request){
        boolean flag = false;
        String contextPath = request.getContextPath();
        String uri = request.getRequestURI();
        String uri1 = uri.substring(uri.indexOf(contextPath) + contextPath.length());
        if(ignoreUrlAllMatchMap.get(uri1) != null){
            flag = true;
        }else{
            for (String urlStr : ignoreUrlPrefixMapList){
                if(uri1.startsWith(urlStr)){
                    flag = true;
                    break;
                }
            }
        }
        return flag;
    }

}
