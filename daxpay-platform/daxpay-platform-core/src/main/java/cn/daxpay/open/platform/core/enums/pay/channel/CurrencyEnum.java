package cn.daxpay.open.platform.core.enums.pay.channel;

import cn.daxpay.open.platform.core.exception.DataNotExistException;
import cn.daxpay.open.platform.core.i18n.I18nSupport;
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

    /// 人民币(2 位小数)
    CNY("cny", 2),
    /// 美元(2 位小数)
    USD("usd", 2),
    /// 日元(零小数位, 最小单位即元)
    JPY("jpy", 0),
    /// 欧元(2 位小数)
    EUR("eur", 2),
    /// 港元(2 位小数)
    HKD("hkd", 2),
    /// 英镑(2 位小数)
    GBP("gbp", 2),
    ;

    private final String code;

    /// 小数位数(ISO 4217): 用于金额「元 ↔ 最小货币单位」换算
    private final int minorUnit;

    @Override
    public String getI18nPrefix() {
        return "enum.currency";
    }

    /// 根据编码获取枚举
    public static CurrencyEnum findByCode(String code) {
        return Arrays.stream(values())
                .filter(e -> e.getCode().equals(code))
                .findFirst()
                .orElseThrow(() -> new DataNotExistException("error.common.currencyNotExist", code));
    }
}
