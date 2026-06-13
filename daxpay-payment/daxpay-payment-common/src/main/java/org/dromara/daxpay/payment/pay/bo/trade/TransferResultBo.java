package org.dromara.daxpay.payment.pay.bo.trade;

import org.dromara.daxpay.platform.core.enums.pay.transfer.TransferStatusEnum;
import lombok.Data;
import lombok.experimental.Accessors;

import java.time.OffsetDateTime;

/// # 转账结果业务类
///
@Data
@Accessors(chain = true)
public class TransferResultBo {
    /// 通道转账订单号
    private String outTransferNo;

    /// 状态
    private TransferStatusEnum status = TransferStatusEnum.PROGRESS;

    /// 转账返回参数
    private String transferBody;

    /// 完成时间
    private OffsetDateTime finishTime;

    /// 进件商户号
    private String onbMchNo;
}
