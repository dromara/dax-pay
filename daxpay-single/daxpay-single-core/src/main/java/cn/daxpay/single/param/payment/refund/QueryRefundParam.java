package cn.daxpay.single.param.payment.refund;

import cn.daxpay.single.param.PaymentCommonParam;
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

    /** 退款号 */
    @Schema(description = "退款号")
    private String refundNo;

    /** 商户退款号 */
    @Schema(description = "商户退款号")
    private String bizRefundNo;
}
