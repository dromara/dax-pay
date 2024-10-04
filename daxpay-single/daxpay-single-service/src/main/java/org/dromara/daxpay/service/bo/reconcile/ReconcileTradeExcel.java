package org.dromara.daxpay.service.bo.reconcile;

import org.dromara.daxpay.service.enums.ReconcileDiscrepancyTypeEnum;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 对账明细导出类
 * @author xxm
 * @since 2024/8/13
 */
@Data
@Accessors(chain = true)
public class ReconcileTradeExcel {

    /** 平台交易号 */
    private String tradeNo;

    /** 商户订单号 */
    private String bizTradeNo;

    /** 通道交易号 */
    private String outTradeNo;

    /**
     * 对账结果
     * @see ReconcileDiscrepancyTypeEnum 和 一致
     */
    private String result;

    /* 平台侧信息 */

    /** 交易类型 */
    private String tradeType;

    /** 交易金额 */
    private String tradeAmount;

    /** 交易状态 */
    private String tradeStatus;

    /** 交易时间 */
    private String tradeTime;

    /* 通道侧信息 */
    /** 通道交易号 */
    private String channelTradeNo;

    /** 交易类型 */
    private String channelTradeType;

    /** 通道交易金额 */
    private String channelTradeAmount;

    /** 通道交易状态 */
    private String channelTradeStatus;

    /** 通道交易时间 */
    private String channelTradeTime;
}
