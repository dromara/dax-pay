package cn.daxpay.open.payment.unipay.result.trade.transfer;

import cn.daxpay.open.payment.trade.enums.PayFundStatusEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

/// # 转账同步结果
///
@Data
@Accessors(chain = true)
@Schema(title = "转账同步结果")
public class TransferSyncResult {

    /// 同步后的转账订单状态
    /// @see PayFundStatusEnum
    @Schema(description = "同步后转账状态")
    private String orderStatus;

    /// 是否触发了调整(本地状态因本次同步发生了变更)
    @Schema(description = "是否触发了调整")
    private boolean adjust;

}
