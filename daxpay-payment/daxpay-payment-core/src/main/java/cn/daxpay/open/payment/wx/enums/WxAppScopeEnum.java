package cn.daxpay.open.payment.wx.enums;

import cn.daxpay.open.platform.core.i18n.I18nSupport;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;
import java.util.Objects;

/// # 微信应用档位
///
/// 区分平台级与商户级开放应用主数据。
///
@Getter
@RequiredArgsConstructor
public enum WxAppScopeEnum implements I18nSupport {
    /// 平台级应用
    PLATFORM("platform"),
    /// 商户级应用
    MERCHANT("merchant"),
    ;

    private final String code;

    /// 翻译 key 前缀
    @Override
    public String getI18nPrefix() {
        return "enum.wx_app_scope";
    }

    /// 根据编码获取枚举；未知编码返回 null
    public static WxAppScopeEnum findByCode(String code) {
        if (code == null) {
            return null;
        }
        return Arrays.stream(values())
                .filter(e -> Objects.equals(e.code, code))
                .findFirst()
                .orElse(null);
    }
}
