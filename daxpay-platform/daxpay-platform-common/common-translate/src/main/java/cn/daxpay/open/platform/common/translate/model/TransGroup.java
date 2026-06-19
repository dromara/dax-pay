package cn.daxpay.open.platform.common.translate.model;

/// # 翻译分组
///
/// 相同 的翻译任务属于同一组，可以合并源字段值做批量查询
///
/// @param entity   目标实体类
/// @param source   源字段名（Result 对象中的字段）
/// @param on       目标实体中的匹配字段名
/// @param result   目标实体中要翻译出的字段名
/// @param cacheTtl 缓存 TTL（秒）
public record TransGroup(
    Class<?> entity,
    String source,
    String on,
    String result,
    int cacheTtl
) {}
