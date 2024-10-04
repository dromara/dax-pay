package org.dromara.daxpay.service.bo.sync;

import org.dromara.daxpay.core.enums.RefundStatusEnum;
import lombok.Data;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 支付退款同步结果
 * @author xxm
 * @since 2024/1/25
 */
@Data
@Accessors(chain = true)
public class RefundSyncResultBo {

    /** 同步是否成功 */
    private boolean syncSuccess = true;

    /** 退款状态 */
    private RefundStatusEnum refundStatus;

    /** 外部三方支付网关生成的退款交易号, 用与将记录关联起来 */
    private String outRefundNo;

    /** 交易错误信息 */
    private String tradeErrorMsg;

    /** 交易金额 */
    private BigDecimal amount;

    /** 退款完成时间(通常用于接收网关返回的时间) */
    private LocalDateTime finishTime;

    /** 同步时网关返回的对象, 序列化为json字符串 */
    private String syncData;

    /** 错误提示码 */
    private String syncErrorCode;

    /** 错误提示 */
    private String syncErrorMsg;
}
