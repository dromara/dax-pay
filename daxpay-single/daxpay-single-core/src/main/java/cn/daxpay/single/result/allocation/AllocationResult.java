package cn.daxpay.single.result.allocation;

import cn.daxpay.single.result.PaymentCommonResult;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 分账请求结果
 * @author xxm
 * @since 2024/4/7
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
@Schema(title = "分账请求结果")
public class AllocationResult extends PaymentCommonResult {

    @Schema(description = "分账订单ID")
    private Long orderId;

    @Schema(description = "分账订单号, 如果请求时未传, 则默认使用分账订单ID")
    private String allocationNo;

    @Schema(description = "分账状态")
    private String status;
}
