/**
 * Copyright (c) 2018 人人开源 All rights reserved.
 * <p>
 * https://www.renren.io
 * <p>
 * 版权所有，侵权必究！
 */

package com.whaleal.common.exception;

import cn.hutool.core.map.MapUtil;
import com.whaleal.common.utils.IpUtils;
import com.whaleal.common.utils.JsonUtils;
import com.whaleal.common.utils.Result;
import com.whaleal.modules.log.entity.SysLogErrorEntity;
import com.whaleal.modules.log.service.SysLogErrorService;
import com.whaleal.common.utils.HttpContextUtils;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.UnauthorizedException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Map;


/**
 * 异常处理器
 *
 * @author Mark sunlightcs@gmail.com
 * @since 1.0.0
 */
@Slf4j
@RestControllerAdvice
@AllArgsConstructor
public class RenExceptionHandler {
    private final SysLogErrorService sysLogErrorService;

    /**
     * 处理自定义异常
     */
    @ExceptionHandler(RenException.class)
    public Result handleRenException(RenException ex) {
        Result result = new Result();
        result.error(ex.getCode(), ex.getMsg());

        return result;
    }

    @ExceptionHandler(UnauthorizedException.class)
    public Result handleUnAuthException(UnauthorizedException ex) {
        Result result = new Result();
        result.error(403, ex.getMessage());

        return result;
    }

    @ExceptionHandler(OrderException.class)
    public Result handleOrderException(RenException ex) {
        Result result = new Result();
        result.error(ex.getCode(), ex.getMsg());

        return result;
    }

    @ExceptionHandler(DuplicateKeyException.class)
    public Result handleDuplicateKeyException(DuplicateKeyException ex) {
        Result result = new Result();
        result.error(ErrorCode.DB_RECORD_EXISTS);

        return result;
    }

    @ExceptionHandler(Exception.class)
    public Result handleException(Exception ex) {
        log.error(ex.getMessage(), ex);

        saveLog(ex);

        return new Result().error();
    }

    /**
     * 保存异常日志
     */
    private void saveLog(Exception ex) {
        SysLogErrorEntity log = new SysLogErrorEntity();

        //请求相关信息
        HttpServletRequest request = HttpContextUtils.getHttpServletRequest();
        log.setIp(IpUtils.getIpAddr(request));
        log.setUserAgent(request.getHeader(HttpHeaders.USER_AGENT));
        log.setRequestUri(request.getRequestURI());
        log.setRequestMethod(request.getMethod());
        Map<String, String> params = HttpContextUtils.getParameterMap(request);
        if (MapUtil.isNotEmpty(params)) {
            log.setRequestParams(JsonUtils.toJsonString(params));
        }

        //异常信息
        log.setErrorInfo(ExceptionUtils.getErrorStackTrace(ex));

        //保存
        sysLogErrorService.save(log);
    }
}
