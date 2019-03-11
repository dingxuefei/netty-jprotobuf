package com.iscas.base.biz.controller.common;


import com.iscas.base.biz.config.Constants;
import com.iscas.base.biz.config.auth.TokenProps;
import com.iscas.base.biz.service.AbstractAuthService;
import com.iscas.base.biz.util.LoginCacheUtils;
import com.iscas.common.tools.random.RandomStringUtils;
import com.iscas.templet.common.ResponseEntity;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

/**
 * 登陆控制器
 *
 * @author zhuquanwen
 * @vesion 1.0
 * @date 2018/7/16 22:38
 * @since jdk1.8
 */
@RestController
@Api(description = "登陆控制器")
public class LoginController extends BaseController implements Constants {


    @Autowired
    private TokenProps tokenProps;

    @Autowired
    private AbstractAuthService authService;

    @GetMapping(value = "/logout", produces = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    public ResponseEntity logout(HttpServletRequest request){
        ResponseEntity responseEntity = new ResponseEntity(200, "注销成功");
        authService.invalidToken(request);
        responseEntity.setValue("注销成功");
        return responseEntity;
    }

    @GetMapping(value = "/prelogin", produces = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    public ResponseEntity<Map> preLogin(){
        ResponseEntity<Map> responseEntity = new ResponseEntity<>();
        String data = RandomStringUtils.randomStr(16);
        String uuid = LoginCacheUtils.put(data);
        Map map = new HashMap();
        map.put("key", uuid);
        map.put("encryKey", data);
        responseEntity.setValue(map);
        return responseEntity;
    }

    @PostMapping(value = "/login", produces = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    public ResponseEntity login(HttpServletResponse response, @RequestBody Map<String,String> user) throws Exception {
        ResponseEntity responseEntity = new ResponseEntity();
        responseEntity.setMessage("登录成功");
        authService.loginHandler(response,  user,
                responseEntity, tokenProps.getExpire(), tokenProps.getCookieExpire());
        return responseEntity;
    }
}
