package cn.daxpay.single.param.payment.allocation;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 分账同步请求参数
 * @author xxm
 * @since 2024/4/12
 */
@Data
@Schema(title = "分账同步请求参数")
public class AllocSyncParam {

    @Schema(description = "分账号")
    private String allocationNo;

    @Schema(description = "商户分账号")
    private String bizAllocationNo;
}
