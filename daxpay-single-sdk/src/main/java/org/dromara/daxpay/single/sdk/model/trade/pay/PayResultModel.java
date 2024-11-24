package org.dromara.daxpay.single.sdk.model.trade.pay;

import lombok.Data;

/**
 * 发起支付后响应对象
 * @author xxm
 * @since 2024/2/2
 */
@Data
public class PayResultModel {

    /** 商户订单号 */
    private String bizOrderNo;

    /** 订单号 */
    private String orderNo;

    /** 支付状态 */
    private String status;

    /** 支付参数体(通常用于发起支付的参数) */
    private String payBody;
}
