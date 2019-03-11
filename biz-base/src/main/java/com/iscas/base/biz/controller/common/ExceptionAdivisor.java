package com.iscas.base.biz.controller.common;

import com.iscas.base.biz.exception.AuthorizationException;
import com.iscas.base.biz.exception.BaseException;
import com.iscas.base.biz.exception.AuthorizationRuntimeException;
import com.iscas.base.biz.util.SpringUtils;
import com.iscas.base.biz.config.StaticInfo;
import com.iscas.base.biz.config.cros.CrosProps;
import com.iscas.base.biz.exception.LoginException;
import com.iscas.common.tools.random.RandomStringUtils;
import com.iscas.templet.common.ResponseEntity;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;

import org.springframework.stereotype.Component;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.multipart.MaxUploadSizeExceededException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Path;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import static com.iscas.base.biz.config.Constants.SESSION_LOGIN_KEY;

@RestControllerAdvice
@Component
@Slf4j
//@CrossOrigin
public class ExceptionAdivisor {
    @Autowired
    private CrosProps crosProps;

    private void setResponseInfo(ResponseEntity responseEntity){
        HttpServletRequest request = SpringUtils.getRequest();
        Long start = null;
        try {
            start = StaticInfo.START_TIME_THREAD_LOCAL.get();
        } finally {
            StaticInfo.START_TIME_THREAD_LOCAL.remove();
        }
        if (Objects.nonNull(start)) {
            responseEntity.setTookInMillis(System.currentTimeMillis() - start);
        }
        if(request != null){
            String requestURI = request.getRequestURI();
            responseEntity.setRequestURL(requestURI);
        }
    }

    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity to400(MethodArgumentNotValidException e){
        ResponseEntity responseEntity = new ResponseEntity(HttpStatus.BAD_REQUEST.value(), "请求参数校验失败");
        StringBuilder result = new StringBuilder();
        result.append("error 400 :");
        BindingResult bindingResult = e.getBindingResult();
        List<FieldError> errors = bindingResult.getFieldErrors();
        if(errors != null){
            for (FieldError error: errors) {
                String field = error.getField();
                String msg = error.getDefaultMessage();
                // 这里可以使用field 和msg 组合成返回的内容，这里就是做一个拼接
                result.append(field).append(",").append(msg).append(";");
            }
        }
        responseEntity.setDesc(result.toString());
        log.error("请求参数有误",e);
        setResponseCros();
        setResponseInfo(responseEntity);
        return responseEntity;
    }

    @ExceptionHandler(value = ConstraintViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity to400(ConstraintViolationException e){
        ResponseEntity responseEntity = new ResponseEntity(HttpStatus.BAD_REQUEST.value(), "请求参数校验失败");
        StringBuilder result = new StringBuilder();
        result.append("error 400 :");
        Set<ConstraintViolation<?>> cvs = e.getConstraintViolations();
        if(cvs != null){
            for (ConstraintViolation cv: cvs) {
                Path path = cv.getPropertyPath();
                String msg = cv.getMessage();
                result.append(path).append(",").append(msg).append(";");

            }
        }
        responseEntity.setDesc(result.toString());
        log.error("请求参数有误",e);
        setResponseCros();
        setResponseInfo(responseEntity);
        return responseEntity;
    }

    @ExceptionHandler(value = LoginException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ResponseEntity loginException(LoginException e){
        ResponseEntity responseEntity = new ResponseEntity(HttpStatus.UNAUTHORIZED.value(), e.getMessage());
        responseEntity.setDesc(e.getMsgDetail() != null ? e.getMsgDetail() : getMessage(e));
        responseEntity.setMessage(e.getMessage());
        HttpSession session = SpringUtils.getSession();
        String data = RandomStringUtils.randomStr(16);
        responseEntity.setValue(data);
        session.setAttribute(SESSION_LOGIN_KEY, data);
        log.error("异常", e);
        setResponseCros();
        setResponseInfo(responseEntity);
        return responseEntity;
    }

    @ExceptionHandler(value = AuthorizationRuntimeException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ResponseEntity loginRuntimeException(AuthorizationRuntimeException e){
        ResponseEntity responseEntity = new ResponseEntity(HttpStatus.UNAUTHORIZED.value(), e.getMessage());
        responseEntity.setDesc(e.getMsgDetail() != null ? e.getMsgDetail() : getMessage(e));
        responseEntity.setMessage(e.getMessage());
        HttpSession session = SpringUtils.getSession();
        String data = RandomStringUtils.randomStr(16);
        responseEntity.setValue(data);
        session.setAttribute(SESSION_LOGIN_KEY, data);
        log.error("异常", e);
        setResponseCros();
        setResponseInfo(responseEntity);
        return responseEntity;
    }

    @ExceptionHandler(value = AuthorizationException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ResponseEntity to403Exception(AuthorizationException e){
        ResponseEntity responseEntity = new ResponseEntity(HttpStatus.FORBIDDEN.value(), e.getMessage());
        responseEntity.setDesc(e.getMsgDetail() != null ? e.getMsgDetail() : getMessage(e));
        log.error("异常", e);
        setResponseCros();
        setResponseInfo(responseEntity);
        return responseEntity;
    }


    @ExceptionHandler(value = BaseException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseEntity to500(BaseException e){
        ResponseEntity responseEntity = new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR.value(), e.getMessage());
        responseEntity.setDesc(e.getMsgDetail() != null ? e.getMsgDetail() : getMessage(e));
        responseEntity.setMessage(e.getMessage());
        log.error("异常", e);
        setResponseCros();
        setResponseInfo(responseEntity);
        return responseEntity;
    }
    @ExceptionHandler(MaxUploadSizeExceededException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseEntity to500(MaxUploadSizeExceededException e){
        ResponseEntity responseEntity = new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR.value(), "上传文件大小超过限制");
        responseEntity.setDesc(getMessage(e));
        responseEntity.setMessage("文件上传大小超过限制");
        log.error("异常", e);
        setResponseCros();
        setResponseInfo(responseEntity);
        return responseEntity;
    }

    @ExceptionHandler(value = Throwable.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseEntity to500(Throwable throwable){
        ResponseEntity responseEntity = new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR.value(), "服务器内部错误");
        responseEntity.setDesc(getMessage(throwable));
        log.error("异常", throwable);
        setResponseCros();
        setResponseInfo(responseEntity);
        return responseEntity;
    }

    @SuppressWarnings("AlibabaUndefineMagicConstant")
    private void setResponseCros(){
        HttpServletRequest request = SpringUtils.getRequest();
        HttpServletResponse response = SpringUtils.getResponse();
        String origin = request.getHeader("Origin");
        if (origin == null || "null".equals(origin)) {
            origin = crosProps.getOrigin();
        }
        response.setHeader("Access-Control-Allow-Origin", origin);
        response.setHeader("Access-Control-Allow-Credentials", crosProps.getCredentials());//服务器同意客户端发送cookies
        response.setHeader("Access-Control-Allow-Methods", crosProps.getMethods());
        response.setHeader("Access-Control-Allow-Headers", crosProps.getHeaders());

    }

    /**
     * 递归获取异常的message
     * */
    private String getMessage(Throwable throwable){
        String message = null;
        if(throwable != null){
            String message1 = throwable.getMessage();
            if(message1 != null){
                return message1;
            }else{
                Throwable cause = throwable.getCause();
                return getMessage(cause);
            }
        }
        return message;
    }


}
