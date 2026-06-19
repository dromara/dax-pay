package cn.daxpay.open.platform.core.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/// # 忽略租户数据隔离注解, 根据是实际情况发现添加注解后, 有多条语句只会第一条生效, 这个所以需要最好放在Manager层使用
///
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface IgnoreTenant {
}
