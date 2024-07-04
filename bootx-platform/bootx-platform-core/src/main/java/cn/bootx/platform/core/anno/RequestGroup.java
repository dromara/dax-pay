package cn.bootx.platform.core.anno;

import java.lang.annotation.*;

/**
 * 请求分组注解
 * @see RequestPath
 * <br/>
 * 分为三级: 模块-->分组-->路径
 * <br/>
 * 模块: 通常对应一个的功能模块, 如用户管理、认证服务、权限管理等
 * <br/>
 * 分组: 通常对应多个请求组成的一个功能点分组, 如用户管理下的用户管理、用户管理下的角色管理等，多数时候会对对应少数几个或者一个控制器
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
     * 模块名称
     */
    String moduleName() default "";

    /**
     * 分组编码
     */
    String groupCode();

    /**
     * 分组名称
     */
    String groupName() default "";

}
