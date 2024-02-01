package cn.bootx.platform.daxpay.service.core.payment.sync.result;

import cn.bootx.platform.daxpay.code.PayRefundSyncStatusEnum;
import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

import static cn.bootx.platform.daxpay.code.PayRefundSyncStatusEnum.PROGRESS;

/**
 * 支付退款同步结果
 * @author xxm
 * @since 2024/1/25
 */
@Data
@Accessors(chain = true)
public class RefundGatewaySyncResult {

    /**
     * 支付网关订单状态, 默认为退款中
     * @see PayRefundSyncStatusEnum
     */
    private PayRefundSyncStatusEnum syncStatus = PROGRESS;

    /** 同步时网关返回的对象, 序列化为json字符串 */
    private String syncInfo;

    /**
     * 第三方支付网关生成的订单号, 用与将记录关联起来
     */
    private String gatewayOrderNo;

    /** 退款完成时间(通常用于接收网关返回的时间) */
    private LocalDateTime refundTime;

    /** 错误提示码 */
    private String errorCode;

    /** 错误提示 */
    private String errorMsg;
}
