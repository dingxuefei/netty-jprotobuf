package com.iscas.common.tools.array;

/**
 * 数组增强操作
 *
 * @author zhuquanwen
 * @vesion 1.0
 * @date 2018/7/18 15:50
 * @since jdk1.8
 */
public class ArrayRaiseUtils {
    private ArrayRaiseUtils() {
    }

    /**
     * 判断一个数组中是否含有某个元素，使用equals方法判断
     *
     * @version 1.0
     * @since jdk1.8
     * @date 2018/7/18
     * @param objects 数组
     * @param obj 判断元素
     * @return boolean
     */
    public static boolean contains(Object[] objects, Object obj) {

        if (objects == null) {
            return false;
        }
        if (obj == null) {
            return false;
        }
        for (Object objx: objects) {
            if (objx.equals(obj)) {
                return true;
            }
        }
        return false;
    }
}
