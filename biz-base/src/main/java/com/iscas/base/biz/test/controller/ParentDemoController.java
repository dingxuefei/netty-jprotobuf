package com.iscas.base.biz.test.controller;

import com.iscas.base.biz.controller.common.BaseController;
import com.iscas.base.biz.test.model.Parent;
import com.iscas.base.biz.test.service.IParentService;
import com.iscas.templet.common.ResponseEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 *
 *
 * @author zhuquanwen
 * @vesion 1.0
 * @date 2018/7/16
 * @since jdk1.8
 */
@RestController
@RequestMapping("/parent")
public class ParentDemoController extends BaseController {
    @Autowired
    private IParentService parentService;
    @GetMapping("/findAll")
    public ResponseEntity findAll(){
        ResponseEntity responseEntity = getResponse();
        responseEntity.setValue(parentService.findCascadeAll());
        return responseEntity;
    }

    @PutMapping()
    public ResponseEntity insert(@RequestBody Parent parent){
        ResponseEntity responseEntity = getResponse();
        parentService.save(parent);
        responseEntity.setValue(parent.getId());
        return responseEntity;
    }

    @PostMapping("/findAllByCondition")
    public ResponseEntity findAllByCondition(@RequestBody Parent parent){
        ResponseEntity responseEntity = getResponse();
        responseEntity.setValue(parentService.findCascadeAllByCondition(parent));
        return responseEntity;
    }

}
