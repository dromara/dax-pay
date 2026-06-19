package cn.daxpay.open.platform.common.mybatisplus.aspect;

import cn.daxpay.open.platform.common.mybatisplus.util.MpUtil;
import cn.daxpay.open.platform.core.annotation.IgnoreTenant;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

/// # 忽略租户(商户)数据权限切面
///
@Slf4j
@Aspect
@Component
@RequiredArgsConstructor
public class IgnoreTenantAspectHandler {

    /// 处理方法上的@IgnoreTenant注解
    @Around("@annotation(ignoreTenant)")
    public Object doAroundMethod(ProceedingJoinPoint pjp, IgnoreTenant ignoreTenant) throws Throwable {
        return doHandle(pjp);
    }

    /// 处理类上的@IgnoreTenant注解（排除方法上也有的情况，避免重复匹配）
    @Around("@within(ignoreTenant) && !@annotation(cn.daxpay.open.platform.core.annotation.IgnoreTenant)")
    public Object doAroundClass(ProceedingJoinPoint pjp, IgnoreTenant ignoreTenant) throws Throwable {
        return doHandle(pjp);
    }

    /// 忽略租户处理逻辑
    private Object doHandle(ProceedingJoinPoint pjp) throws Throwable {
        // 设置忽略租户插件
        MpUtil.ignoreTenant();
        try {
            // 执行逻辑
            return pjp.proceed();
        } finally {
            // 关闭忽略策略
            MpUtil.clearIgnoreTenant();
        }
    }
}
