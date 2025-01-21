package org.dromara.daxpay.service.aop;

import cn.bootx.platform.core.exception.BizException;
import cn.bootx.platform.core.exception.ValidationFailedException;
import cn.bootx.platform.core.util.ValidationUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.dromara.daxpay.core.param.PaymentCommonParam;
import org.dromara.daxpay.core.result.DaxResult;
import org.dromara.daxpay.service.service.assist.PaymentAssistService;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

/**
 * 支付签名切面, 用于对支付参数进行校验和签名
 * 执行顺序: 过滤器 -> 拦截器 -> 切面 -> 方法
 * @author xxm
 * @since 2023/12/24
 */
@Aspect
@Slf4j
@Component
@Order()
@RequiredArgsConstructor
public class PaymentVerifyAspect {
    private final PaymentAssistService paymentAssistService;

    /**
     * 切面处理
     */
    @Around("@annotation(org.dromara.daxpay.service.common.anno.PaymentVerify)||within(@org.dromara.daxpay.service.common.anno.PaymentVerify *)")
    public Object methodPoint(ProceedingJoinPoint pjp) throws Throwable {
        Object[] args = pjp.getArgs();
        if (args.length == 0){
            throw new ValidationFailedException("支付方法至少有一个参数，并且需要签名的支付参数放在第一位");
        }
        Object param = args[0];
        if (!(param instanceof PaymentCommonParam paymentParam)) {
            throw new ValidationFailedException("参数需要继承PayCommonParam");
        }
        Object proceed;
        // 应用信息初始化
        paymentAssistService.initMchApp(paymentParam.getAppId());
        try {
            // 参数签名校验
            paymentAssistService.signVerify(paymentParam);
            // 参数校验
            ValidationUtil.validateParam(paymentParam);
            // 终端信息初始化
            paymentAssistService.initClient(paymentParam);
            // 参数请求时间校验
            paymentAssistService.reqTimeoutVerify(paymentParam);

            proceed = pjp.proceed();
        } catch (BizException ex) {
            DaxResult<Void> result = new DaxResult<>(ex.getCode(), ex.getMessage());
            paymentAssistService.sign(result);
            return result;
        }
        // 对返回值进行签名
        if (proceed instanceof DaxResult<?> result){
            paymentAssistService.sign(result);
        } else {
            throw new ValidationFailedException("支付方法返回类型需要为 DaxResult 类型的对象");
        }
        return proceed;
    }

}
