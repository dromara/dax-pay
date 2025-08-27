package org.dromara.daxpay.sdk.code;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 支付订单的退款状态
 * @author xxm
 * @since 2024/6/7
 */
@Getter
@AllArgsConstructor
public enum PayRefundStatusEnum {
    /** 未退款 */
    NO_REFUND("no_refund"),
    /** 退款中 */
    REFUNDING("refunding"),
    /** 部分退款 */
    PARTIAL_REFUND("partial_refund"),
    /** 全部退款 */
    REFUNDED("refunded"),
    ;
    private final String code;
}
