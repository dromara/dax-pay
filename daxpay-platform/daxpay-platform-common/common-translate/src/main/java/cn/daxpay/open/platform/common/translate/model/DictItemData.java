package cn.daxpay.open.platform.common.translate.model;

/// # 字典项数据
///
/// 封装字典项的多种语言名称，通过 @Trans.result 指定取哪种语言
///
/// @param nameCn 中文名称
/// @param nameEn 英文名称
public record DictItemData(String nameCn, String nameEn) {

    /// 根据字段名获取对应的语言值
    /// @param fieldName 字段名，如 "nameCn"、"nameEn"
    /// @return 对应的语言值，不支持的字段名返回 null
    public String get(String fieldName) {
        return switch (fieldName) {
            case "nameCn" -> nameCn;
            case "nameEn" -> nameEn;
            default -> null;
        };
    }
}

