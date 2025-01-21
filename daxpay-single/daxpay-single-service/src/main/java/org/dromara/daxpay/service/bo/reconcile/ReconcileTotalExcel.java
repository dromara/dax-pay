package org.dromara.daxpay.service.bo.reconcile;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 对账单汇总导出对象
 * @author xxm
 * @since 2024/8/13
 */
@Data
@Accessors(chain = true)
public class ReconcileTotalExcel {

    /** 对账日期 */
    private String reconcileDate;

    /** 生成时间 */
    private String createTime;

    /** 通道 */
    private String channel;

    /** 对账结果 */
    private String result;

    /* 平台交易概览 */
    /** 交易支付汇总 */
    private String tradeAmount;

    /** 交易支付次数 */
    private Integer tradeCount;

    /** 交易退款汇总 */
    private String refundAmount;

    /** 退款次数 */
    private Integer refundCount;

    /* 通道交易概览 */
    /** 交易支付汇总 */
    private String channelTradeAmount;

    /** 交易支付次数 */
    private Integer channelTradeCount;

    /** 交易退款汇总 */
    private String channelRefundAmount;

    /** 退款次数 */
    private Integer channelRefundCount;

}
