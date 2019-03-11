package com.iscas.base.biz.util;

import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * spring相关操作工具类
 * @auth zhuquanwen
 **/
public class SpringUtils {
    private SpringUtils(){}

    public static ServletContext getServletContext(){
        return getRequest().getServletContext();
    }

    /*获取request*/
    public static HttpServletRequest getRequest(){
        HttpServletRequest request = null;
        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if(requestAttributes != null){
            request = requestAttributes.getRequest();
        }
        return request;
    }

    /*获取response*/
    public static HttpServletResponse getResponse(){
        HttpServletResponse response = null;
        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if(requestAttributes != null){
            response = requestAttributes.getResponse();
        }
        return response;
    }

    public static HttpSession getSession(){
        HttpServletRequest request = getRequest();
        if(request != null){
            return request.getSession();
        }
        return null;
    }

    public static HttpSession getSession(boolean flag){
        HttpServletRequest request = getRequest();
        if(request != null){
            return request.getSession(flag);
        }
        return null;
    }

    @SuppressWarnings("AlibabaUndefineMagicConstant")
    public static String getIpAddr() {
        HttpServletRequest request = getRequest();
        String ipAddress = null;
        try {
            ipAddress = request.getHeader("x-forwarded-for");
            if (ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress)) {
                ipAddress = request.getHeader("Proxy-Client-IP");
            }
            if (ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress)) {
                ipAddress = request.getHeader("WL-Proxy-Client-IP");
            }
            if (ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress)) {
                ipAddress = request.getRemoteAddr();
                if ("127.0.0.1".equals(ipAddress)) {
                    // 根据网卡取本机配置的IP
                    InetAddress inet = null;
                    try {
                        inet = InetAddress.getLocalHost();
                    } catch (UnknownHostException e) {
                        e.printStackTrace();
                    }
                    ipAddress = inet.getHostAddress();
                }
            }
            // 对于通过多个代理的情况，第一个IP为客户端真实IP,多个IP按照','分割
            if (ipAddress != null && ipAddress.length() > 15) { // "***.***.***.***".length()
                // = 15
                if (ipAddress.indexOf(",") > 0) {
                    ipAddress = ipAddress.substring(0, ipAddress.indexOf(","));
                }
            }
        } catch (Exception e) {
            ipAddress="";
        }

        return ipAddress;
    }

}
