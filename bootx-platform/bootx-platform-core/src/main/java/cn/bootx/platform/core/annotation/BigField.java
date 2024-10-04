package cn.bootx.platform.core.annotation;

import java.lang.annotation.*;

/**
 * 大字段注解
 * 配合 {@linkplain  cn.hutool.core.map.MapUtil} 中 excludeBigField 方法使用使用
 *
 * 使用Mp条件构造器器分页查询时样例
 * {@snippet :
 * wrapper.select(this.getEntityClass (), MpUtil::excludeBigField);
 * }
 *
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
