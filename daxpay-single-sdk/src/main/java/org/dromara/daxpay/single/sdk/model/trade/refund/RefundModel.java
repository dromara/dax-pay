package org.dromara.daxpay.single.sdk.model.trade.refund;

import lombok.Data;

/**
 * 退款响应参数
 * @author xxm
 * @since 2023/12/18
 */
@Data
public class RefundModel{

    /** 退款号 */
    private String refundNo;

    /** 商户退款号 */
    private String bizRefundNo;

    /** 退款状态 */
    private String status;
}
