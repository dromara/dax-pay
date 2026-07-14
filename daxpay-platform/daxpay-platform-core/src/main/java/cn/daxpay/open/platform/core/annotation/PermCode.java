package cn.daxpay.open.platform.core.annotation;

import java.lang.annotation.*;

/// # 权限码
///
@Target({ ElementType.TYPE, ElementType.PARAMETER, ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
public @interface PermCode {

    /// 权限码
    String code() default "";

    /// 菜单编码
    String menuCode() default "";
}

