package org.dromara.daxpay.platform.core.annotation;

import java.lang.annotation.*;

/// # 操作日志注解组
///
@Target({ ElementType.PARAMETER, ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
public @interface OperateLogs {

    /// 操作日志注解组
    OperateLog[] value();

}
