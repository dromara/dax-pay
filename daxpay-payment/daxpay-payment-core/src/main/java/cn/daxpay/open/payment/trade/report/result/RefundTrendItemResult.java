package cn.daxpay.open.payment.trade.report.result;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

/// # 退款趋势单项结果
///
/// 一个日期点对应的退款金额与笔数, 日期格式 yyyy-MM-dd, 金额单位为分。
/// Service 层负责对查询区间内无退款的日期补零。
@Data
@Accessors(chain = true)
@Schema(title = "退款趋势单项")
public class RefundTrendItemResult {

    /// 日期(yyyy-MM-dd)
    @Schema(description = "日期(yyyy-MM-dd)")
    private String date;

    /// 退款金额(分)
    @Schema(description = "退款金额(分)")
    private Long amount;

    /// 退款笔数
    @Schema(description = "退款笔数")
    private Long count;
}
