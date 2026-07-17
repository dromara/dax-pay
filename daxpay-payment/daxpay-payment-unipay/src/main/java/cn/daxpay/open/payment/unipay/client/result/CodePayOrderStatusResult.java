package cn.daxpay.open.payment.unipay.client.result;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

/// # 码牌订单状态(公开脱敏)
@Data
@Accessors(chain = true)
@Schema(title = "码牌订单状态")
public class CodePayOrderStatusResult {

    @Schema(description = "平台业务单号")
    private String orderNo;

    /// @see cn.daxpay.open.payment.trade.enums.NormalPayOrderStatusEnum
    @Schema(description = "订单状态")
    private String status;

    @Schema(description = "金额(分)")
    private Long amount;

    @Schema(description = "标题")
    private String title;
}
