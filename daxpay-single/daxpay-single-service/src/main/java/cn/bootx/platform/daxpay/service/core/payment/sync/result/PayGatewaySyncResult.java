package cn.bootx.platform.daxpay.service.core.payment.sync.result;

import cn.bootx.platform.daxpay.code.PaySyncStatusEnum;
import lombok.Data;
import lombok.experimental.Accessors;

import static cn.bootx.platform.daxpay.code.PaySyncStatusEnum.FAIL;

/**
 * 支付网关同步结果
 *
 * @author xxm
 * @since 2021/4/21
 */
@Data
@Accessors(chain = true)
public class PayGatewaySyncResult {

    /**
     * 支付网关订单状态
     * @see PaySyncStatusEnum
     */
    private PaySyncStatusEnum syncStatus = FAIL;

    /** 同步支付时网关返回的对象, 序列化为json字符串 */
    private String syncInfo;

    /** 错误提示 */
    private String errorMsg;

}
