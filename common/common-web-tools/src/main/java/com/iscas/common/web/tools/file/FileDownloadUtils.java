package com.iscas.common.web.tools.file;



import com.iscas.common.web.tools.file.limiter.BandWidthLimiter;
import com.iscas.common.web.tools.file.limiter.LimiterOutputStream;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * web-file 相关操作工具集
 *
 * @author zhuquanwen
 * @vesion 1.0
 * @date 2018/7/14 21:19
 * @since jdk1.8
 */
public class FileDownloadUtils {
    /**私有的构造方法*/
    private FileDownloadUtils(){
    }
    /**当前正在下载文件的连接数，作文件下载限流使用*/
    public static AtomicInteger onlineDownloadNumber = new AtomicInteger(0);

    @SuppressWarnings("AlibabaUndefineMagicConstant")
    public static String transFileName(String fileName, HttpServletRequest request) throws Exception{
        String agent = request.getHeader("USER-AGENT");
        //fileName = fileName.trim();       //去首尾空格
        //根据文件头判断请求来自的浏览器，以便有针对性的对文件名转码
        if(null != agent && -1 != agent.indexOf("theworld")){   //世界之窗
            fileName = new String(fileName.getBytes("gb2312"), "ISO8859-1");    //解决下载的文件名中含有小括号转变义符%28%29
        }else if(null != agent && -1 != agent.indexOf("MSIE 8.0")){ //IE8
            String lenFileName = URLEncoder.encode(fileName, "UTF-8");
            if (lenFileName.length() > 150) {    //文件名长度是否大于150个字符
                fileName = new String(fileName.getBytes("gb2312"), "ISO8859-1");
            } else {
                fileName = URLEncoder.encode(fileName,"UTF-8").replace("+","%20");
            }
        }else if(null != agent && -1 != agent.indexOf("MSIE 7.0") && -1 != agent.indexOf("SE 2.X MetaSr 1.0")){ //sogo浏览器
            fileName = URLEncoder.encode(fileName,"UTF-8").replace("+","%20");
        }else if(null != agent && (-1 != agent.indexOf("SV1") || -1 != agent.indexOf("360SE"))){    //360安全浏览器
            String lenFileName = URLEncoder.encode(fileName, "UTF-8");
            if (lenFileName.length() > 150) {    //文件名长度是否大于150个字符
                fileName = new String(fileName.getBytes("gb2312"), "ISO8859-1");
            } else {
                fileName = URLEncoder.encode(fileName,"UTF-8").replace("+","%20");
            }
        }else if(null != agent && -1 != agent.indexOf("Chrome")){   //google
            fileName = new String(fileName.getBytes("gb2312"), "ISO8859-1");    //解决下载的文件名中含有小括号转变义符%28%29
        }else if(null != agent && -1 != agent.indexOf("Firefox")){  //Firefox
            fileName = new String(fileName.getBytes("gb2312"), "ISO8859-1");
        }else if(null != agent && -1 != agent.indexOf("Safari")){   //Firefox
            fileName = new String(fileName.getBytes("UTF-8"), "ISO8859-1");
        }else { //其它浏览器
            fileName = new String(fileName.getBytes("gb2312"), "ISO8859-1");
        }
        return fileName;
    }
    public static void setResponseExcelHeader(HttpServletRequest request, HttpServletResponse response, String name) throws Exception {
        response.reset();
        response.setContentType("application/octet-stream;charset=utf-8"); // 改成输出excel文件
        String fileName = transFileName(name, request);
        response.setHeader(
                "Content-disposition",
                "attachment; filename="
                        +fileName);
    }


    /**
     * http下载文件
     * @version 1.0
     * @since jdk1.8
     * @date 2018/7/14
     * @param request {@link HttpServletRequest}请求
     * @param response {@link HttpServletResponse}响应
     * @param path 要下载的文件路径
     * @param name 文件名称
     * @throws Exception
     * @return void
     */
    public static void downFile(HttpServletRequest request, HttpServletResponse response, String path,
                                String name) throws Exception{
        File file = new File(path);
        downFile(request, response, file, name );
    }


