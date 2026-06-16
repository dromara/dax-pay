package org.dromara.daxpay.platform.core.enums.pay.channel;

import org.dromara.daxpay.platform.core.exception.DataNotExistException;
import org.dromara.daxpay.platform.core.i18n.I18nSupport;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;

/// # 币种枚举
///
/// ISO 4217 三位字母币种代码
/// 字典: currency
@Getter
@RequiredArgsConstructor
public enum CurrencyEnum implements I18nSupport {

    /// 人民币
    CNY("cny"),
    /// 美元
    USD("usd"),
    /// 日元
    JPY("jpy"),
    /// 欧元
    EUR("eur"),
    /// 港元
    HKD("hkd"),
    /// 英镑
    GBP("gbp"),
    ;

    private final String code;

    @Override
    public String getI18nPrefix() {
        return "enum.currency";
    }

    /// 根据编码获取枚举
    public static CurrencyEnum findByCode(String code) {
        return Arrays.stream(values())
                .filter(e -> e.getCode().equals(code))
                .findFirst()
                .orElseThrow(() -> new DataNotExistException("不存在的币种: " + code));
    }
}
