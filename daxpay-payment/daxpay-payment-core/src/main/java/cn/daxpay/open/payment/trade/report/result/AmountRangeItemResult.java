package cn.daxpay.open.payment.trade.report.result;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

/// # 金额区间分桶单项结果
///
/// 成交订单按金额区间分桶统计笔数, 金额单位分, 桶口径见 Mapper SQL 的 CASE WHEN.
/// Service 层负责补齐所有预定义桶, 保证前端柱图完整。
@Data
@Accessors(chain = true)
@Schema(title = "金额区间分桶单项")
public class AmountRangeItemResult {

    /// 区间标签(如 '0-50', '5000+')
    @Schema(description = "区间标签")
    private String bucket;

    /// 笔数
    @Schema(description = "笔数")
    private Long count;
}
