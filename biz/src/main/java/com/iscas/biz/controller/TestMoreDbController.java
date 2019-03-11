package com.iscas.biz.controller;


import com.iscas.base.biz.controller.common.BaseController;
import com.iscas.biz.model.User;
import com.iscas.biz.service.mysql.DynamicMysqlService;
import com.iscas.biz.service.mysql.UserService;
import com.iscas.biz.service.sqlite.DynamicSqlliteService;
import com.iscas.templet.common.ResponseEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author ${author}
 * @since 2018-10-12
 */
@RestController
@RequestMapping("/db")
public class TestMoreDbController extends BaseController {
    /**
     *
     * 多数据源访问测试
     *
     *
     * */


    //测试注入com.iscas.biz.service.mysql下的一个service，访问mysql
    @Autowired
    private UserService userService;

    //测试注入com.iscas.biz.service.sqllite下的一个service，访问sqllite
    @Autowired
    private DynamicSqlliteService dynamicSqlliteService;

    @Autowired
    private DynamicMysqlService dynamicMysqlService;


    /**测试使用sqllite数据源*/
    @GetMapping("/t1")
    public ResponseEntity t1() {
        ResponseEntity response = getResponse();
        List<User> list = userService.list(null);
        response.setValue(list);
        return response;
    }


    /**测试使用sqlite数据源 使用动态查询方式*/
    @GetMapping("/t2")
    public ResponseEntity t2() {
        ResponseEntity response = getResponse();
        String sql = "select * from EnumSensorGeneration limit 1,50";
        List<Map> list = dynamicSqlliteService.dynamicExecute(sql);
        response.setValue(list);
        return response;
    }

    /**测试使用mysql数据源 使用动态查询方式*/
    @GetMapping("/t3")
    public ResponseEntity t3() {
        ResponseEntity response = getResponse();
        String sql = "select * from user";
        List<Map> list = dynamicMysqlService.dynamicExecute(sql);
        response.setValue(list);
        return response;
    }
}

