package org.dromara.daxpay.core.result.allocation.transaction;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;
import org.dromara.daxpay.core.enums.AllocTransactionStatusEnum;

/**
 * 分账请求结果
 * @author xxm
 * @since 2024/4/7
 */
@Data
@Accessors(chain = true)
@Schema(title = "分账请求结果")
public class AllocResult  {
    /** 分账订单号 */
    @Schema(description = "分账订单号")
    private String allocNo;

    /** 商户分账订单号 */
    @Schema(description = "商户分账订单号")
    private String bizAllocNo;

    /**
     * 分账状态
     * @see AllocTransactionStatusEnum
     */
    @Schema(description = "分账状态")
    private String status;
}
