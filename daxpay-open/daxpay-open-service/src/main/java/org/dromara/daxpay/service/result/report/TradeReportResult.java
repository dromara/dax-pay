package org.dromara.daxpay.service.result.report;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

import java.math.BigDecimal;

/**
 * 交易报表结果
 * @author xxm
 * @since 2024/11/17
 */
@Data
@Accessors(chain = true)
@Schema(title = "交易报表结果")
public class TradeReportResult {

    @Schema(description = "标题")
    private String title;

    @Schema(description = "交易金额")
    private BigDecimal tradeAmount;

    @Schema(description = "交易笔数")
    private Integer tradeCount;

    @Schema(description = "最大单笔金额")
    private BigDecimal maxAmount;

    @Schema(description = "平均单笔金额")
    private BigDecimal avgAmount;


}
