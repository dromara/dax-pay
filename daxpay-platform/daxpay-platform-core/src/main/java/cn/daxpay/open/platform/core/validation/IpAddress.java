package cn.daxpay.open.platform.core.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


@Target({ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
// 标记由哪个类来执行校验逻辑，该类需要实现ConstraintValidator接口
@Constraint(validatedBy = IpAddressValidator.class)
public @interface IpAddress {

    String message() default "{validation.field.ip.notValid}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
