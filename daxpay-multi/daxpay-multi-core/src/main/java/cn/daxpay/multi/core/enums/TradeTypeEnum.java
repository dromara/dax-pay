package cn.daxpay.multi.core.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 常见的交易类型, 如支付/退款/转账等
 * @author xxm
 * @since 2024/1/28
 */
@Getter
@AllArgsConstructor
public enum TradeTypeEnum {

    PAY("pay","支付"),
    REFUND("refund","退款"),
    TRANSFER("transfer","转账"),;

    private final String code;
    private final String name;
}