    /**
     * http下载文件
     * @version 1.0
     * @since jdk1.8
     * @date 2018/7/14
     * @param request {@link HttpServletRequest}请求
     * @param response {@link HttpServletResponse}响应
     * @param file 要下载的文件
     * @param name 文件名称
     * @throws Exception
     * @return void
     */
    public static void downFile(HttpServletRequest request, HttpServletResponse response, File file,
                                String name) throws Exception{
        Long fileLength = file.length();// 文件的长度
        if (fileLength != 0) {
            response.reset();
            response.setContentType("application/force-download;charset=utf-8");
            String fileName = transFileName(name, request);
            response.setHeader(
                    "Content-disposition",
                    "attachment; filename="
                            +fileName);
            response.setHeader("Content-Length", String.valueOf(fileLength));
            try(
                    FileInputStream fis = new FileInputStream(file);
                    BufferedInputStream bis = new BufferedInputStream(fis);
                    // 输出流
                    BufferedOutputStream bos = new BufferedOutputStream(response.getOutputStream());
            ){
                byte[] buff = new byte[2048];
                int bytesread;
                // 写文件
                while (-1 != (bytesread = bis.read(buff, 0, buff.length))) {
                    bos.write(buff, 0, bytesread);
                }
            }
        }
    }
    /**
     * 限流下载文件，需要传入maxRate kb/s  最大下载速率
     * @version 1.0
     * @since jdk1.8
     * @date 2018/7/16
     * @param request {@link HttpServletRequest} 请求
     * @param response {@link HttpServletResponse} 响应
     * @param path 文件位置
     * @param name 文件名称
     * @param maxRate 最大下载速率 kb/s
     * @throws Exception
     * @return void
     */
    public static void downFileWithLimiter(HttpServletRequest request, HttpServletResponse response, String path,
                                           String name, int maxRate) throws Exception{
        Long fileLength = new File(path).length();// 文件的长度
        if (fileLength != 0) {
            response.reset();
            response.setContentType("application/force-download;charset=utf-8");
            String fileName = transFileName(name, request);
            response.setHeader(
                    "Content-disposition",
                    "attachment; filename="
                            +fileName);
            response.setHeader("Content-Length", String.valueOf(fileLength));
            BandWidthLimiter bandwidthLimiter = new BandWidthLimiter(maxRate);
            try(
                    FileInputStream fis = new FileInputStream(path);
//                    LimiterInputStream limiterInput = new LimiterInputStream(fis,bandwidthLimiter);
                    BufferedInputStream bis = new BufferedInputStream(fis);
                    // 输出流
                    BufferedOutputStream bos = new BufferedOutputStream(response.getOutputStream());
                    OutputStream os = new LimiterOutputStream(bos,bandwidthLimiter);
            ){
                byte[] buff = new byte[2048];
                int bytesread;
                // 写文件
                while (-1 != (bytesread = bis.read(buff, 0, buff.length))) {
                    os.write(buff, 0, bytesread);
                }
            }
        }
    }

    /**
     * <p>设置最大的下载带宽</p><br/>
     * <p>最好不要频繁设置这个带宽，比如服务启动的时候设置一次<p/>
     *
     * @version 1.0
     * @since jdk1.8
     * @date 2018/7/14
     * @param maxRate 最大下载带宽 单位kb
     * @return void
     */
    public static void setMaxRate(int maxRate){
        BandWidthLimiter.maxBandWith = maxRate;
    }

