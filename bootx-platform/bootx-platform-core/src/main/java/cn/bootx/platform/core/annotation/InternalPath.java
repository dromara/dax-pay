package cn.bootx.platform.core.annotation;

import java.lang.annotation.*;

/**
 * 内部请求接口, 只允许超级管理员访问
 * @author xxm
 * @since 2024/6/25
 */
@Target({ ElementType.METHOD, ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
public @interface InternalPath {
}
