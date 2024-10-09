package org.dromara.daxpay.unisdk.common.bean;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 退款订单信息
 *
 * @author egan
 * <pre>
 *      email egzosn@gmail.com
 *      date 2018/1/15 21:40
 *   </pre>
 */
@Setter
@Getter
public class UniRefundOrder extends AssistOrder {
    /**
     * 退款单号，每次进行退款的单号，此处唯一
     */
    private String refundNo;
    /**
     * 退款金额
     */
    private BigDecimal refundAmount;
    /**
     * 订单总金额
     */
    private BigDecimal totalAmount;

    /**
     * 退款交易日期
     */
    private Date orderDate;

    /**
     * 货币
     */
    private CurType curType;
    /**
     * 退款说明
     */
    private String description;
    /**
     * 退款用户
     */
    private String userId;

    /**
     * 退款URL
     */
    private String refundUrl;

    public UniRefundOrder() {
    }

    public UniRefundOrder(String refundNo, String tradeNo, BigDecimal refundAmount) {
        this.refundNo = refundNo;
        setTradeNo(tradeNo);
        this.refundAmount = refundAmount;
    }

    public UniRefundOrder(String tradeNo, String outTradeNo, BigDecimal refundAmount, BigDecimal totalAmount) {
        setTradeNo(tradeNo);
        setOutTradeNo(outTradeNo);
        this.refundAmount = refundAmount;
        this.totalAmount = totalAmount;
    }

    public UniRefundOrder(String refundNo, String tradeNo, String outTradeNo, BigDecimal refundAmount, BigDecimal totalAmount) {
        this.refundNo = refundNo;
        setTradeNo(tradeNo);
        setOutTradeNo(outTradeNo);
        this.refundAmount = refundAmount;
        this.totalAmount = totalAmount;
    }

}
