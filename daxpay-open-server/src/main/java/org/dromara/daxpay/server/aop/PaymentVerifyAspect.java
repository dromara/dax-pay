package org.dromara.daxpay.server.aop;

import cn.bootx.platform.core.code.CommonCode;
import cn.bootx.platform.core.exception.BizException;
import cn.bootx.platform.core.exception.ValidationFailedException;
import cn.bootx.platform.core.util.ValidationUtil;
import org.dromara.daxpay.core.param.PaymentCommonParam;
import org.dromara.daxpay.core.result.DaxResult;
import org.dromara.daxpay.service.pay.common.anno.PaymentVerify;
import org.dromara.daxpay.service.pay.service.assist.PaymentAssistService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.MDC;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

/**
 * 支付签名切面, 用于对支付参数进行校验和签名
 * 执行顺序: 过滤器 -> 拦截器 -> 切面 -> 方法
 * @author xxm
 * @since 2023/12/24
 */
@Aspect
@Slf4j
@Component
@Order
@RequiredArgsConstructor
public class PaymentVerifyAspect {
    private final PaymentAssistService paymentAssistService;

    /**
     * 切面处理
     */
    @Around("@annotation(paymentVerify)||@within(paymentVerify)")
    public Object methodPoint(ProceedingJoinPoint pjp, PaymentVerify paymentVerify) throws Throwable {
        Object[] args = pjp.getArgs();
        if (args.length == 0){
            throw new ValidationFailedException("支付方法至少有一个参数，并且需要签名的支付参数放在第一位");
        }
        Object param = args[0];
        if (param instanceof PaymentCommonParam paymentParam){
            // 参数校验
            ValidationUtil.validateParam(paymentParam);
            // 商户和应用信息初始化
            paymentAssistService.initMchAndApp(paymentParam.getMchNo(), paymentParam.getAppId());
            // 状态判断
            paymentAssistService.checkStatus();
            // 终端信息初始化
            paymentAssistService.initClient(paymentParam);
            // 参数签名校验
            paymentAssistService.signVerify(paymentParam);
            // 参数请求时间校验
            paymentAssistService.reqTimeoutVerify(paymentParam);
        } else {
            throw new ValidationFailedException("参数需要继承PayCommonParam");
        }
        Object proceed;
        try {
            proceed = pjp.proceed();
        } catch (BizException ex) {
            DaxResult<Void> result = new DaxResult<>(ex.getCode(), ex.getMessage());
            paymentAssistService.sign(result);
            return result;
        }
        // 对返回值添加追踪ID/响应时间并进行签名
        if (proceed instanceof DaxResult<?> result){
            result.setTraceId(MDC.get(CommonCode.TRACE_ID));
            result.setResTime(LocalDateTime.now());
            paymentAssistService.sign(result);
        } else {
            throw new ValidationFailedException("支付方法返回类型需要为 DaxResult 类型的对象");
        }
        return proceed;
    }

}
