package org.dromara.daxpay.core.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 钱包流水类型
 * 字典: wallet_flow_type
 * @author xxm
 * @since 2025/6/23
 */
@Getter
@AllArgsConstructor
public enum WalletFlowTypeEnum {
    ORDER_PROFIT("order_profit","订单分润"),
    REFUND_PROFIT("refund_profit","退款轧差"),
    WITHDRAW("withdraw","提现"),
    ADJUST("adjust","人工调账"),
    ;
    private final String code;
    private final String name;

}
