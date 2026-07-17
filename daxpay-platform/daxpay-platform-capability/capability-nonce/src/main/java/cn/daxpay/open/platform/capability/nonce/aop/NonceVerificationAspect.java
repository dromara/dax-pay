package cn.daxpay.open.platform.capability.nonce.aop;

import cn.daxpay.open.platform.capability.nonce.config.NonceVerificationConfigProvider;
import cn.daxpay.open.platform.capability.nonce.service.NonceService;
import cn.daxpay.open.platform.core.annotation.NonceVerification;
import cn.daxpay.open.platform.core.code.WebHeaderCode;
import cn.daxpay.open.platform.core.exception.NonceMissingException;
import cn.hutool.core.util.StrUtil;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

/// # 防重放Nonce验证切面
///
/// 配置来源（依赖倒置）: 若容器中注册了 [NonceVerificationConfigProvider] 实现:
/// - enabled=false 时跳过所有校验（配置级总开关）
/// - timestampTolerance 从配置读取，覆盖注解参数
/// 若无实现，则走注解参数默认值，保持向后兼容。
@Aspect
@Component
@Order
@Slf4j
@RequiredArgsConstructor
public class NonceVerificationAspect {

    private final NonceService nonceService;
    private final ObjectProvider<NonceVerificationConfigProvider> configProviderProvider;

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
        // 配置级总开关：provider 存在且 enabled=false 时跳过校验
        NonceVerificationConfigProvider provider = configProviderProvider.getIfAvailable();
        if (provider != null && !provider.isEnabled()) {
            return pjp.proceed();
        }

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

        // 时间戳容差: provider 优先（全局配置），回退注解参数（方法级）
        int timestampTolerance = nonceVerification.timestampTolerance();
        if (provider != null) {
            int configTolerance = provider.getTimestampToleranceSeconds();
            if (configTolerance > 0) {
                timestampTolerance = configTolerance;
            }
        }

        // 验证nonce和时间戳
        nonceService.verify(nonce, timestamp, timestampTolerance);

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
