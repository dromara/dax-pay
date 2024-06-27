package cn.daxpay.single.core.param.payment.allocation;

import cn.daxpay.single.core.param.PaymentCommonParam;
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
public class QueryAllocOrderParam extends PaymentCommonParam {

    @Schema(description = "分账单号")
    private String allocNo;

    @Schema(description = "商户分账单号")
    private String bizAllocNo;
}
