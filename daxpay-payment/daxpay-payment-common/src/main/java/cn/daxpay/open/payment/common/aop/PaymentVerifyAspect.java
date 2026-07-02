package cn.daxpay.open.payment.common.aop;

import cn.daxpay.open.platform.core.code.CommonCode;
import cn.daxpay.open.platform.core.exception.BizException;
import cn.daxpay.open.platform.core.exception.ValidationFailedException;
import cn.daxpay.open.platform.core.exception.BizInfoException;
import cn.daxpay.open.platform.core.code.CommonErrorCode;
import cn.daxpay.open.platform.core.util.ValidationUtil;
import cn.daxpay.open.payment.unipay.param.MerchantPaymentCommonParam;
import cn.daxpay.open.payment.common.result.DaxResult;
import cn.daxpay.open.payment.common.service.MerchantContextLoader;
import cn.daxpay.open.payment.common.service.PaySignService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.MDC;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;

/// # 支付签名切面(common.aop.PaymentVerify)
///
/// 执行顺序: 过滤器 -> 拦截器 -> 切面 -> 方法。
/// 切面只负责"商户身份初始化 + 签名校验",不负责应用解析(由支付流程完成)。
@Aspect
@Slf4j
@Component
@Order
@RequiredArgsConstructor
public class PaymentVerifyAspect {
    private final PaySignService paySignService;
    private final MerchantContextLoader merchantContextLoader;

    /// 处理方法上的@PaymentVerify注解
    @Around("@annotation(paymentVerify)")
    public Object methodPointAnnotation(ProceedingJoinPoint pjp, PaymentVerify paymentVerify) throws Throwable {
        return doVerify(pjp);
    }

    /// 处理类上的@PaymentVerify注解（排除方法上也有的情况，避免重复匹配）
    @Around("@within(paymentVerify) && !@annotation(cn.daxpay.open.payment.common.aop.PaymentVerify)")
    public Object methodPointWithin(ProceedingJoinPoint pjp, PaymentVerify paymentVerify) throws Throwable {
        return doVerify(pjp);
    }

    /// 支付签名校验逻辑
    private Object doVerify(ProceedingJoinPoint pjp) throws Throwable {
        Object[] args = pjp.getArgs();
        if (args.length == 0){
            // 支付方法至少有一个参数
            throw new BizInfoException(CommonErrorCode.VALIDATE_PARAMETERS_ERROR, "pay.error.verify.methodParamRequired");
        }
        Object param = args[0];
        if (param instanceof MerchantPaymentCommonParam paymentParam){
            // 参数校验
            ValidationUtil.validateParam(paymentParam);
            // 商户身份初始化(含状态校验), 使 mchNo 进入线程上下文供签名校验与自动填充
            merchantContextLoader.initMch(paymentParam.getMchNo());
            // 参数签名校验
            paySignService.signVerify(paymentParam);
        } else {
            // 参数需要继承MerchantPaymentCommonParam
            throw new BizInfoException(CommonErrorCode.VALIDATE_PARAMETERS_ERROR, "pay.error.verify.paramExtendRequired");
        }
        Object proceed;
        try {
            proceed = pjp.proceed();
        } catch (BizException ex) {
            DaxResult<Void> result = new DaxResult<>(ex.getCode(), ex.getMessage());
            paySignService.sign(result);
            return result;
        }
        // 对返回值添加响应时间并进行签名
        if (proceed instanceof DaxResult<?> result){
            result.setResTime(OffsetDateTime.now(ZoneOffset.UTC));
            paySignService.sign(result);
        } else {
            // 支付方法返回类型需要为 DaxResult
            throw new BizInfoException(CommonErrorCode.VALIDATE_PARAMETERS_ERROR, "pay.error.verify.returnTypeRequired");
        }
        return proceed;
    }

}
