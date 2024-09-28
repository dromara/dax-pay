package cn.daxpay.multi.core.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 收银台类型
 * @author xxm
 * @since 2024/9/28
 */
@Getter
@AllArgsConstructor
public enum CashierTypeEnum {

    WECHAT_PAY("wechat_pay", "微信收银台"),
    ALIPAY("alipay", "支付宝收银台"),
    ;

    private final String code;
    private final String name;
}
