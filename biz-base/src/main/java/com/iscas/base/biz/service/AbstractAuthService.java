package com.iscas.base.biz.service;

import com.auth0.jwt.interfaces.Claim;
import com.iscas.base.biz.config.Constants;
import com.iscas.base.biz.exception.AuthConfigException;
import com.iscas.base.biz.exception.LoginException;
import com.iscas.base.biz.exception.ValidTokenException;
import com.iscas.base.biz.model.auth.Role;
import com.iscas.base.biz.model.auth.Url;
import com.iscas.base.biz.util.CaffCacheUtils;
import com.iscas.base.biz.util.JWTUtils;
import com.iscas.common.web.tools.cookie.CookieUtils;
import com.iscas.templet.common.ResponseEntity;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

/**
 * 用户权限认证service
 *
 * @author zhuquanwen
 * @vesion 1.0
 * @date 2018/7/16 18:58
 * @since jdk1.8
 */
public abstract class AbstractAuthService implements Constants {
    public abstract Map<String, Role> getAuth() throws IOException, AuthConfigException;
    public abstract Map<String, Url> getUrls() throws IOException, AuthConfigException;
    public abstract String getRoles(String username);

    public abstract void loginHandler(HttpServletResponse response, Map<String,String> user,
                                      ResponseEntity responseEntity, int expire, int cookieExpire) throws LoginException;

    public void invalidToken(HttpServletRequest request) {
        String token = null;
        token = request.getHeader(TOKEN_KEY);
        if (token == null) {
            //尝试从cookie中拿author
            Cookie cookie = CookieUtils.getCookieByName(request, TOKEN_KEY);
            if (cookie != null) {
                token = cookie.getValue();
            }
        }
        CaffCacheUtils.remove(token);
        request.getSession().invalidate();
    }

    public String verifyToken(String token) throws ValidTokenException {
        try {
            Map<String, Claim> clainMap = JWTUtils.verifyToken(token);
            String username = clainMap.get("username").asString();
            if (username == null) {
                throw new ValidTokenException("token 校验失败");
            }
            return username;
        } catch (ValidTokenException e) {
            throw new ValidTokenException(e.getMessage(), e.getMsgDetail());
        } catch (Exception e) {
            throw new ValidTokenException("token 校验失败", e.getMessage());
        }

    }



}
