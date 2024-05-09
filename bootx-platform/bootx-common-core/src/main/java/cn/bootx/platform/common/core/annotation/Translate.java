package cn.bootx.platform.common.core.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 翻译注解
 *
 * @author xxm
 * @since 2022/12/14
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Translate {

    /**
     * 翻译类型
     */
    Type type();

    /**
     * 字典编码
     */
    String dicCode() default "";

    /**
     * 来源字段 默认为自身
     */
    String source() default "";

    /**
     * 目标字段 默认为自身
     */
    String target() default "";

    /**
     * 翻译类型
     */
    enum Type {

        /** 字典 */
        DICT,
        /** 数据表 暂未实现 */
        TABLE;

    }

}
