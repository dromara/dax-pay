package cn.daxpay.open.payment.unipay.result.trade.refund;

import cn.daxpay.open.payment.trade.enums.RefundOrderStatusEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

/// # 统一退款响应参数
///
@Data
@Accessors(chain = true)
@Schema(title = "统一退款响应参数")
public class RefundResult {

    /// 平台退款号
    @Schema(description = "平台退款号")
    private String refundNo;

    /// 商户退款号
    @Schema(description = "商户退款号")
    private String bizRefundNo;

    /// 退款状态
    /// @see RefundOrderStatusEnum
    @Schema(description = "退款状态")
    private String status;

    /// 错误信息(失败时返回)
    @Schema(description = "错误信息")
    private String errorMsg;

}
