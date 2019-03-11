package com.iscas.common.tools.file;

import org.junit.Ignore;
import org.junit.Test;

/**
 * 图片生成缩略图工具类测试
 *
 * @author zhuquanwen
 * @vesion 1.0
 * @date 2018/7/14 18:10
 * @since jdk1.8
 */
public class ThumbnailPicUtilsTests {
    @Test
    @Ignore
    public void thumbnailTest() throws Exception {
        String oriPath = "c:/1.jpg";
        String outPath = "d:/1.jpg";
        ThumbnailPicUtils.transform(oriPath, outPath, 20,20);
    }
}
