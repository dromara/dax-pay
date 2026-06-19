package cn.daxpay.open.platform.core.annotation;

import java.lang.annotation.*;

/// # 内部请求接口, 只允许超级管理员访问
///
@Target({ ElementType.METHOD, ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
public @interface InternalPath {
}
