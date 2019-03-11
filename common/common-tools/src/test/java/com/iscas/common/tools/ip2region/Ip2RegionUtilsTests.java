package com.iscas.common.tools.ip2region;

import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.lionsoul.ip2region.DbMakerConfigException;

import java.io.FileNotFoundException;
import java.lang.reflect.InvocationTargetException;

/**
 * //TODO
 *
 * @author zhuquanwen
 * @vesion 1.0
 * @date 2018/9/29 17:45
 * @since jdk1.8
 */
@RunWith(JUnit4.class)
public class Ip2RegionUtilsTests {
    @Test
    @Ignore
    public  void test() throws InvocationTargetException, FileNotFoundException, IllegalAccessException, DbMakerConfigException, NoSuchMethodException {
        String cityInfo = Ip2RegionUtils.getCityInfo("220.248.12.158", "H:\\ideaProjects\\stc-pub\\common\\common-tools\\src\\main\\java\\com\\iscas\\common\\tools\\ip2region\\ip2region.db");
        System.out.println(cityInfo);
    }
}
