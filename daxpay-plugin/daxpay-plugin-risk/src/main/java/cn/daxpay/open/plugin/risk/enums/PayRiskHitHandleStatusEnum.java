package cn.daxpay.open.plugin.risk.enums;

import cn.daxpay.open.platform.core.i18n.I18nSupport;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;
import java.util.Optional;

/// # 风险命中处理状态
///
@Getter
@RequiredArgsConstructor
public enum PayRiskHitHandleStatusEnum implements I18nSupport {

    /// 待处理
    PENDING("pending"),
    /// 已忽略
    IGNORED("ignored"),
    /// 已加入黑名单
    ADDED_BLACKLIST("added_blacklist"),
    /// 已标记停用商户（仅状态，不自动改商户）
    MERCHANT_DISABLED("merchant_disabled"),
    /// 其他
    OTHER("other");

    private final String code;

    @Override
    public String getI18nPrefix() {
        return "enum.pay_risk_hit_handle_status";
    }

    public static Optional<PayRiskHitHandleStatusEnum> findByCode(String code) {
        return Arrays.stream(values()).filter(e -> e.code.equals(code)).findFirst();
    }
}
