package cn.daxpay.open.platform.common.translate.cache;

/// # 翻译缓存键
///
/// 由目标实体类 + 源字段名 + 翻译结果字段名 + 源字段值 组成
/// 用于缓存翻译结果，避免重复查询数据库
///
/// @param entity      目标实体类
/// @param source      源字段名
/// @param result      翻译结果字段名
/// @param sourceValue 源字段值
public record TransCacheKey(
    Class<?> entity,
    String source,
    String result,
    Object sourceValue
) {}
