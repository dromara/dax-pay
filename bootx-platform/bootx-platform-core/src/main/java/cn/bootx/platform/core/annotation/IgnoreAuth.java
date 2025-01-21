package cn.bootx.platform.core.annotation;

import java.lang.annotation.*;

/**
 * 忽略鉴权, 可以放在controller控制器类和方法上，同时使用时，以方法上的为准
 * @author xxm
 * @since 2024/6/25
 */
@Target({ ElementType.METHOD, ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
public @interface IgnoreAuth {

    /**
     * 只要登录就可以访问，忽略权限校验
     */
    boolean login() default false;


}
