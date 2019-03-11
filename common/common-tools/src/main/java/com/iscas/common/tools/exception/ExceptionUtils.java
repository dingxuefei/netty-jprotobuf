package com.iscas.common.tools.exception;

import com.iscas.common.tools.string.StringUtils;

import java.io.PrintWriter;
import java.io.StringWriter;

/**
 * Created by LJian on 2017/4/14.
 */
public class ExceptionUtils {
    public static String getExceptionInfo(Exception e){
        try {
            String info = e.getMessage();
            if(StringUtils.isEmpty(info)) {
                StringWriter sw = new StringWriter();
                PrintWriter pw = new PrintWriter(sw);
                e.printStackTrace(pw);
                info =sw.toString();
                sw.close();
                pw.close();
            }
            return info.length() > 300 ? info.substring(0,300)+"...":info;
        }catch (Exception e2){
            e2.printStackTrace();
        }
        return "analyze Exception error";
    }
}
