package com.iscas.common.tools.pinyin;

import cn.hutool.core.util.PinyinUtil;
import net.sourceforge.pinyin4j.format.exception.BadHanyuPinyinOutputFormatCombination;
import org.junit.Test;

/**
 * //TODO
 *
 * @author zhuquanwen
 * @vesion 1.0
 * @date 2018/10/9 17:23
 * @since jdk1.8
 */
public class PinyinUtilsTests {
    @Test
    public void test1() throws BadHanyuPinyinOutputFormatCombination {
        //Hutool实现
        String sun = PinyinUtil.getPinYin("太阳");
        System.out.println("太阳====" + sun);

        //Pinyin4J实现
        System.out.println("美丽的中国====" + PinyinUtils.toPinYin("美丽的中国", "", PinyinUtils.Type.LOWERCASE));
    }
}
