package cn.bootx.platform.common.core.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 字典转换标示注解, 标注此注解会对对应对象进行字典值转换处理
 *
 * @author xxm
 * @since 2022/12/15
 */
@Target({ ElementType.METHOD, ElementType.TYPE, ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
public @interface TableFieldConvertModel {

}
