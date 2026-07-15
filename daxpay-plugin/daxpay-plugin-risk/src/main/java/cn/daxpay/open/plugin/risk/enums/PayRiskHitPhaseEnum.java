package cn.daxpay.open.plugin.risk.enums;

import cn.daxpay.open.platform.core.i18n.I18nSupport;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;
import java.util.Optional;

/// # 风险命中阶段
///
@Getter
@RequiredArgsConstructor
public enum PayRiskHitPhaseEnum implements I18nSupport {

    /// 事前拦截
    BEFORE_PAY("before_pay"),
    /// 事后命中
    AFTER_PAY("after_pay");

    private final String code;

    @Override
    public String getI18nPrefix() {
        return "enum.pay_risk_hit_phase";
    }

    public static Optional<PayRiskHitPhaseEnum> findByCode(String code) {
        return Arrays.stream(values()).filter(e -> e.code.equals(code)).findFirst();
    }
}
