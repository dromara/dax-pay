package org.dromara.daxpay.platform.core.annotation;

import java.lang.annotation.*;

/// # 部分脱敏规则注解
///
/// 用于配置字段值保留前后 N/M 位，中间用 * 掩码
///
/// @see OperateLog
@Target(ElementType.ANNOTATION_TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface PartialMaskRule {

    /// 敏感字段名，大小写不敏感
    String key();

    /// 保留前缀字符数
    /// 默认 3
    int keepPrefix() default 3;

    /// 保留后缀字符数
    /// 默认 4
    int keepSuffix() default 4;

}

