package com.iscas.base.biz.aop.associate;

/**
 * //TODO
 *
 * @author zhuquanwen
 * @vesion 1.0
 * @date 2018/9/4 14:53
 * @since jdk1.8
 */
public class AssoThreadLocal {
    private AssoThreadLocal() {

    }
    public static final ThreadLocal<CustomAssociates> ASSOCIATES_THREAD_LOCAL = new ThreadLocal<>();
}
