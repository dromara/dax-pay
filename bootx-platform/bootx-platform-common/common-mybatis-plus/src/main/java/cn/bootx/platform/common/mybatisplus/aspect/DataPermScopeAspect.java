package cn.bootx.platform.common.mybatisplus.aspect;

import cn.bootx.platform.common.mybatisplus.local.DataPermContextHolder;
import cn.bootx.platform.common.spring.util.AopUtil;
import cn.bootx.platform.core.annotation.DataPermScope;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

/**
 * 数据权限注解处理切面
 * @author xxm
 * @since 2025/2/2
 */
@Slf4j
@Aspect
@Component
@RequiredArgsConstructor
public class DataPermScopeAspect {

    /**
     * 数据权限注解切面，用栈的方式进行储存， 处理数据权限注解嵌套的情况
     */
    @Around("@annotation(dataPermScope)||@within(dataPermScope)")
    public Object doAround(ProceedingJoinPoint pjp, DataPermScope dataPermScope) throws Throwable {
        Object obj;
        var methodAnnotation = AopUtil.getMethodAnnotation(pjp, DataPermScope.class);
        DataPermContextHolder.push(methodAnnotation);
        try {
            obj = pjp.proceed();
        }
        finally {
            DataPermContextHolder.pop();
        }
        return obj;
    }
}
