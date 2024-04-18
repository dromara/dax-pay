package cn.bootx.platform.daxpay.param.payment.refund;

import cn.bootx.platform.daxpay.param.PaymentCommonParam;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 查询退款订单参数类
 * @author xxm
 * @since 2024/1/16
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Schema(title = "查询退款订单参数类")
public class QueryRefundParam extends PaymentCommonParam {

    @Schema(description = "退款号")
    private String refundNo;

    @Schema(description = "商户退款号")
    private String outRefundNo;
}
