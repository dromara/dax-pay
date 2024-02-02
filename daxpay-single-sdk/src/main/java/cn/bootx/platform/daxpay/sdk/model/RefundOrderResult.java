package cn.bootx.platform.daxpay.sdk.model;

import cn.bootx.platform.daxpay.sdk.code.RefundStatusEnum;
import cn.bootx.platform.daxpay.sdk.net.DaxPayResponseModel;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 退款订单数据
 * @author xxm
 * @since 2024/1/16
 */
public class RefundOrderResult extends DaxPayResponseModel {

    /** 支付号 */
    private Long paymentId;

    /** 关联的业务id */
    private String businessNo;

    /** 退款号 */
    private String refundNo;

    /** 标题 */
    private String title;

    /** 退款金额 */
    private BigDecimal amount;

    /** 剩余可退 */
    private BigDecimal refundableBalance;

    /**
     * 异步支付通道发给网关的退款号, 用与将记录关联起来
     */
    private String gatewayOrderNo;

    /** 通道退款订单 */
    private List<RefundChannelOrderModel> channels;

    /** 退款终端ip */
    private String clientIp;

    /** 退款时间 */
    private LocalDateTime refundTime;

    /**
     * 退款状态
     * @see RefundStatusEnum
     */
    private String status;
}
