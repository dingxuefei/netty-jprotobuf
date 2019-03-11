package com.iscas.base.biz.model.auth;

import lombok.Getter;
import lombok.Setter;

import java.security.Principal;
import java.util.List;
@Getter
@Setter
public class User implements Principal {

    private String username;

    private String password;

    private String role;

    private List<Url> urls;

    @Override
    public String getName() {
        return username;
    }
}