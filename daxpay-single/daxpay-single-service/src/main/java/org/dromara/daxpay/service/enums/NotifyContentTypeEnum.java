package org.dromara.daxpay.service.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 客户通知内容类型
 * 字典: notify_content_type
 * @author xxm
 * @since 2024/7/30
 */
@Getter
@AllArgsConstructor
public enum NotifyContentTypeEnum {

    /** 支付订单变动通知 */
    PAY("pay", "支付订单变动通知"),

    /** 退款订单变动通知 */
    REFUND("refund", "退款订单变动通知"),

    /** 支付订单变动通知 */
    TRANSFER("transfer", "转账订单变动通知"),

    /** 分账订单变动通知 */
    ALLOCATION("allocation", "分账订单变动通知"),
    ;
    private final String code;
    private final String name;
}
