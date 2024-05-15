package cn.bootx.platform.starter.data.perm.handler;

import cn.bootx.platform.common.core.annotation.NestedPermission;
import cn.bootx.platform.common.core.annotation.Permission;
import cn.bootx.platform.common.spring.util.AopUtil;
import cn.bootx.platform.starter.auth.util.SecurityUtil;
import cn.bootx.platform.starter.data.perm.local.DataPermContextHolder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import java.util.Objects;

/**
 * 忽略权限控制切面处理类
 *
 * @author xxm
 * @since 2021/12/22
 */
@Slf4j
@Aspect
@Component
@RequiredArgsConstructor
public class DataPermAspectHandler {

    /**
     * 数据权限注解切面
     */
    @Around("@annotation(permission)||@within(permission)")
    public Object doAround(ProceedingJoinPoint pjp, Permission permission) throws Throwable {
        Object obj;
        // 如果方法和类同时存在, 以方法上的注解为准
        Permission methodAnnotation = AopUtil.getMethodAnnotation(pjp, Permission.class);
        if (Objects.nonNull(methodAnnotation)) {
            DataPermContextHolder.putPermission(methodAnnotation);
        }
        else {
            DataPermContextHolder.putPermission(permission);
        }
        DataPermContextHolder.putUserDetail(SecurityUtil.getCurrentUser().orElse(null));
        try {
            obj = pjp.proceed();
        }
        finally {
            DataPermContextHolder.clearUserAndPermission();
        }
        return obj;
    }

    @Around("@annotation(nestedPermission)||@within(nestedPermission)")
    public Object doAround(ProceedingJoinPoint pjp, NestedPermission nestedPermission) throws Throwable {
        Object obj;
        // 如果方法和类同时存在, 以方法上的注解为准
        NestedPermission methodAnnotation = AopUtil.getMethodAnnotation(pjp, NestedPermission.class);
        if (Objects.nonNull(methodAnnotation)) {
            DataPermContextHolder.putNestedPermission(methodAnnotation);
        }
        else {
            DataPermContextHolder.putNestedPermission(nestedPermission);
        }
        DataPermContextHolder.putUserDetail(SecurityUtil.getCurrentUser().orElse(null));
        try {
            obj = pjp.proceed();
        }
        finally {
            DataPermContextHolder.clearNestedPermission();
        }
        return obj;
    }

}
