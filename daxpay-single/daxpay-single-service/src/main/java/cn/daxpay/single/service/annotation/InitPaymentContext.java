package cn.daxpay.single.service.annotation;

import cn.daxpay.single.code.PaymentApiCode;

import java.lang.annotation.*;

/**
 * 初始化平台信息和接口API相关的上下文信息
 * @author xxm
 * @since 2024/5/22
 */
@Target({ ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
public @interface InitPaymentContext {

    /**
     * 接口标识
     * @see PaymentApiCode
     */
    String value();
}
