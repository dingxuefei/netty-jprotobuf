package com.iscas.base.biz.test.controller;


import com.iscas.base.biz.aop.auth.RequiredRole;
import com.iscas.base.biz.controller.common.BaseController;
import com.iscas.templet.common.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 权限验证过滤器
 *
 * @author zhuquanwen
 * @vesion 1.0
 * @date 2018/7/17 8:35
 * @since jdk1.8
 */
@RestController
@RequestMapping("/testauth")
public class AuthDemoController extends BaseController {
    @GetMapping("/t1")
    public ResponseEntity t1() {
        return getResponse();
    }
    @GetMapping("/t2")
    public ResponseEntity t2() {
        return getResponse();
    }
    @GetMapping("/t3")
    public ResponseEntity t3() {
        return getResponse();
    }

    @RequiredRole(value = {"normal"})
    @GetMapping("/t4")
    public ResponseEntity t4() {
        return getResponse();
    }
}
