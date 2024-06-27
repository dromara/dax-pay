package cn.daxpay.single.sdk.code;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 支付订单的退款状态
 * @author xxm
 * @since 2024/6/7
 */
@Getter
@AllArgsConstructor
public enum PayOrderRefundStatusEnum {
    NO_REFUND("no_refund","未退款"),
    REFUNDING("refunding","退款中"),
    PARTIAL_REFUND("partial_refund","部分退款"),
    REFUNDED("refunded","全部退款"),
    ;
    private final String code;
    private final String name;
}
