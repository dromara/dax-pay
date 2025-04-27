package org.dromara.daxpay.core.enums;

import org.dromara.daxpay.core.exception.ConfigNotExistException;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;
import java.util.Objects;

/**
 * 码牌收银台类型
 * 字典: cashier_code_type
 * @author xxm
 * @since 2024/9/28
 */
@Getter
@AllArgsConstructor
public enum CashierCodeTypeEnum {

    WECHAT_PAY("wechat_pay", "微信码牌"),
    ALIPAY("alipay", "支付宝码牌"),
    UNION_PAY("union_pay", "云闪付码牌"),
    ;

    private final String code;
    private final String name;

    public static CashierCodeTypeEnum findByCode(String code) {
        return Arrays.stream(values())
                .filter(item -> Objects.equals(item.code, code))
                .findFirst()
                .orElseThrow(() -> new ConfigNotExistException("退款状态不存在"));
    }
}
