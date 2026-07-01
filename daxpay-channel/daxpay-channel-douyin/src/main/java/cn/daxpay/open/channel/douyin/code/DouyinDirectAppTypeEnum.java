package cn.daxpay.open.channel.douyin.code;

import cn.daxpay.open.platform.core.i18n.I18nSupport;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/// # 抖音直连商户应用类型
///
/// 定义抖音直连商户应用([cn.daxpay.open.channel.douyin.entity.direct.DouyinDirectApp].appType)的取值范围。
///
@Getter
@RequiredArgsConstructor
public enum DouyinDirectAppTypeEnum implements I18nSupport {
    /// 小程序
    MINI_PROGRAM("mini_program"),
    /// 移动应用
    MOBILE_APP("mobile_app"),
    /// 网站应用
    WEB_APP("web_app"),
    ;

    private final String code;

    /// 翻译 key 前缀
    @Override
    public String getI18nPrefix() {
        return "enum.douyin_direct_app_type";
    }
}
