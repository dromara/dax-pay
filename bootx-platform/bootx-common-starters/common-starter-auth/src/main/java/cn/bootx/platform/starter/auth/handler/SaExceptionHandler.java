package cn.bootx.platform.starter.auth.handler;

import cn.bootx.platform.common.core.code.CommonCode;
import cn.bootx.platform.common.core.rest.Res;
import cn.bootx.platform.common.core.rest.ResResult;
import cn.dev33.satoken.exception.SaTokenException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
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
     * sa鉴权业务异常
     */
    @ExceptionHandler(SaTokenException.class)
    public ResResult<Void> handleBusinessException(SaTokenException ex) {
        log.info(ex.getMessage(), ex);
        return Res.response(CommonCode.FAIL_CODE, ex.getMessage());
    }

}
