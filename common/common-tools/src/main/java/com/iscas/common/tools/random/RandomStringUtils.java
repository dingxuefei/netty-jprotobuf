package com.iscas.common.tools.random;

import java.util.Random;

/**
 * 随机字符串工具类
 * @author zhuquanwen
 * @date 2018/7/13 18:01
 **/
public final class RandomStringUtils {
    private RandomStringUtils(){}

    /**
     *获得随机字母数字字符串
     *@date 2018/7/13 18:02
     *@param length 随机串长度
     *@return {@link java.lang.String}
     */
    public static String randomStr(int length){

        StringBuilder sb = new StringBuilder();
        Random random = new Random();
        for (int i =0 ; i< length; i++){
            //先选择从数字，大写,小写中拿？
            int r = random.nextInt(3);
            int charInfo = 48;
            if(r == 0){
                //从数字中拿一个
                charInfo = 48 + random.nextInt(10);
            }else if(r == 1){
                //从大写字母中拿一个
                charInfo = 65 + random.nextInt(26);
            }else if(r == 2){
                //从小写字母中拿一个
                charInfo = 97 + random.nextInt(26);
            }
            sb.append((char)charInfo);
        }
        return sb.toString();
    }
}
