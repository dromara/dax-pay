package org.dromara.daxpay.platform.capability.auth.service.impl;

import org.dromara.daxpay.platform.core.annotation.IgnoreAuth;
import org.dromara.daxpay.platform.capability.auth.service.RouterCheck;
import org.dromara.daxpay.platform.capability.auth.util.SecurityUtil;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;

import java.util.Objects;

/// # 注解方式过滤
///
@Component
public class IgnoreAnnotationRouterCheck implements RouterCheck {

    @Override
    public int sortNo() {
        return Integer.MIN_VALUE;
    }

    @Override
    public boolean check(Object handler) {
        if (handler instanceof HandlerMethod handlerMethod) {
            // controller上是否加了跳过鉴权注解
            IgnoreAuth ignoreAuth = handlerMethod.getBeanType().getAnnotation(IgnoreAuth.class);
            if (Objects.isNull(ignoreAuth)) {
                // 方法上上是否加了跳过鉴权注解
                ignoreAuth = handlerMethod.getMethodAnnotation(IgnoreAuth.class);
            }
            else {
                // controller和方法上都加了跳过鉴权注解,以方法上为准
                IgnoreAuth annotation = handlerMethod.getMethodAnnotation(IgnoreAuth.class);
                if (Objects.nonNull(annotation)) {
                    ignoreAuth = annotation;
                }
            }
            return this.ignoreAuth(ignoreAuth);
        }
        return false;
    }

    /// 忽略鉴权注解处理
    private boolean ignoreAuth(IgnoreAuth ignoreAuth) {
        if (Objects.isNull(ignoreAuth)) {
            return false;
        }
        // 只校验登录状态
        if (ignoreAuth.login()) {
            return SecurityUtil.getCurrentUser().isPresent();
        }
        return true;
    }

}
