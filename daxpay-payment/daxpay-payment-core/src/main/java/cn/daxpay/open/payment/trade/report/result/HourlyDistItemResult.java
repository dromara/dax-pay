package cn.daxpay.open.payment.trade.report.result;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

/// # 时段分布单项结果
///
/// 按业务时区 Asia/Shanghai 取 HOUR(0-23) 在所选区间内汇总后, 由 Service 除以区间天数得到**日均**。
/// Service 层负责补齐 0-23 缺失小时, 保证前端柱图完整。
@Data
@Accessors(chain = true)
@Schema(title = "时段分布单项(日均)")
public class HourlyDistItemResult {

    /// 小时(0-23)
    @Schema(description = "小时(0-23)")
    private Integer hour;

    /// 日均成交金额(分, 四舍五入)
    @Schema(description = "日均成交金额(分)")
    private Long amount;

    /// 日均成交笔数(保留 1 位小数)
    @Schema(description = "日均成交笔数")
    private Double count;
}
