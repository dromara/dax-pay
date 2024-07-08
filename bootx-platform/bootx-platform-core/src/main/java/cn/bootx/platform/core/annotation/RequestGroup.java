package cn.bootx.platform.core.annotation;

import java.lang.annotation.*;

/**
 * 请求分组注解
 * @see RequestPath
 *
 * <br/>
 * 分为三级: 模块-->分组-->路径
 * <br/>
 * 模块: 通常对应一个的功能模块, 如用户管理、认证服务、权限管理等
 * <br/>
 * 分组: 通常对应多个请求组成的一个功能点分组, 如用户管理下的用户管理、用户管理下的角色管理等，多数时候会对对应少数几个或者一个控制器(Controller)
 * <br/>
 * 路径: 对应具体的一个具体请求方式(GET/POST)的请求路径
 *
 * @author xxm
 * @since 2024/7/4
 */
@Target({ ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
public @interface RequestGroup {
    /**
     * 模块编码
     */
    String moduleCode();

    /**
     * 模块名称, 多次标注注解时, 同样的模块编码, 可以只写一个模块名称, 其他未写的会自动使用这个
     */
    String moduleName() default "";

    /**
     * 分组编码
     */
    String groupCode();

    /**
     * 分组名称, 多次标注注解时, 同样的分组编码, 可以只写一个分组名称, 其他未写的会自动使用这个
     */
    String groupName() default "";

}
