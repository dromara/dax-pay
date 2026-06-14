package org.dromara.daxpay.platform.core.enums.channel;

import org.dromara.daxpay.platform.core.i18n.I18nSupport;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/// # 通道商户创建来源
///
@Getter
@RequiredArgsConstructor
public enum ChannelMerchantSourceEnum implements I18nSupport {

    /// 手动创建
    MANUAL("manual");

    /// 编码
    private final String code;

    /// 翻译 key 前缀
    @Override
    public String getI18nPrefix() {
        return "enum.channel_merchant_source";
    }

}
