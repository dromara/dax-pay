package cn.daxpay.single.service.core.payment.common.aop;

import cn.bootx.platform.common.core.rest.ResResult;
import cn.bootx.platform.common.core.util.ValidationUtil;
import cn.daxpay.single.exception.pay.PayFailureException;
import cn.daxpay.single.param.PaymentCommonParam;
import cn.daxpay.single.result.PaymentCommonResult;
import cn.daxpay.single.service.annotation.PaymentVerify;
import cn.daxpay.single.service.core.payment.common.service.PaymentAssistService;
import cn.daxpay.single.util.DaxRes;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
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
public class PaymentVerifyAop {
    private final PaymentAssistService paymentAssistService;

    @Around("@annotation(paymentVerify)")
    public Object beforeMethod(ProceedingJoinPoint pjp, @SuppressWarnings("unused") PaymentVerify paymentVerify) throws Throwable {
        Object[] args = pjp.getArgs();
        if (args.length == 0){
            throw new PayFailureException("支付方法至少有一个参数，并且需要签名支付参数需要放在第一位");
        }
        Object param = args[0];
        if (param instanceof PaymentCommonParam){
            // 参数校验
            ValidationUtil.validateParam(param);

            // 请求上下文初始化
            paymentAssistService.initRequest((PaymentCommonParam) param);
            // 参数签名校验
            paymentAssistService.signVerify((PaymentCommonParam) param);
            // 参数请求时间校验
            paymentAssistService.reqTimeoutVerify((PaymentCommonParam) param);

        } else {
            throw new PayFailureException("支付参数需要继承PayCommonParam");
        }
        Object proceed;
        try {
            proceed = pjp.proceed();
        } catch (PayFailureException ex) {
            // 如果抛出支付异常, 包裹异常信息, 进行返回
            PaymentCommonResult commonResult = new PaymentCommonResult();
            // todo 后期错误码统一管理后进行更改
            commonResult.setCode(1);
            commonResult.setMsg(ex.getMessage());
            paymentAssistService.sign(commonResult);
            return DaxRes.ok(commonResult);
        }
        // 对返回值进行签名
        if (proceed instanceof ResResult){
            Object data = ((ResResult<?>) proceed).getData();
            if (data instanceof PaymentCommonResult){
                paymentAssistService.sign((PaymentCommonResult) data);
            } else {
                throw new PayFailureException("支付方法返回类型需要为 ResResult<T extends PaymentCommonResult> 格式");
            }
        } else {
            throw new PayFailureException("支付方法返回类型需要为 ResResult<T extends PaymentCommonResult> 格式");
        }
        return proceed;
    }
}
