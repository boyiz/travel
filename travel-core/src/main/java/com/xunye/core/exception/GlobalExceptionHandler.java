package com.xunye.core.exception;

import javax.servlet.http.HttpServletRequest;

import cn.dev33.satoken.exception.NotLoginException;
import cn.dev33.satoken.exception.NotPermissionException;
import cn.dev33.satoken.exception.NotRoleException;
import com.xunye.core.result.R;
import com.xunye.core.result.RStateEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(AccessDeniedException.class)
    public R<String> handleAuthorizationException(AccessDeniedException e) {
        log.error(e.getMessage());
        return R.failure(RStateEnum.UNAUTHORIZED.getCode(), RStateEnum.UNAUTHORIZED.getMsg(), e.getMessage());
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public R<String> handleMismatchedInputException(HttpMessageNotReadableException e) {
        log.error(e.getMessage());
        return R.failure(RStateEnum.PARAMS_ERROR.getCode(), RStateEnum.PARAMS_ERROR.getMsg(), e.getMessage());
    }

    // 重新处理Sa-token 的 权限校验 异常处理，sa-token 信息过于详细
    @ExceptionHandler(NotPermissionException.class)
    public R<String> handleAuthorizationException(NotPermissionException e) {
        log.error(e.getMessage());
        return R.failure(RStateEnum.UNAUTHORIZED.getCode(), RStateEnum.UNAUTHORIZED.getMsg());
    }

    // 重新处理Sa-token 的 角色校验 异常处理，sa-token 信息过于详细
    @ExceptionHandler(NotRoleException.class)
    public R<String> handleAuthorizationException(NotRoleException e) {
        log.error(e.getMessage());
        return R.failure(RStateEnum.UNAUTHORIZED.getCode(), RStateEnum.UNAUTHORIZED.getMsg());
    }
    // Sa-token 的未登录异常处理
    @ExceptionHandler(NotLoginException.class)
    public R<String> handleAuthorizationException(NotLoginException e) {
        log.error(e.getMessage());
        return R.failure(RStateEnum.FORBIDDEN.getCode(), RStateEnum.FORBIDDEN.getMsg());
    }

    @ExceptionHandler(Exception.class)
    @Transactional(rollbackFor = Exception.class)
    public R<String> handleException(Exception e, HttpServletRequest request) {
        log.error("系统内部错误", e);

        /* 捕获的全局异常，进行事务回滚(PS:主要针对与controller层发生的异常，事务管理器未捕获到的) */
        // controller方法必须有@Transactional(rollbackFor = Exception.class)注解才生效
        TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();

        if (e instanceof IllegalArgumentException) {
            return R.failure(RStateEnum.PARAMS_ERROR.getCode(), RStateEnum.PARAMS_ERROR.getMsg(), e.getMessage());
        }
        // 业务异常
        else if (e instanceof BusinessException) {
            return R.failure(RStateEnum.BUSINESS_ERROR.getCode(), RStateEnum.BUSINESS_ERROR.getMsg(), e.getMessage());
        }

        return R.failure(RStateEnum.SYSTEM_ERROR.getCode(), RStateEnum.SYSTEM_ERROR.getMsg(), e.getMessage());
    }

}
