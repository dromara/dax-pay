package org.dromara.daxpay.platform.common.translate.model;

import org.dromara.daxpay.platform.core.annotation.Trans;
import java.lang.reflect.Field;

/// # 翻译字段信息
///
/// 记录某个 Result 对象中某个字段的翻译信息，用于批量回填
///
/// @param result     当前 Result 对象
/// @param field      被 @Trans 注解的字段
/// @param annotation @Trans 注解
public record TransFieldInfo(
    Object result,
    Field field,
    Trans annotation
) {}
