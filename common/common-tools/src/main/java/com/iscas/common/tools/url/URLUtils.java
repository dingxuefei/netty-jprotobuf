package com.iscas.common.tools.url;

import java.net.MalformedURLException;
import java.net.URL;

/**
 * url解析工具类
 *
 * @author zhuquanwen
 * @vesion 1.0
 * @date 2018/10/26 17:13
 * @since jdk1.8
 */
public class URLUtils {
    private URLUtils(){}

//     System.out.println("URL 是 " + url.toString());
//        System.out.println("协议是 " + url.getProtocol());
//        System.out.println("文件名是 " + url.getFile());
//        System.out.println("主机是 " + url.getHost());
//        System.out.println("路径是 " + url.getPath());
//        System.out.println("端口号是 " + url.getPort());
//        System.out.println("默认端口号是 "
//                + url.getDefaultPort());
//


    /**
     * 解析URL 协议
     * @version 1.0
     * @since jdk1.8
     * @date 2018/10/26
     * @param address 网址
     * @throws
     * @return java.lang.String
     */
    public static String getProtocol(String address) throws MalformedURLException {
        URL url = new URL(address);
        return url.getProtocol();
    }

    /**
     * 解析URL 的文件名，没有返回空字符串
     * @version 1.0
     * @since jdk1.8
     * @date 2018/10/26
     * @param address 网址
     * @throws
     * @return java.lang.String
     */
    public static String getFile(String address) throws MalformedURLException {
        URL url = new URL(address);
        return url.getFile();
    }

    /**
     * 解析URL 的主机名/域名，没有返回空字符串
     * @version 1.0
     * @since jdk1.8
     * @date 2018/10/26
     * @param address 网址
     * @throws
     * @return java.lang.String
     */
    public static String getHost(String address) throws MalformedURLException {
        URL url = new URL(address);
        return url.getHost();
    }

    /**
     * 解析URL 的路径，没有返回空字符串
     * @version 1.0
     * @since jdk1.8
     * @date 2018/10/26
     * @param address 网址
     * @throws
     * @return java.lang.String
     */
    public static String getPath(String address) throws MalformedURLException {
        URL url = new URL(address);
        return url.getPath();
    }

    /**
     * 解析URL 的端口,一般网址内没有端口都会使用默认端口会返回-1
     * @version 1.0
     * @since jdk1.8
     * @date 2018/10/26
     * @param address 网址
     * @throws
     * @return java.lang.String
     */
    public static int getPort(String address) throws MalformedURLException {
        URL url = new URL(address);
        return url.getPort();
    }

    /**
     * 解析URL 的默认端口，没有返回-1
     * @version 1.0
     * @since jdk1.8
     * @date 2018/10/26
     * @param address 网址
     * @throws
     * @return java.lang.String
     */
    public static int getDefaultPort(String address) throws MalformedURLException {
        URL url = new URL(address);
        return url.getDefaultPort();
    }
}
