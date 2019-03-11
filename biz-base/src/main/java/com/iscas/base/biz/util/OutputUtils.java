package com.iscas.base.biz.util;

import com.google.gson.Gson;
import com.iscas.templet.common.ResponseEntity;
import org.springframework.http.HttpStatus;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;

/**
 * 输出工具类
 *
 * 废弃 使用抛异常的方式
 *
 * @author zhuquanwen
 * @vesion 1.0
 * @date 2018/7/17 17:33
 * @since jdk1.8
 */
@Deprecated
public class OutputUtils {
    private static Gson gson = new Gson();
    private OutputUtils(){}

    public static void output(HttpServletResponse response, int status, String msg, String desc) throws IOException {
        response.setContentType("application/json; charset=utf-8");
        response.setCharacterEncoding("UTF-8");
        response.setStatus(status);
        ServletOutputStream pw = response.getOutputStream();
        ResponseEntity responseEntity = new ResponseEntity(status,msg);
        responseEntity.setDesc(desc);
        pw.write(gson.toJson(responseEntity).getBytes("UTF-8"));
        pw.flush();
//        pw.close();
    }

    public static void output(HttpServletResponse response, int status, ResponseEntity responseEntity) throws IOException {
        response.setContentType("application/json; charset=utf-8");
        response.setCharacterEncoding("UTF-8");
        response.setStatus(status);
        ServletOutputStream pw = response.getOutputStream();
        pw.write(gson.toJson(responseEntity).getBytes("UTF-8"));
        pw.flush();
//        pw.close();
    }
}
