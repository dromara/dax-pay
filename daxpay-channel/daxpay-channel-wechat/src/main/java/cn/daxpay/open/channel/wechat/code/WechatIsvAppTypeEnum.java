package cn.daxpay.open.channel.wechat.code;

import cn.daxpay.open.platform.core.i18n.I18nSupport;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/// # 微信服务商应用类型
///
@Getter
@RequiredArgsConstructor
public enum WechatIsvAppTypeEnum implements I18nSupport {
    /// 公众号
    OFFICIAL_ACCOUNT("official_account"),
    /// 小程序
    MINI_PROGRAM("mini_program"),
    /// 移动应用
    MOBILE_APP("mobile_app"),
    ;

    private final String code;

    /// 翻译 key 前缀
    @Override
    public String getI18nPrefix() {
        return "enum.wechat_isv_app_type";
    }
}
