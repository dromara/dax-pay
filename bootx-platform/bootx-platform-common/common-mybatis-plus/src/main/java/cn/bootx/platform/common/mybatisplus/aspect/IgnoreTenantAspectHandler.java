package cn.bootx.platform.common.mybatisplus.aspect;

import cn.bootx.platform.core.annotation.IgnoreTenant;
import com.baomidou.mybatisplus.core.plugins.IgnoreStrategy;
import com.baomidou.mybatisplus.core.plugins.InterceptorIgnoreHelper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

/**
 * 忽略租户(商户)数据权限切面服务商
 * @author xxm
 * @since 2024/6/25
 */
@Slf4j
@Aspect
@Component
@RequiredArgsConstructor
public class IgnoreTenantAspectHandler {

    @Around("@annotation(ignoreTenant)||@within(ignoreTenant)")
    public Object doAround(ProceedingJoinPoint pjp, IgnoreTenant ignoreTenant) throws Throwable {
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
