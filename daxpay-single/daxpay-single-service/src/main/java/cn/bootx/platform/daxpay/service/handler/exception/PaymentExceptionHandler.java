package cn.bootx.platform.daxpay.service.handler.exception;

import cn.bootx.platform.common.core.exception.BizException;
import cn.bootx.platform.common.core.rest.Res;
import cn.bootx.platform.common.core.rest.ResResult;
import cn.bootx.platform.daxpay.exception.pay.PayFailureException;
import cn.bootx.platform.daxpay.result.CommonResult;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
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
@Order(Ordered.LOWEST_PRECEDENCE - 2)
@Slf4j
@RestControllerAdvice
public class PaymentExceptionHandler {

    /**
     * 支付异常
     */
    @ExceptionHandler({PayFailureException.class})
    public ResResult<CommonResult> handleBusinessException(PayFailureException ex) {
        log.info(ex.getMessage(), ex);
        CommonResult commonResult = new CommonResult();
        commonResult.setMsg(ex.getMessage());
        return Res.ok(commonResult);
    }
}
