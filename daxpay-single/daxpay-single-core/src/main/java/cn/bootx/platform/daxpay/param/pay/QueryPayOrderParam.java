package cn.bootx.platform.daxpay.param.pay;

import cn.bootx.platform.daxpay.param.PaymentCommonParam;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 支付订单查询参数
 * @author xxm
 * @since 2024/1/15
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Schema(title = "支付订单查询参数")
public class QueryPayOrderParam extends PaymentCommonParam {

    @Schema(description = "支付号")
    private Long paymentId;

    @Schema(description = "业务号")
    private String businessNo;

}
