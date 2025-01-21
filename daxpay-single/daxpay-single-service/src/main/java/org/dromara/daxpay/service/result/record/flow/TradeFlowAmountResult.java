package org.dromara.daxpay.service.result.record.flow;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

import java.math.BigDecimal;

/**
 * 各类型流水汇总金额
 * @author xxm
 * @since 2024/8/19
 */
@Data
@Accessors(chain = true)
@Schema(title = "各类型流水汇总金额")
public class TradeFlowAmountResult {
    @Schema(description = "收入金额")
    private BigDecimal incomeAmount;
    @Schema(description = "退款金额")
    private BigDecimal refundAmount;
    @Schema(description = "转账金额")
    private BigDecimal transferAmount;
}
