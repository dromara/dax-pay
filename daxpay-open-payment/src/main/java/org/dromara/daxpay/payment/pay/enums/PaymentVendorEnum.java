package org.dromara.daxpay.payment.pay.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 支付厂商
 * @author xxm
 * @since 2025/10/9
 */
@Getter
@AllArgsConstructor
public enum PaymentVendorEnum {
    WECHAT("wechat", "微信"),
    ALIPAY("alipay", "支付宝"),
    UNION_PAY("union_pay", "银联"),
    ;
    private final String code;
    private final String name;
}
