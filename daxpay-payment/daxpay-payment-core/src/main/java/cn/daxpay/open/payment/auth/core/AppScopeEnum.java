package cn.daxpay.open.payment.auth.core;

import cn.daxpay.open.platform.core.i18n.I18nSupport;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;
import java.util.Objects;

/// # 通道应用档位
///
/// 区分平台级与商户级开放应用主数据, 通道无关(微信 / 抖音 / 未来扩展通道共用)。
/// 供认证域([AuthSession]/[cn.daxpay.open.payment.unipay.param.assist.GenerateAuthUrlParam])
/// 与各通道 Facade([cn.daxpay.open.payment.wx.facade.WxAppFacade]/
/// [cn.daxpay.open.payment.douyin.facade.DouyinAppFacade])统一标识应用档位。
///
/// @see cn.daxpay.open.payment.wx.enums.WxAppTypeEnum 通道特定的应用类型(不归并)
/// @see cn.daxpay.open.payment.douyin.enums.DyAppTypeEnum
@Getter
@RequiredArgsConstructor
public enum AppScopeEnum implements I18nSupport {
    /// 平台级应用
    PLATFORM("platform"),
    /// 商户级应用
    MERCHANT("merchant"),
    ;

    private final String code;

    /// 翻译 key 前缀
    @Override
    public String getI18nPrefix() {
        return "enum.app_scope";
    }

    /// 根据编码获取枚举；未知编码返回 null
    public static AppScopeEnum findByCode(String code) {
        if (code == null) {
            return null;
        }
        return Arrays.stream(values())
                .filter(e -> Objects.equals(e.code, code))
                .findFirst()
                .orElse(null);
    }
}
