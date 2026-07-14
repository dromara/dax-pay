package cn.daxpay.open.platform.common.translate.model;

/// # 字典项数据
///
/// 封装字典项的 i18nKey，通过语言包翻译
///
/// @param i18nKey 国际化key
public record DictItemData(String i18nKey) {

    /// 根据字段名获取对应的语言值
    /// @param fieldName 字段名，如 "i18nKey"
    /// @return 对应的值，不支持的字段名返回 null
    public String get(String fieldName) {
        return switch (fieldName) {
            case "i18nKey" -> i18nKey;
            default -> null;
        };
    }
}

