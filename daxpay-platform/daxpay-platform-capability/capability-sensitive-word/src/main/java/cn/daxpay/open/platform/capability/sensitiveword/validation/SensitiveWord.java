package cn.daxpay.open.platform.capability.sensitiveword.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/// # 敏感词校验注解
///
/// 用于管理端/商户端 Param 自由文本字段；开放 API 请用 [SensitiveWordCheckService]。
@Documented
@Target({ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = SensitiveWordValidator.class)
public @interface SensitiveWord {

    String message() default "{validation.field.sensitiveWord.rejected}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    /// 是否按 HTML 剥离后再检
    boolean html() default false;
}

