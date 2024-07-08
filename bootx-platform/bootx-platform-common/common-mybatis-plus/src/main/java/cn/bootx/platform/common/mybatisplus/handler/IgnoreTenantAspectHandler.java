package cn.bootx.platform.common.mybatisplus.handler;

import com.baomidou.mybatisplus.core.plugins.IgnoreStrategy;
import com.baomidou.mybatisplus.core.plugins.InterceptorIgnoreHelper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

/**
 * 商户租户切面
 * @author xxm
 * @since 2024/6/25
 */
@Slf4j
@Aspect
@Component
@RequiredArgsConstructor
public class IgnoreTenantAspectHandler {

    @Around("@annotation(cn.bootx.platform.core.annotation.IgnoreTenant)||within(@cn.bootx.platform.core.annotation.IgnoreTenant *)")
    public Object doAround(ProceedingJoinPoint pjp) throws Throwable {
        // 设置忽略租户插件
        InterceptorIgnoreHelper.handle(IgnoreStrategy.builder().tenantLine(true).build());
        try {
            // 执行逻辑
            return pjp.proceed();
        } finally {
            // 关闭忽略策略
            InterceptorIgnoreHelper.clearIgnoreStrategy();
        }
    }
}
