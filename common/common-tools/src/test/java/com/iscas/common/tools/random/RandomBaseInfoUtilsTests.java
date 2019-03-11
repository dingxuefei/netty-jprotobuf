package com.iscas.common.tools.random;

import org.junit.Test;

import java.util.Map;
import java.util.Random;

/**
 * //TODO
 *
 * @author zhuquanwen
 * @vesion 1.0
 * @date 2018/10/8 17:47
 * @since jdk1.8
 */
public class RandomBaseInfoUtilsTests {
    @Test
    public void test(){
        Map baseInfo = RandomBaseInfoUtils.getBaseInfo();
        System.out.println(baseInfo);
        String chineseName = RandomBaseInfoUtils.getChineseName();
        String email = RandomBaseInfoUtils.getEmail();
        String idCard = RandomBaseInfoUtils.getIdCard();
        String ip = RandomBaseInfoUtils.getIp();
        String road = RandomBaseInfoUtils.getRoad();
        String sex = RandomBaseInfoUtils.getSex();
        String tel = RandomBaseInfoUtils.getTel();
        System.out.println(chineseName);
        System.out.println(email);
        System.out.println(idCard);
        System.out.println(ip);
        System.out.println(road);
        System.out.println(sex);
        System.out.println(tel);
    }
}
