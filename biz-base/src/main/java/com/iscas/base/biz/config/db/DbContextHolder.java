package com.iscas.base.biz.config.db;

/**
 * //TODO
 *
 * @author zhuquanwen
 * @vesion 1.0
 * @date 2019/3/1 10:15
 * @since jdk1.8
 */
public class DbContextHolder {
    private static final ThreadLocal contextHolder = new ThreadLocal<>();
    /**
     * 设置数据源
     * @param dbTypeEnum
     */
    public static void setDbType(DBTypeEnum dbTypeEnum) {
        contextHolder.set(dbTypeEnum.getValue());
    }

    /**
     * 取得当前数据源
     * @return
     */
    public static String getDbType() {
        return (String) contextHolder.get();
    }

    /**
     * 清除上下文数据
     */
    public static void clearDbType() {
        contextHolder.remove();
    }
}

