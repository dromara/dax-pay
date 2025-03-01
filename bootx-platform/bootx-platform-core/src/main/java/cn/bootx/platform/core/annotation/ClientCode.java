package cn.bootx.platform.core.annotation;

import java.lang.annotation.*;

/**
 * 终端类型注解, 如果添加控制器上添加了该类型. 进行访问时将会自动验证终端类型是否匹配, 不添加默认所有终端类型都可以访问
 * 终端可以约等于业务系统概念, 但不是完全一对一的关系
 * 在控制器类和方法上都可以使用该注解, 同时标注时会以方法上的为准, 并不会取这两者的终端合集, 而是方法上的直接覆盖掉类的注解
 * @author xxm
 * @since 2025/1/31
 */
@Target({ ElementType.TYPE, ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
public @interface ClientCode {

    /**
     * 终端编码
     */
    String[] value();
}
