package cn.bootx.platform.daxpay.result.pay;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 退款同步结果
 * @author xxm
 * @since 2024/1/29
 */
@Data
@Accessors(chain = true)
@Schema(title = "退款同步结果")
public class RefundSyncResult {



    @Schema(description = "是否进行了修复")
    private boolean repair;

    @Schema(description = "支付单修复ID")
    private Long repairId;

    @Schema(description = "失败原因")
    private String errorMsg;
}
