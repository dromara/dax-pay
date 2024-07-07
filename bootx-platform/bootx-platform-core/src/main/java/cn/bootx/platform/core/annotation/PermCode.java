package cn.bootx.platform.core.annotation;

import java.lang.annotation.*;

/**
 * 权限码
 * @author xxm
 * @since 2024/7/7
 */
@Target({ ElementType.PARAMETER, ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
public @interface PermCode {

    /**
     * 权限码，支持配置多个
     */
    String[] value();
}
