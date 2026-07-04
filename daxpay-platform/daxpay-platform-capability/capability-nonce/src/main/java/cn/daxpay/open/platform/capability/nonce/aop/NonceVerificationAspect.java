package cn.daxpay.open.platform.capability.nonce.aop;

import cn.daxpay.open.platform.capability.nonce.service.NonceService;
import cn.daxpay.open.platform.core.annotation.NonceVerification;
import cn.daxpay.open.platform.core.code.WebHeaderCode;
import cn.daxpay.open.platform.core.exception.NonceMissingException;
import cn.hutool.core.util.StrUtil;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

/// # 防重放Nonce验证切面
///
@Aspect
@Component
@Order
@RequiredArgsConstructor
public class NonceVerificationAspect {

    private final NonceService nonceService;

    /// 处理方法上的@NonceVerification注解
    @Around("@annotation(nonceVerification)")
    public Object verifyMethod(ProceedingJoinPoint pjp, NonceVerification nonceVerification) throws Throwable {
        return doVerify(pjp, nonceVerification);
    }

    /// 处理类上的@NonceVerification注解（排除方法上也有的情况，避免重复匹配）
    @Around("@within(nonceVerification) && !@annotation(cn.daxpay.open.platform.core.annotation.NonceVerification)")
    public Object verifyClass(ProceedingJoinPoint pjp, NonceVerification nonceVerification) throws Throwable {
        return doVerify(pjp, nonceVerification);
    }

    /// 验证逻辑
    private Object doVerify(ProceedingJoinPoint pjp, NonceVerification nonceVerification) throws Throwable {
        HttpServletRequest request = getRequest();
        String nonce = request.getHeader(WebHeaderCode.X_NONCE);
        String timestampStr = request.getHeader(WebHeaderCode.X_TIMESTAMP);

        // nonce缺失
        if (StrUtil.isBlank(nonce)) {
            throw new NonceMissingException();
        }

        // 解析时间戳
        long timestamp;
        try {
            timestamp = Long.parseLong(timestampStr);
        } catch (NumberFormatException e) {
            timestamp = 0L;
        }

        // 验证nonce和时间戳
        nonceService.verify(nonce, timestamp, nonceVerification.timestampTolerance());

        return pjp.proceed();
    }

    /// 获取当前HTTP请求
    private HttpServletRequest getRequest() {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (attributes == null) {
            throw new NonceMissingException("error.common.requestContextMissing");
        }
        return attributes.getRequest();
    }

}
