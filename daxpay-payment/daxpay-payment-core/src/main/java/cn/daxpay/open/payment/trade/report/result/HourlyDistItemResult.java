package cn.daxpay.open.payment.trade.report.result;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

/// # 时段分布单项结果
///
/// 一小时区间内的成交金额与笔数(按业务时区 Asia/Shanghai 取 HOUR).
/// Service 层负责补齐 0-23 缺失小时, 保证前端柱图完整。
@Data
@Accessors(chain = true)
@Schema(title = "时段分布单项")
public class HourlyDistItemResult {

    /// 小时(0-23)
    @Schema(description = "小时(0-23)")
    private Integer hour;

    /// 成交金额(分)
    @Schema(description = "成交金额(分)")
    private Long amount;

    /// 成交笔数
    @Schema(description = "成交笔数")
    private Long count;
}
