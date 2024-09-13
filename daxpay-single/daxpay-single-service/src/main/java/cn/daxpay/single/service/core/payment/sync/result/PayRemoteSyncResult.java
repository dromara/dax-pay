package cn.daxpay.single.service.core.payment.sync.result;

import cn.daxpay.single.core.code.PaySyncStatusEnum;
import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

import static cn.daxpay.single.core.code.PaySyncStatusEnum.FAIL;

/**
 * 支付网关同步结果
 *
 * @author xxm
 * @since 2021/4/21
 */
@Data
@Accessors(chain = true)
public class PayRemoteSyncResult {

    /**
     * 支付网关订单状态
     * @see PaySyncStatusEnum
     */
    private PaySyncStatusEnum syncStatus = FAIL;

    /**
     * 外部第三方支付系统的交易号, 用与和本地记录关联起来
     */
    private String outOrderNo;

    /** 支付完成时间 */
    private LocalDateTime finishTime;

    /** 同步时网关返回的对象, 序列化为json字符串 */
    private String syncInfo;

    /** 错误提示码 */
    private String errorCode;

    /** 错误提示 */
    private String errorMsg;

}
