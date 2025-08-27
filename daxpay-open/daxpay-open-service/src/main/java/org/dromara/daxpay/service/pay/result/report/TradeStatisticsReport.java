package org.dromara.daxpay.service.pay.result.report;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * 交易统计信息
 * @author xxm
 * @since 2025/6/5
 */
@Data
@Accessors(chain = true)
@Schema(title = "交易统计信息")
public class TradeStatisticsReport {

    @Schema(description = "日期")
    private LocalDate localDate;

    @Schema(description = "支付金额")
    private BigDecimal payAmount;

    @Schema(description = "退款金额")
    private BigDecimal refundAmount;

    @Schema(description = "支付笔数")
    private Integer payCount;

    @Schema(description = "退款笔数")
    private Integer refundCount;
}
