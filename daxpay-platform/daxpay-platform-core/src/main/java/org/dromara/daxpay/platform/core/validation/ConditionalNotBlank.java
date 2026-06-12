package org.dromara.daxpay.platform.core.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/// # 条件非空校验：当指定字段的值等于期望值时，目标字段必须非空
///
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Repeatable(ConditionalNotBlank.List.class)
@Constraint(validatedBy = ConditionalNotBlankValidator.class)
public @interface ConditionalNotBlank {

    String message() default "{validation.notBlank}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    /// 目标字段（需要校验的字段名）
    String target();

    /// 关联字段（用作条件的字段名）
    String field();

    /// 关联字段期望的值，满足时目标字段必填
    String fieldValue();

    @Target({ElementType.TYPE})
    @Retention(RetentionPolicy.RUNTIME)
    @interface List {
        ConditionalNotBlank[] value();
    }
}