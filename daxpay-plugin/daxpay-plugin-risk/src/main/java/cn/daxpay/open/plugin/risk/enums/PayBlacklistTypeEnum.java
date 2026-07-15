package cn.daxpay.open.plugin.risk.enums;

import cn.daxpay.open.platform.core.i18n.I18nSupport;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;
import java.util.Optional;

/// # 黑名单类型
///
@Getter
@RequiredArgsConstructor
public enum PayBlacklistTypeEnum implements I18nSupport {

    /// IP
    IP("ip"),
    /// 用户 openId / buyerId
    OPEN_ID("open_id");

    private final String code;

    @Override
    public String getI18nPrefix() {
        return "enum.pay_blacklist_type";
    }

    public static Optional<PayBlacklistTypeEnum> findByCode(String code) {
        return Arrays.stream(values()).filter(e -> e.code.equals(code)).findFirst();
    }
}
