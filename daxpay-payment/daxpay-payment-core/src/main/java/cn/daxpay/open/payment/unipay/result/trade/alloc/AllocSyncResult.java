package cn.daxpay.open.payment.unipay.result.trade.alloc;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

/// # 分账同步结果(对外)
@Data
@Accessors(chain = true)
@Schema(title = "分账同步结果")
public class AllocSyncResult {

    /// 同步后分账状态
    @Schema(description = "同步后分账状态")
    private String orderStatus;

    /// 是否调整了状态(同步前后状态不同则为 true)
    @Schema(description = "是否调整了状态")
    private boolean adjust;
}
