package cn.daxpay.open.payment.old.pay.result.record.flow;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

/// # 各类型流水汇总金额
///
@Data
@Accessors(chain = true)
@Schema(title = "各类型流水汇总金额")
public class TradeFlowAmountResult {
    @Schema(description = "收入金额(分)")
    private Long incomeAmount;
    @Schema(description = "退款金额(分)")
    private Long refundAmount;
    @Schema(description = "转账金额(分)")
    private Long transferAmount;
}
