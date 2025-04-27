package org.dromara.daxpay.service.bo.trade;

import org.dromara.daxpay.core.enums.TransferStatusEnum;
import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

/**
 * 转账结果业务类
 * @author xxm
 * @since 2024/7/23
 */
@Data
@Accessors(chain = true)
public class TransferResultBo {
    /** 通道转账订单号 */
    private String outTransferNo;

    /** 状态 */
    private TransferStatusEnum status = TransferStatusEnum.PROGRESS;

    /** 完成时间 */
    private LocalDateTime finishTime;
}
