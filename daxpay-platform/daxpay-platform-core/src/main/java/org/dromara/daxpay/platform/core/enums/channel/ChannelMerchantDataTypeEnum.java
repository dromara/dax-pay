package org.dromara.daxpay.platform.core.enums.channel;

import org.dromara.daxpay.platform.core.i18n.I18nSupport;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/// # 通道商户数据类型
///
@Getter
@RequiredArgsConstructor
public enum ChannelMerchantDataTypeEnum implements I18nSupport {

    /// 商户信息
    MCH_INFO("mch_info");

    /// 编码
    private final String code;

    /// 翻译 key 前缀
    @Override
    public String getI18nPrefix() {
        return "enum.channel_merchant_data_type";
    }

}
