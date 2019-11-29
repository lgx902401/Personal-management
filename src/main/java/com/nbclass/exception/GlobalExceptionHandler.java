package com.nbclass.exception;

import com.alibaba.fastjson.JSONObject;
import com.nbclass.enums.ResponseStatus;
import io.swagger.annotations.Api;
import org.apache.commons.lang.StringUtils;
import org.apache.shiro.authz.AuthorizationException;
import org.apache.shiro.authz.UnauthenticatedException;
import org.apache.shiro.authz.UnauthorizedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

/**
 * @version V1.0
 * @date 2018年7月13日
 * @author superzheng
 */
@Api(tags = "异常处理")
@ResponseBody
@ControllerAdvice
public class GlobalExceptionHandler {
    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    /**
     * 统一业务异常处理
     * @param e
     * @param request
     * @return
     */
    @ExceptionHandler(ZbException.class)
    public JSONObject defaultErrorHandler(Exception e, HttpServletRequest request) {
//        request.setAttribute("javax.servlet.error.status_code",ResponseStatus.ERROR.getCode());
        JSONObject jsonObject = new JSONObject();
//        Map<String,Object> jsonMap = new HashMap<>(2);
        jsonObject.put("status", ResponseStatus.ERROR.getCode());
        jsonObject.put("msg", StringUtils.isNotBlank(e.getMessage())? e.getMessage() : ResponseStatus.ERROR.getMessage());
        logger.error(e.getMessage());
//        request.setAttribute("ext",map);
        return jsonObject;
    }
    /**
     * 权限不足报错拦截
     */
    @ExceptionHandler(UnauthorizedException.class)
    public JSONObject handleAuth() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("code",ResponseStatus.FORBIDDEN.getCode());
        jsonObject.put("msg",ResponseStatus.FORBIDDEN.getMessage());
        jsonObject.put("info",new JSONObject());
        return jsonObject;
    }

    /**
     * 未登录报错拦截
     * 在请求需要权限的接口,而连登录都还没登录的时候,会报此错
     */
    @ExceptionHandler(UnauthenticatedException.class)
    public JSONObject unauthenticatedException(){
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("code",ResponseStatus.OVERDUE.getCode());
        jsonObject.put("msg",ResponseStatus.OVERDUE.getMessage());
        jsonObject.put("info",new JSONObject());
        return jsonObject;
    }


}
