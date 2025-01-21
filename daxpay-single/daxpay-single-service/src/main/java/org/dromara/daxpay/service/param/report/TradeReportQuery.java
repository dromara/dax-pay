package org.dromara.daxpay.service.param.report;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDate;

/**
 * 交易报表查询参数
 * @author xxm
 * @since 2024/11/17
 */
@Data
@Accessors(chain = true)
@Schema(title = "交易报表查询参数")
public class TradeReportQuery {

    /** 开始日期 */
    @Schema(title = "开始日期")
    private LocalDate startDate;

    /** 结束日期 */
    @Schema(title = "结束日期")
    private LocalDate endDate;
}
