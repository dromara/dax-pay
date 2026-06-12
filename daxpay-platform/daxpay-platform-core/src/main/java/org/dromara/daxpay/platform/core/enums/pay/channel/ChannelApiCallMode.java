package org.dromara.daxpay.platform.core.enums.pay.channel;

import org.dromara.daxpay.platform.core.i18n.I18nSupport;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/// # 通道接口调用方式
///
@Getter
@RequiredArgsConstructor
public enum ChannelApiCallMode implements I18nSupport {

    /// 标准商户调用: 子商户包含所有调用接口使用的配置
    MCH("mch"),
    /// 服务商调用: 服务商包含所有调用接口使用的配置, 只需要商户号就可以进行调用接口
    ISV("isv"),
    /// 混合调用: 单独服务商或者都无法发起调用，需要综合两份的配置才可以
    MIX("mix");

    /// 编码
    private final String code;

    /// 翻译 key 前缀
    @Override
    public String getI18nPrefix() {
        return "enum.channel_api_call_mode";
    }

}
