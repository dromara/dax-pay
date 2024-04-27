package cn.bootx.platform.daxpay.service.core.payment.common.aop;

import cn.bootx.platform.common.core.rest.Res;
import cn.bootx.platform.common.core.rest.ResResult;
import cn.bootx.platform.common.core.util.ValidationUtil;
import cn.bootx.platform.daxpay.exception.pay.PayFailureException;
import cn.bootx.platform.daxpay.param.PaymentCommonParam;
import cn.bootx.platform.daxpay.result.PaymentCommonResult;
import cn.bootx.platform.daxpay.service.annotation.PaymentApi;
import cn.bootx.platform.daxpay.service.core.payment.common.service.PaymentSignService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
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
@RequiredArgsConstructor
public class PaymentVerifySignAop {
    private final PaymentSignService paymentSignService;

    @Around("@annotation(paymentApi)")
    public Object beforeMethod(ProceedingJoinPoint pjp, PaymentApi paymentApi) throws Throwable {
        Object[] args = pjp.getArgs();
        if (args.length == 0){
            throw new PayFailureException("支付方法至少有一个参数，并且需要签名支付参数需要放在第一位");
        }
        Object param = args[0];
        if (param instanceof PaymentCommonParam){
            // 参数校验
            ValidationUtil.validateParam(param);
            // 参数验签
            paymentSignService.verifySign((PaymentCommonParam) param);
        } else {
            throw new PayFailureException("支付参数需要继承PayCommonParam");
        }
        Object proceed;
        try {
            proceed = pjp.proceed();
        } catch (PayFailureException ex) {
            // 如果抛出支付异常, 包裹异常信息, 进行返回
            PaymentCommonResult commonResult = new PaymentCommonResult();
            commonResult.setMsg(ex.getMessage());
            paymentSignService.sign(commonResult);
            return Res.ok(commonResult);
        }
        // 对返回值进行签名
        if (proceed instanceof ResResult){
            Object data = ((ResResult<?>) proceed).getData();
            if (data instanceof PaymentCommonResult){
                paymentSignService.sign((PaymentCommonResult) data);
            } else {
                throw new PayFailureException("支付方法返回类型需要为 ResResult<T extends PaymentCommonResult> 格式");
            }
        } else {
            throw new PayFailureException("支付方法返回类型需要为 ResResult<T extends PaymentCommonResult> 格式");
        }
        return proceed;
    }
}
