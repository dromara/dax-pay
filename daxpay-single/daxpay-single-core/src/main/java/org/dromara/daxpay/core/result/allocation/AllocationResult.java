package org.dromara.daxpay.core.result.allocation;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;
import org.dromara.daxpay.core.enums.AllocationResultEnum;
import org.dromara.daxpay.core.enums.AllocationStatusEnum;

/**
 * 分账请求结果
 * @author xxm
 * @since 2024/4/7
 */
@Data
@Accessors(chain = true)
@Schema(title = "分账请求结果")
public class AllocationResult {
    /** 分账订单号 */
    @Schema(description = "分账订单号")
    private String allocNo;

    /** 商户分账订单号 */
    @Schema(description = "商户分账订单号")
    private String bizAllocNo;

    /**
     * 分账状态
     * @see AllocationStatusEnum
     */
    @Schema(description = "分账状态")
    private String status;

    /**
     * 分账结果
     * @see AllocationResultEnum
     */
    @Schema(description = "分账结果")
    private String result;
}
