package cn.bootx.platform.daxpay.service.core.payment.sync.result;

import cn.bootx.platform.daxpay.code.PayRefundSyncStatusEnum;
import lombok.Data;
import lombok.experimental.Accessors;

import static cn.bootx.platform.daxpay.code.PayRefundSyncStatusEnum.FAIL;

/**
 * 支付退款同步结果
 * @author xxm
 * @since 2024/1/25
 */
@Data
@Accessors(chain = true)
public class RefundGatewaySyncResult {

    /**
     * 支付网关订单状态
     * @see PayRefundSyncStatusEnum
     */
    private PayRefundSyncStatusEnum syncStatus = FAIL;

    /** 同步时网关返回的对象, 序列化为json字符串 */
    private String syncInfo;

    /** 错误提示码 */
    private String errorCode;

    /** 错误提示 */
    private String errorMsg;
}
