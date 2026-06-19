package cn.daxpay.open.channel.wechat.code;

import cn.daxpay.open.platform.core.i18n.I18nSupport;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/// # 微信OpenId认证方式
///
@Getter
@RequiredArgsConstructor
public enum WechatAuthTypeEnum implements I18nSupport {
    /// 服务商用户标识
    SP("sp"),
    /// 子商户应用用户标识
    SUB("sub"),
    ;

    private final String code;

    /// 翻译 key 前缀
    @Override
    public String getI18nPrefix() {
        return "enum.wechat_auth_type";
    }
}
