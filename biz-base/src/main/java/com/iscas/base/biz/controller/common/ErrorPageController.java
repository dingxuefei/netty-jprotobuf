package com.iscas.base.biz.controller.common;

import com.iscas.base.biz.exception.AuthorizationRuntimeException;
import com.iscas.base.biz.exception.BaseException;
import com.iscas.base.biz.util.SpringUtils;
import com.iscas.templet.common.ResponseEntity;
import io.swagger.annotations.Api;
import io.undertow.server.handlers.form.MultiPartParserDefinition;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
@Api(description = "全局异常处理")
public class ErrorPageController {
    @Value("${spring.servlet.multipart.max-file-size}")
    private String maxFileSize;
    @RequestMapping(value = "/401", produces = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ResponseEntity to401(){
        ResponseEntity responseEntity = new ResponseEntity();
//        responseEntity.setStatus(401);
        responseEntity.setMessage("未登录");
        return responseEntity;
    }

    @RequestMapping(value = "/403", produces = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ResponseEntity to403(){
        ResponseEntity responseEntity = new ResponseEntity();
//        responseEntity.setStatus(403);
        responseEntity.setMessage("没有权限");
        return responseEntity;
    }

    @RequestMapping(value = "/404", produces = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity to404(){
        ResponseEntity responseEntity = new ResponseEntity();
//        responseEntity.setStatus(404);
        responseEntity.setMessage("找不到资源");
        return responseEntity;
    }

    @RequestMapping(value = "/502", produces = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    @ResponseStatus(HttpStatus.BAD_GATEWAY)
    public ResponseEntity to502(){
        ResponseEntity responseEntity = new ResponseEntity();
//        responseEntity.setStatus(502);
        responseEntity.setMessage("网关错误");
        return responseEntity;
    }
    @RequestMapping(value = "/400", produces = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity to400(){
        ResponseEntity responseEntity = new ResponseEntity();
//        responseEntity.setStatus(400);
        responseEntity.setMessage("请求无效");
        return responseEntity;
    }
    @RequestMapping(value = "/500", produces = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseEntity to500() throws Exception {

        HttpServletRequest request = SpringUtils.getRequest();
        Object attribute = request.getAttribute("javax.servlet.error.exception");
        //如果是文件太大了异常 抛处错误给前台
        if(attribute != null && attribute instanceof IllegalStateException ){
            IllegalStateException exception = (IllegalStateException) attribute;
            Throwable cause = exception.getCause();
            //TODO 这里耦合的Undertow的包，如果以后替为tomcat这段要删除或作其他处理
            if(cause != null && cause instanceof MultiPartParserDefinition.FileTooLargeException){
                throw new BaseException("文件大小超过限制，最大限制" + maxFileSize, ((Exception)attribute).getMessage());
            }
        } else if (attribute != null && attribute instanceof AuthorizationRuntimeException) {
            throw (AuthorizationRuntimeException) attribute;
        }
        throw (Exception) attribute;
    }

}
