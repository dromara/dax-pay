package org.dromara.daxpay.service.bo.sync;

import org.dromara.daxpay.core.enums.TransferStatusEnum;
import lombok.Data;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 转账同步结果
 * @author xxm
 * @since 2024/7/25
 */
@Data
@Accessors(chain = true)
public class TransferSyncResultBo {

    /** 同步是否成功 */
    private boolean syncSuccess = true;

    /** 转账订单状态 */
    private TransferStatusEnum transferStatus;

    /** 支付通道生成的转账交易号, 用与将本地记录关联起来 */
    private String outTransferNo;

    /** 交易错误信息 */
    private String tradeErrorMsg;

    /** 交易金额 */
    private BigDecimal amount;

    /** 完成时间(通常用于接收网关返回的时间) */
    private LocalDateTime finishTime;

    /** 同步时网关返回的对象, 序列化为json字符串 */
    private String syncData;

    /** 错误提示码 */
    private String syncErrorCode;

    /** 同步错误提示 */
    private String syncErrorMsg;
}
