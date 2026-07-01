package cn.daxpay.open.payment.unipay.result.trade.pay;

import cn.daxpay.open.platform.core.enums.pay.pay.PayStatusEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

/// # 支付同步结果
///
@Data
@Accessors(chain = true)
@Schema(title = "支付同步结果")
public class NormalPaySyncResult {

    /// 退款订单同步后的状态状态
    /// @see PayStatusEnum
    @Schema(description = "同步状态")
    private String orderStatus;

    /// 是否触发了调整
    @Schema(description = "是否触发了调整")
    private boolean adjust;

}

