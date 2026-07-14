package cn.daxpay.open.platform.core.annotation;

import java.lang.annotation.*;

/// # 大字段注解
///
/// 标记实体中体积较大的字段（如文本/二进制），供查询构造时选择性排除。
@Target({ ElementType.PARAMETER, ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
public @interface BigField {

}
