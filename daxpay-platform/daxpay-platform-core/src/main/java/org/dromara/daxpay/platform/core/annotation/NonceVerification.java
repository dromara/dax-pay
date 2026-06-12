package org.dromara.daxpay.platform.core.annotation;

import java.lang.annotation.*;

/// # 防重放Nonce验证, 可以放在controller控制器类和方法上，同时使用时，以方法上的为准
///
@Target({ ElementType.METHOD, ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
public @interface NonceVerification {

    /// nonce有效期（秒），默认300秒（5分钟）
    int timeout() default 300;

    /// 时间戳允许的偏差（秒），默认300秒（5分钟）
    /// 请求时间戳与服务器时间差超过此值则拒绝
    int timestampTolerance() default 300;

}

