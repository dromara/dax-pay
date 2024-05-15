package cn.bootx.platform.common.core.annotation;

import java.lang.annotation.*;

/**
 * 加密字段注解
 *
 * @author xxm
 * @since 2021/11/23
 */
@Target({ ElementType.PARAMETER, ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
public @interface EncryptionField {

}
