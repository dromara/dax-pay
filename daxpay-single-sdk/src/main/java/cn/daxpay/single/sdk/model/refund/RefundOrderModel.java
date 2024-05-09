package cn.daxpay.single.sdk.model.refund;

import cn.daxpay.single.sdk.code.RefundStatusEnum;
import cn.daxpay.single.sdk.net.DaxPayResponseModel;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;

/**
 * 退款订单数据
 * @author xxm
 * @since 2024/1/16
 */
@Getter
@Setter
@ToString
public class RefundOrderModel extends DaxPayResponseModel {

    /** 退款号 */
    private String refundNo;

    /** 商户退款号 */
    private String bizRefundNo;

    /** 标题 */
    private String title;

    /** 退款金额 */
    private BigDecimal amount;

    /** 退款完成时间 */
    private Long finishTime;

    /** 退款发起时间 */
    private Long refundTime;

    /**
     * 退款状态
     * @see RefundStatusEnum
     */
    private String status;
}
