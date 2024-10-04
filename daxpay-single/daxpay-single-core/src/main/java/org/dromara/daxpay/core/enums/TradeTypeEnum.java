package org.dromara.daxpay.core.enums;

import org.dromara.daxpay.core.exception.ConfigNotExistException;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

/**
 * 交易类型, 如支付/退款/转账等
 * 字典: trade_type
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

    public static TradeTypeEnum findByCode(String code) {
        return Arrays.stream(values())
                .filter(tradeTypeEnum -> tradeTypeEnum.getCode().equals(code))
                .findFirst()
                .orElseThrow(() -> new ConfigNotExistException("交易类型不存在"));
    }
}
