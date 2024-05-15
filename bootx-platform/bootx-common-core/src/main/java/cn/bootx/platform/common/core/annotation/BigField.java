package cn.bootx.platform.common.core.annotation;

import java.lang.annotation.*;

/**
 * 大字段注解
 *
 * @author xxm
 * @since 2021/10/24
 */
@Target({ ElementType.PARAMETER, ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
public @interface BigField {

}
