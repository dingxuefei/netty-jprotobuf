package com.iscas.base.biz.controller.common;

import com.iscas.base.biz.service.common.MpGenerator;
import com.iscas.templet.common.ResponseEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * //TODO
 *
 * @author zhuquanwen
 * @vesion 1.0
 * @date 2018/10/11 16:59
 * @since jdk1.8
 */
@RestController
@RequestMapping("/mp")
public class MpGeneratorController extends BaseController{
    @Autowired
    private MpGenerator mpGenerator;
    @GetMapping("/generator")
    public ResponseEntity generator(){
        mpGenerator.generator();
        return getResponse();
    }
}
