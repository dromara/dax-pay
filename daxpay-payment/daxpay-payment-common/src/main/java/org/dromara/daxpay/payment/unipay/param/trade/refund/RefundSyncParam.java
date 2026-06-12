package org.dromara.daxpay.payment.unipay.param.trade.refund;

import org.dromara.daxpay.payment.unipay.param.MerchantPaymentCommonParam;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.EqualsAndHashCode;

/// # 退款状态同步参数
///
@EqualsAndHashCode(callSuper = true)
@Data
@Schema(title = "退款状态同步参数")
public class RefundSyncParam extends MerchantPaymentCommonParam {

    /// 退款号
    @Schema(description = "退款号")
    @Size(max = 100, message = "{validation.field.refundNo.size}")
    private String refundNo;

    /// 商户退款号
    @Schema(description = "商户退款号")
    @Size(max = 100, message = "{validation.field.bizRefundNo.size}")
    private String bizRefundNo;

}
