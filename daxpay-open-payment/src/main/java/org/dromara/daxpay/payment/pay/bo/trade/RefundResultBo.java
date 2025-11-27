package org.dromara.daxpay.payment.pay.bo.trade;

import org.dromara.daxpay.payment.pay.enums.RefundStatusEnum;
import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

/**
 * 退款结果业务类
 * @author xxm
 * @since 2024/7/23
 */
@Data
@Accessors(chain = true)
public class RefundResultBo {
    /**
     * 第三方支付网关生成的退款订单号, 用与将记录关联起来
     */
    private String outRefundNo;

    /**
     * 特殊通道关联订单号
     */
    private String relationOrderNo;

    /**
     * 退款状态
     */
    private RefundStatusEnum status;

    /** 退款完成时间 */
    private LocalDateTime finishTime;
}
