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
/// 与 [MpUtil#ignoreTenant]/[MpUtil#clearIgnoreTenant] 的线程内引用计数配合，
/// 支持 `@IgnoreTenant` 嵌套：内层返回后不会清掉外层 ignore，直到最外层 finally 才恢复过滤。
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

    /// 可重入忽略租户：内层 @IgnoreTenant 不会清掉外层 ignore（见 MpUtil 引用计数）
    private Object doHandle(ProceedingJoinPoint pjp) throws Throwable {
        // 进入忽略租户作用域（可重入）
        MpUtil.ignoreTenant();
        try {
            // 执行逻辑
            return pjp.proceed();
        } finally {
            // 退出忽略租户作用域（depth 归零时才真正 clear）
            MpUtil.clearIgnoreTenant();
        }
    }
}
