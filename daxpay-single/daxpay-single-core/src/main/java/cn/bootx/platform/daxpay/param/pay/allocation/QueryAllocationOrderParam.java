package cn.bootx.platform.daxpay.param.pay.allocation;

import cn.bootx.platform.daxpay.param.PaymentCommonParam;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 分账订单查询参数
 * @author xxm
 * @since 2024/4/7
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Schema(title = "支付订单查询参数")
public class QueryAllocationOrderParam extends PaymentCommonParam {

    @Schema(description = "分账订单ID")
    private Long orderId;

    @Schema(description = "分账单号")
    private String allocationNo;
}
