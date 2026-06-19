package cn.daxpay.open.platform.core.i18n;

/// # 国际化枚举支持接口
///
/// 需要按 message key 展示文案的 Java enum 实现此接口, 通过 I18nUtil.getEnumName() 获取本地化名称
public interface I18nSupport {

    /// 获取枚举的编码值
    String getCode();

    /// 枚举对应的翻译 key 前缀（不含最后的点号）
    /// 与 JSON 文件路径对应, 例如 ChannelEnum 对应 "enum.channel",
    /// 则 JSON 文件路径为 enum/channel.json, key 为 enum.channel.alipay
    default String getI18nPrefix() {
        return "enum";
    }
}
