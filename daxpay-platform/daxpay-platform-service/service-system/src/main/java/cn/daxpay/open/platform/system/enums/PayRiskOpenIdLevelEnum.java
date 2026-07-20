package cn.daxpay.open.platform.system.enums;

import cn.daxpay.open.platform.core.i18n.I18nSupport;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;
import java.util.Optional;

/// # 用户标识拦截级别
///
/// 控制用户标识黑名单（微信 openId / 支付宝 userId 等）对「原本免用户标识支付方式（H5 / 主扫）」
/// 是否触发强制 OAuth。JSAPI / 小程序原生方式业务本身就要用户标识, 不受此开关影响。
///
/// - [NORMAL]: 仅做 IP + 事后补录, 不为风控额外触发 OAuth（保留用户体验）
/// - [ENHANCED]: 存在用户标识名单时, 对 H5 / 主扫等可 OAuth 方式强制静默授权
@Getter
@RequiredArgsConstructor
public enum PayRiskOpenIdLevelEnum implements I18nSupport {

    /// 正常拦截（仅 IP + 事后补录）
    NORMAL("normal"),
    /// 增强拦截（对 H5 / 主扫强制 OAuth 取用户标识）
    ENHANCED("enhanced");

    private final String code;

    @Override
    public String getI18nPrefix() {
        return "enum.pay_risk_open_id_level";
    }

    public static Optional<PayRiskOpenIdLevelEnum> findByCode(String code) {
        return Arrays.stream(values()).filter(e -> e.code.equals(code)).findFirst();
    }
}
