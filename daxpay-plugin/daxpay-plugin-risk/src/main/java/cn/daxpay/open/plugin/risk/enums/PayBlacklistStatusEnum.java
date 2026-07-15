package cn.daxpay.open.plugin.risk.enums;

import cn.daxpay.open.platform.core.i18n.I18nSupport;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;
import java.util.Optional;

/// # 黑名单状态
///
@Getter
@RequiredArgsConstructor
public enum PayBlacklistStatusEnum implements I18nSupport {

    /// 启用
    ENABLE("enable"),
    /// 禁用
    DISABLE("disable");

    private final String code;

    @Override
    public String getI18nPrefix() {
        return "enum.pay_blacklist_status";
    }

    public static Optional<PayBlacklistStatusEnum> findByCode(String code) {
        return Arrays.stream(values()).filter(e -> e.code.equals(code)).findFirst();
    }
}
