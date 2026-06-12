package org.dromara.daxpay.platform.iam.code.annotation;

import java.lang.annotation.*;

/// # 菜单绑定注解
///
/// 用于标记Controller类,声明该类属于哪个菜单模块
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface MenuBinding {
    /// 菜单编码,用于关联菜单
    String value();
    
    /// 描述信息
    String description() default "";
}
