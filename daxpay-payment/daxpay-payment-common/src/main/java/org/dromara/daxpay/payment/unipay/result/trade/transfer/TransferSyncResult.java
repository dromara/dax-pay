package org.dromara.daxpay.payment.unipay.result.trade.transfer;

import org.dromara.daxpay.platform.core.enums.pay.transfer.TransferStatusEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

/// # 转账同步结果
///
@Data
@Accessors(chain = true)
@Schema(title = "转账同步结果")
public class TransferSyncResult {

    /// 转账状态
    /// @see TransferStatusEnum
    @Schema(description = "转账状态")
    private String orderStatus;

    /// 是否触发了调整
    @Schema(description = "是否触发了调整")
    private boolean adjust;
}

