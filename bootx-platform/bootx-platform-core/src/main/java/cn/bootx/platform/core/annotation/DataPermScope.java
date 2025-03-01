package cn.bootx.platform.core.annotation;

import java.lang.annotation.*;

/**
 * 数据权限控制注解 可以放在服务类和方法上，支持嵌套使用，嵌套使用时可以通过添加多个数据来控制不同作用域实现不同的控制
 * A 使用部门数据权限控制
 * ----- 根据部门进行控制
 * - B 关闭数据权限
 * ----- 数据权限不生效
 * -- C 使用终端数据权限
 * ----- 根据终端进行控制
 * -- C 使用终端数据权限使用结束
 * ----- 数据权限不生效
 * - B 开启数据权限控制
 * ----- 根据部门进行控制
 * A 执行结束
 * @author xxm
 * @since 2021/12/22
 */
@Target({ ElementType.METHOD, ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
public @interface DataPermScope {

    /**
     * 是否开启数据权限， 在嵌套情况下， 可以通过设置为false来临时关闭数据权限的控制
     */
    boolean dataScope() default true;

    /**
     * 数据权限类型，不同类型需要有不同的数据权限处理实现
     */
    String value();

}
