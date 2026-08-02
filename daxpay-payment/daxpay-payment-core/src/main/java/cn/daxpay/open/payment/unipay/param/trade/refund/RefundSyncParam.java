package cn.daxpay.open.payment.unipay.param.trade.refund;

import cn.daxpay.open.payment.unipay.param.MerchantPaymentCommonParam;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.EqualsAndHashCode;

/// # 退款状态同步参数
///
/// 主动查询通道网关方退款终态并回写本地退款单。
/// 平台退款号与商户退款号至少传一个, 优先使用平台退款号。
@EqualsAndHashCode(callSuper = true)
@Data
@Schema(title = "退款状态同步参数")
public class RefundSyncParam extends MerchantPaymentCommonParam {

    /// 平台退款号
    @Schema(description = "平台退款号")
    @Size(max = 100, message = "{validation.field.refundNo.size}")
    private String refundNo;

    /// 商户退款号
    @Schema(description = "商户退款号")
    @Size(max = 100, message = "{validation.field.bizRefundNo.size}")
    private String bizRefundNo;

}
