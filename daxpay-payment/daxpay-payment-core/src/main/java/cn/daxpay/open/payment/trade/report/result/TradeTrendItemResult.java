package cn.daxpay.open.payment.trade.report.result;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

/// # 交易趋势单项结果
///
/// 一个日期点对应的成交金额与笔数, 日期格式 yyyy-MM-dd, 金额单位为分。
/// Service 层负责对查询区间内无成交的日期补零, 保证前端折线图连续。
@Data
@Accessors(chain = true)
@Schema(title = "交易趋势单项")
public class TradeTrendItemResult {

    /// 日期(yyyy-MM-dd)
    @Schema(description = "日期(yyyy-MM-dd)")
    private String date;

    /// 成交金额(分)
    @Schema(description = "成交金额(分)")
    private Long amount;

    /// 成交笔数
    @Schema(description = "成交笔数")
    private Long count;
}
