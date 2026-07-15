package cn.daxpay.open.plugin.risk.enums;

import cn.daxpay.open.platform.core.i18n.I18nSupport;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;
import java.util.Optional;

/// # 风险命中来源场景
///
@Getter
@RequiredArgsConstructor
public enum PayRiskHitSceneEnum implements I18nSupport {

    /// 商户 API
    API("api"),
    /// 支付网关
    GATEWAY("gateway"),
    /// 码牌
    CODE("code"),
    /// 手工补录
    MANUAL("manual"),
    /// 未知
    UNKNOWN("unknown");

    private final String code;

    @Override
    public String getI18nPrefix() {
        return "enum.pay_risk_hit_scene";
    }

    public static Optional<PayRiskHitSceneEnum> findByCode(String code) {
        return Arrays.stream(values()).filter(e -> e.code.equals(code)).findFirst();
    }
}
