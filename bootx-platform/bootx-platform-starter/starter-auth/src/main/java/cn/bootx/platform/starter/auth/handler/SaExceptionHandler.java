package cn.bootx.platform.starter.auth.handler;

import cn.bootx.platform.core.code.CommonCode;
import cn.bootx.platform.core.rest.Res;
import cn.bootx.platform.core.rest.result.Result;
import cn.bootx.platform.starter.auth.exception.NotLoginException;
import cn.bootx.platform.starter.auth.exception.RouterCheckException;
import cn.dev33.satoken.exception.SaTokenException;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * 过滤SaTokenException,需要运行在 RestExceptionHandler 之前
 *
 * @author xxm
 * @since 2021/8/5
 */
@Order(Ordered.LOWEST_PRECEDENCE - 1)
@Slf4j
@RestControllerAdvice
public class SaExceptionHandler {

    /**
     * 未登录返回401
     */
    @ExceptionHandler(NotLoginException.class)
    public ResponseEntity<Result<Void>> handleNotLoginException(NotLoginException ex){
        log.info(ex.getMessage(), ex);
        Result<Void> result = Res.response(ex.getCode(), ex.getMessage(), MDC.get(CommonCode.TRACE_ID));
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(result);
    }

    /**
     * 路径无权访问
     */
    @ExceptionHandler(RouterCheckException.class)
    public ResponseEntity<Result<Void>> handleBusinessException(RouterCheckException ex) {
        log.info(ex.getMessage(), ex);
        Result<Void> result = Res.response(ex.getCode(), ex.getMessage(), MDC.get(CommonCode.TRACE_ID));
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(result);
    }

    /**
     * sa鉴权业务异常
     */
    @ExceptionHandler(SaTokenException.class)
    public Result<Void> handleBusinessException(SaTokenException ex) {
        log.info(ex.getMessage(), ex);
        return Res.response(CommonCode.FAIL_CODE, ex.getMessage());
    }

}
