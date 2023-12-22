package cn.bootx.platform.daxpay.annotation;

import java.lang.annotation.*;

/**
 * 支付接口标识
 * @author xxm
 * @since 2023/12/22
 */
@Target({ ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
public @interface PaymentApi {

    /**
     * 支付编码
     */
    String code();
}
