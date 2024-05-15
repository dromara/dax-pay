package cn.bootx.platform.common.core.annotation;

import java.lang.annotation.*;

/**
 * 数据权限控制注解 可以放在类上服务方法、服务类和数据实体类上 用在数据实体类上时通常是，停用该实体类对应SQL语句执行时停用数据权限
 *
 * @see NestedPermission 通常在嵌套方法不启用数据权限时使用
 * @author xxm
 * @since 2021/12/22
 */
@Target({ ElementType.METHOD, ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
public @interface Permission {

    /**
     * 数据范围权限
     */
    boolean dataScope() default true;

    /**
     * 查询字段权限
     */
    boolean selectField() default true;

}