    /**
     * <p>按照最大下载带宽自动平均分布下载速率<p/> <br/>
     * <p>设置一个服务最大下载带宽,按照当前正在下载的人的数量平均分配下载速率<p/>
     * @version 1.0
     * @since jdk1.8
     * @date 2018/7/13
     * @param request {@link HttpServletRequest} 请求
     * @param response {@link HttpServletResponse} 响应
     * @param path 文件位置
     * @param name 文件名称
     * @throws Exception
     * @return void
     * @see #setMaxRate(int)
     */
    public static void downFileWithLimiter(HttpServletRequest request, HttpServletResponse response, String path,
                                           String name) throws Exception{

        int onlineNumber = onlineDownloadNumber.incrementAndGet();
        int maxRate = BandWidthLimiter.maxBandWith/onlineNumber;
        Long fileLength = new File(path).length();// 文件的长度
        if (fileLength != 0) {
            response.reset();
            response.setContentType("application/force-download;charset=utf-8");
            String fileName = transFileName(name, request);
            response.setHeader(
                    "Content-disposition",
                    "attachment; filename="
                            +fileName);
            response.setHeader("Content-Length", String.valueOf(fileLength));
            BandWidthLimiter bandwidthLimiter = new BandWidthLimiter(maxRate);
            try{
                try(
                        FileInputStream fis = new FileInputStream(path);
//                    LimiterInputStream limiterInput = new LimiterInputStream(fis,bandwidthLimiter);
                        BufferedInputStream bis = new BufferedInputStream(fis);
                        // 输出流
                        BufferedOutputStream bos = new BufferedOutputStream(response.getOutputStream());
                        OutputStream os = new LimiterOutputStream(bos,bandwidthLimiter);
                ){

                    byte[] buff = new byte[2048];
                    int bytesread;
                    // 写文件
                    while (-1 != (bytesread = bis.read(buff, 0, buff.length))) {
                        os.write(buff, 0, bytesread);
                    }
                }
            }catch(Exception e){
                throw e;
            }finally{
                onlineDownloadNumber.decrementAndGet();
            }
        }
    }

    /**
     * 通过文件位置下载EXCEL文件
     * @version 1.0
     * @since jdk1.8
     * @date 2018/7/13
     * @param request {@link HttpServletRequest} 请求
     * @param response {@link HttpServletResponse} 响应
     * @param path 文件位置
     * @param name 文件名称
     * @throws Exception
     * @return void
     */
    public static void downExcelFile(HttpServletRequest request, HttpServletResponse response, String path,
                                     String name) throws Exception {

        Long fileLength = new File(path).length();// 文件的长度
        if (fileLength != 0) {
            setResponseExcelHeader(request,response,name);
            response.setHeader("Content-Length", String.valueOf(fileLength));
            try(
                    FileInputStream fis = new FileInputStream(path);
                    BufferedInputStream bis = new BufferedInputStream(fis);
                    // 输出流
                    BufferedOutputStream bos = new BufferedOutputStream(response.getOutputStream());
            ){

                byte[] buff = new byte[2048];
                int bytesread;
                // 写文件
                while (-1 != (bytesread = bis.read(buff, 0, buff.length))) {
                    bos.write(buff, 0, bytesread);
                }
            }
        }
    }

    /**
     * 通过输入流下载EXCEL文件
     * @version 1.0
     * @since jdk1.8
     * @date 2018/7/13
     * @param request {@link HttpServletRequest} 请求
     * @param response {@link HttpServletResponse} 响应
     * @param inputStream {@link InputStream} 输入流
     * @param name 文件名称
     * @throws Exception
     * @return void
     */
    public static void downExcelStream(HttpServletRequest request, HttpServletResponse response, InputStream inputStream,
                                       String name) throws Exception {
        setResponseExcelHeader(request,response,name);
        try(
                BufferedInputStream bis = new BufferedInputStream(inputStream);
                // 输出流
                BufferedOutputStream bos = new BufferedOutputStream(response.getOutputStream());
        ){

            byte[] buff = new byte[2048];
            int bytesread;
            // 写文件
            while (-1 != (bytesread = bis.read(buff, 0, buff.length))) {
                bos.write(buff, 0, bytesread);
            }
        }
    }
}
