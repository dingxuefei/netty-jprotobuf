package com.iscas.base.biz.test.controller;

import com.iscas.base.biz.controller.common.BaseController;
import com.iscas.base.biz.test.service.impl.MapResultService;
import com.iscas.base.biz.controller.common.BaseController;
import com.iscas.base.biz.test.service.impl.MapResultService;
import com.iscas.templet.common.ResponseEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * MapResult 测试控制器
 * */
@RestController
@RequestMapping("/map")
public class MapResultDemoController extends BaseController {
    @Autowired
    private MapResultService mapResultService;
    @GetMapping("/findAll")
    public ResponseEntity findAll(){
        ResponseEntity responseEntity = getResponse();
        responseEntity.setValue(mapResultService.selectAll());
        return responseEntity;
    }

    @GetMapping("/findById")
    public ResponseEntity findById(Integer id){
        ResponseEntity responseEntity = getResponse();
        responseEntity.setValue(mapResultService.selectById(id));
        return responseEntity;
    }

}
