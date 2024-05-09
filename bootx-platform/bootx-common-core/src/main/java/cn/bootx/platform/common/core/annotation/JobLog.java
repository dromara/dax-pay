package cn.bootx.platform.common.core.annotation;

import java.lang.annotation.*;

/**
 * 定时任务日志
 *
 * @author xxm
 * @since 2022/7/12
 */
@Target({ ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
public @interface JobLog {

    /**
     * 是否记录正常日志
     */
    boolean log() default true;

    /**
     * 是否记录异常日志
     */
    boolean errorLog() default true;

}
