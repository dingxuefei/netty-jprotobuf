package com.iscas.base.biz.config.db;

/**
 * //TODO
 *
 * @author zhuquanwen
 * @vesion 1.0
 * @date 2019/3/1 10:15
 * @since jdk1.8
 */
public enum DBTypeEnum {
    db1("db_mysql"), db2("db_sqllite");
    private String value;

    DBTypeEnum(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}

