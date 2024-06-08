package cn.daxpay.single.result.allocation;

import cn.daxpay.single.code.AllocOrderStatusEnum;
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

    /** 分账订单号 */
    @Schema(description = "分账订单号")
    private String allocNo;

    /** 商户分账订单号 */
    @Schema(description = "商户分账订单号")
    private String bizAllocNo;

    /**
     * 分账状态
     * @see AllocOrderStatusEnum
     */
    @Schema(description = "分账状态")
    private String status;
}
