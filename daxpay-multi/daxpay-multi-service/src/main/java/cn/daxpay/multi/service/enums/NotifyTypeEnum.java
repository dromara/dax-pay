package cn.daxpay.multi.service.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 客户通知类型
 * @author xxm
 * @since 2024/7/30
 */
@Getter
@AllArgsConstructor
public enum NotifyTypeEnum {

    /** 支付订单变动通知 */
    PAY("pay", "支付订单变动通知"),

    /** 退款订单变动通知 */
    REFUND("refund", "退款订单变动通知"),

    /** 支付订单变动通知 */
    TRANSFER("transfer", "转账订单变动通知"),
    ;
    private final String code;
    private final String name;
}
