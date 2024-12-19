package org.dromara.daxpay.core.param.allocation.order;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.dromara.daxpay.core.param.PaymentCommonParam;

/**
 * 分账订单查询参数
 * @author xxm
 * @since 2024/4/7
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Schema(title = "支付订单查询参数")
public class QueryAllocOrderParam extends PaymentCommonParam {

    @Schema(description = "分账单号")
    private String allocNo;

    @Schema(description = "商户分账单号")
    private String bizAllocNo;
}
