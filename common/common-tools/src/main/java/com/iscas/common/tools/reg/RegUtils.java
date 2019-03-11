package com.iscas.common.tools.reg;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * //TODO
 *
 * @author zhuquanwen
 * @vesion 1.0
 * @date 2018/9/6 19:15
 * @since jdk1.8
 */
public class RegUtils {
    private RegUtils(){

    }

    public static boolean match(String regex, String str) {
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(str);
        return matcher.matches();
    }

}
