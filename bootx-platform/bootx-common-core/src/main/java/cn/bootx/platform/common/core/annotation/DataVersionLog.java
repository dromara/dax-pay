package cn.bootx.platform.common.core.annotation;

import java.lang.annotation.*;

/**
 * 数据版本日志
 *
 * @author xxm
 * @since 2023/1/2
 */
@Target({ ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
public @interface DataVersionLog {

    /**
     * 数据记录的标题
     */
    String title() default "";

}
