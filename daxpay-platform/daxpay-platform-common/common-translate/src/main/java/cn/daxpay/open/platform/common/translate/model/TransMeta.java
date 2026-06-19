package cn.daxpay.open.platform.common.translate.model;

import cn.daxpay.open.platform.core.annotation.Trans;
import java.lang.reflect.Field;

/// # 翻译元数据
///
/// 记录一个 @Trans 注解字段的所有翻译信息
///
/// @param entity     目标实体类
/// @param source     源字段名（Result 对象中的字段）
/// @param on         目标实体中的匹配字段名
/// @param result     目标实体中要翻译出的字段名
/// @param field      被 @Trans 注解的字段
/// @param annotation @Trans 注解
public record TransMeta(
    Class<?> entity,
    String source,
    String on,
    String result,
    Field field,
    Trans annotation
) {}
