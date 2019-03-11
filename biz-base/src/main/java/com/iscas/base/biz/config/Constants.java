package com.iscas.base.biz.config;

/**
 * 常量定义类
 *
 * @author zhuquanwen
 * @vesion 1.0
 * @date 2018/7/16 18:47
 * @since jdk1.8
 */
public interface Constants {
    /**权限配置XML名称*/
    String AUTH_CONFIG_XML_NAME = "auth.xml";

    /**token key*/
    String TOKEN_KEY = "Authorization";

    /**SESSION_LOGIN_KEY*/
    String SESSION_LOGIN_KEY = "SESSION_LOGIN_KEY";

    /**用户信息*/
    String SESSION_USER = "SESSION_USER";

    /**序号的Key*/
    String NUM_KEY = "num";

    /**超级管理员角色*/
    String SUPER_ROLE_KEY = "super";

    /**管理员角色*/
    String MANAGER_ROLE_KEY = "manager";

    /**企业角色*/
    String APPLY_ROLE_KEY = "apply";

    /**Appliction session*/
    String APPLICATION_SESSION_KEY = "sessions";
}
