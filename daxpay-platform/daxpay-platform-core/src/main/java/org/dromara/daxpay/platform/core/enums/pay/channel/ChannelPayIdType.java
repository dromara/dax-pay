package org.dromara.daxpay.platform.core.enums.pay.channel;

import org.dromara.daxpay.platform.core.i18n.I18nSupport;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/// # 通道支付标识类型
///
@Getter
@RequiredArgsConstructor
public enum ChannelPayIdType implements I18nSupport {

    /// 商户号作为支付标识
    MCH("mch"),
    /// 身份标识（如支付宝服务商的app_auth_token）
    IDENTITY("identity");

    /// 编码
    private final String code;

    /// 翻译 key 前缀
    @Override
    public String getI18nPrefix() {
        return "enum.channel_pay_id_type";
    }

}
