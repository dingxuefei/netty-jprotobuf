package com.iscas.base.biz.model.auth;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * 角色信息
 *
 * @author zhuquanwen
 * @vesion 1.0
 * @date 2018/7/16 18:43
 * @since jdk1.8
 */
@Data
public class Role {
    private String key;
    private String name;
    private List<Url> urls = new ArrayList<>();
    private List<Menu> menus = new ArrayList<>();

    public void addUrl(Url url){
        urls.add(url);
    }
    public void addMenu(Menu menu){
        menus.add(menu);
    }
}
