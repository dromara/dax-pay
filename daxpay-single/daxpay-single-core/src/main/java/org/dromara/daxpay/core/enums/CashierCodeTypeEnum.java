package org.dromara.daxpay.core.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 收银码牌类型
 * 字典: cashier_code_type
 * @author xxm
 * @since 2024/9/28
 */
@Getter
@AllArgsConstructor
public enum CashierCodeTypeEnum {

    WECHAT_PAY("wechat_pay", "微信码牌"),
    ALIPAY("alipay", "支付宝码牌"),
    ;

    private final String code;
    private final String name;
}
