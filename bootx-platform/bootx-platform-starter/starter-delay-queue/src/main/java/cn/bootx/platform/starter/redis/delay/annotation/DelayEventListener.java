package cn.bootx.platform.starter.redis.delay.annotation;


import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 延时任务接收方法注解
 * 参数需要固定为 DelayJobEvent<T>
 * @see DelayJobEvent
 * @author xxm
 * @since 2024/7/31
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface DelayEventListener {

    /**
     * 任务topic名称
     */
    String value();
}
