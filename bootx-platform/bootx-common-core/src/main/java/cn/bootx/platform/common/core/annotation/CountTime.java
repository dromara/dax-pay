package cn.bootx.platform.common.core.annotation;

import java.lang.annotation.*;

/**
 * 获取程序执行时间注解
 *
 * @author xxm
 * @since 2020/12/22
 */
@Target({ ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
public @interface CountTime {

}
