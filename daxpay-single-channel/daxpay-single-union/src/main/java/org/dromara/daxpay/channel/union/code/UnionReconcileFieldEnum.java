package org.dromara.daxpay.channel.union.code;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

/**
 * 云闪付对账单字段
 * @author xxm
 * @since 2024/3/24
 */
@Getter
@AllArgsConstructor
public enum UnionReconcileFieldEnum {

    /** 交易代码 */
    TRADE_TYPE(0, "tradeType"),
    /** 交易传输时间 MMDDhhmmss */
    txnTime(4, "txnTime"),
    /** 交易金额  */
    TXN_AMT(6, "txnAmt"),
    /** 查询流水号 */
    QUERY_ID(9, "queryId"),
    /** 商户订单号 */
    ORDER_ID(11, "orderId");



    /** 序号 */
    private final int no;
    /** 字段名 */
    private final String filed;

    /**
     * 根据序号查询
     */
    public static UnionReconcileFieldEnum findByNo(int no){
        return Arrays.stream(values())
                .filter(o->o.no == no)
                .findFirst()
                .orElse(null);
    }
}
