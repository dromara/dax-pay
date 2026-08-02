package cn.daxpay.open.payment.unipay.param.trade.refund;

import cn.daxpay.open.payment.unipay.param.MerchantPaymentCommonParam;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.EqualsAndHashCode;

/// # 退款单查询参数
///
/// 平台退款号与商户退款号至少传一个, 优先使用平台退款号。
/// 仅查询本地退款单, 不调用通道; 需要实时通道状态请走退款同步接口。
@EqualsAndHashCode(callSuper = true)
@Data
@Schema(title = "退款单查询参数")
public class RefundOrderQueryParam extends MerchantPaymentCommonParam {

    /// 平台退款号
    @Schema(description = "平台退款号")
    @Size(max = 100, message = "{validation.field.refundNo.size}")
    private String refundNo;

    /// 商户退款号
    @Schema(description = "商户退款号")
    @Size(max = 100, message = "{validation.field.bizRefundNo.size}")
    private String bizRefundNo;

}
