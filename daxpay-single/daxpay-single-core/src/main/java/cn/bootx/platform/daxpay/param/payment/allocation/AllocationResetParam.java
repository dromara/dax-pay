package cn.bootx.platform.daxpay.param.payment.allocation;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 重新分账参数
 * @author xxm
 * @since 2024/4/15
 */
@Data
@Accessors(chain = true)
@Schema(title = "重新分账参数")
public class AllocationResetParam {

    @Schema(description = "分账订单ID")
    private Long orderId;

    @Schema(description = "分账单号")
    private String allocationNo;
}
