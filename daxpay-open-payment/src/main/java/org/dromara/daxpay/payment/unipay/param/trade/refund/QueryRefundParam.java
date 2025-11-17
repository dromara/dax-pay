package org.dromara.daxpay.payment.unipay.param.trade.refund;

import org.dromara.daxpay.payment.unipay.param.MerchantPaymentCommonParam;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Size;
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
public class QueryRefundParam extends MerchantPaymentCommonParam {

    /** 退款号 */
    @Schema(description = "退款号")
    @Size(max = 100, message = "退款号不可超过100位")
    private String refundNo;

    /** 商户退款号 */
    @Schema(description = "商户退款号")
    @Size(max = 100, message = "商户退款号不可超过100位")
    private String bizRefundNo;
}
