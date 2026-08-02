package cn.daxpay.open.payment.unipay.result.trade.refund;

import cn.daxpay.open.payment.trade.enums.RefundOrderStatusEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

/// # 退款同步结果
///
@Data
@Accessors(chain = true)
@Schema(title = "退款同步结果")
public class RefundSyncResult {

    /// 同步后的退款订单状态
    /// @see RefundOrderStatusEnum
    @Schema(description = "同步后退款状态")
    private String orderStatus;

    /// 是否触发了调整(本地状态因本次同步发生了变更)
    @Schema(description = "是否触发了调整")
    private boolean adjust;

}
