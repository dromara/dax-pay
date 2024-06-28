package cn.daxpay.multi.gateway.aop;

import cn.bootx.platform.core.exception.ValidationFailedException;
import cn.bootx.platform.core.util.ValidationUtil;
import cn.daxpay.multi.core.anno.PaymentVerify;
import cn.daxpay.multi.core.exception.PayFailureException;
import cn.daxpay.multi.core.param.PaymentCommonParam;
import cn.daxpay.multi.core.result.DaxResult;
import cn.daxpay.multi.service.service.assist.PaymentAssistService;
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
            throw new ValidationFailedException("支付方法至少有一个参数，并且需要签名的支付参数放在第一位");
        }
        Object param = args[0];
        if (param instanceof PaymentCommonParam){
            // 参数校验
            ValidationUtil.validateParam(param);
            // 终端信息初始化
            paymentAssistService.initClient((PaymentCommonParam) param);
            // 参数签名校验
            paymentAssistService.signVerify((PaymentCommonParam) param);
            // 参数请求时间校验
            paymentAssistService.reqTimeoutVerify((PaymentCommonParam) param);

        } else {
            throw new ValidationFailedException("参数需要继承PayCommonParam");
        }
        Object proceed;
        try {
            proceed = pjp.proceed();
        } catch (PayFailureException ex) {
            DaxResult<Void> result = new DaxResult<>(ex.getCode(), ex.getMessage());
            paymentAssistService.sign(result);
            return result;
        }
        // 对返回值进行签名
        if (proceed instanceof DaxResult<?> result){
            paymentAssistService.sign(result);
        } else {
            throw new ValidationFailedException("支付方法返回类型需要为 ResResult> 格式");
        }
        return proceed;
    }
}
