package cn.bootx.platform.common.redis.delay.annotation;


import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 延时任务接收注解
 * @author xxm
 * @since 2024/7/31
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface DelayTaskJob {

    /**
     * 任务名称
     */
    String value();
}
