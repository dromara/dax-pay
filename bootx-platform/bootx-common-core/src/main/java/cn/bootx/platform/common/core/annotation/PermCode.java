package cn.bootx.platform.common.core.annotation;

import java.lang.annotation.*;

/**
 * 权限码鉴权注解
 * 1. 用在Controller及对应的请求方法上时，可以通过用户是否拥有该请求路径的权限码，来决定是否可以通行本次请求
 * 2.放在数据库实体类及字段上时，会在启用查询字段权限的时候，对用户没有权限码的字段不进行SQL查询
 *
 * @author xxm
 * @since 2023/1/22
 */
@Target({ ElementType.METHOD, ElementType.FIELD, ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
public @interface PermCode {

    /**
     * 权限码，支持配置多个
     */
    String[] value();

}
