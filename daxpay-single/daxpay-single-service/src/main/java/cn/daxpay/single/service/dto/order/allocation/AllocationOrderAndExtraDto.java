package cn.daxpay.single.service.dto.order.allocation;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 分账订单和扩展信息
 * @author xxm
 * @since 2024/5/29
 */
@Data
@Accessors(chain = true)
@Schema(title = "分账订单和扩展信息")
public class AllocationOrderAndExtraDto {
    @Schema(description = "分账订单")
    private AllocationOrderDto order;
    @Schema(description = "分账订单扩展信息")
    private AllocationOrderExtraDto extra;
}
