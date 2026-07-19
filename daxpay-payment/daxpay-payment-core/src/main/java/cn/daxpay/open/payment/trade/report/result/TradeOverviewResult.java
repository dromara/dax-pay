package cn.daxpay.open.payment.trade.report.result;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

/// # 交易概览统计结果
///
/// 成交与退款核心指标聚合, 含总下单笔数(用于成功率) 与上一周期对比字段(用于环比).
/// 金额单位为分(最小货币单位), 前端按需转元展示.
///
/// ## 字段拆分
/// - **本期字段**(`success/refund/total*`): 由当前查询区间聚合
/// - **上期字段**(`prev*`): 由与本区间等长的前一区间聚合, 前端用
///   `(curr - prev) / prev * 100` 计算环比百分比; 上期无数据(prevCount=0)时前端不应展示环比
///
/// 由 [cn.daxpay.open.payment.trade.report.dao.TradeReportMapper] 的成交/退款两个查询分别填充
/// 各自一半字段, Service 合并.
@Data
@Accessors(chain = true)
@Schema(title = "交易概览统计")
public class TradeOverviewResult {

    // ===== 本期 =====

    /// 成交金额(分)
    @Schema(description = "成交金额(分)")
    private Long successAmount;

    /// 成交笔数
    @Schema(description = "成交笔数")
    private Long successCount;

    /// 退款金额(分)
    @Schema(description = "退款金额(分)")
    private Long refundAmount;

    /// 退款笔数
    @Schema(description = "退款笔数")
    private Long refundCount;

    /// 总下单笔数(含成功/失败/关闭, 用于计算成功率)
    @Schema(description = "总下单笔数")
    private Long totalOrders;

    // ===== 上期(用于环比, 任一字段为 null 表示上期无数据) =====

    /// 上期成交金额(分)
    @Schema(description = "上期成交金额(分)")
    private Long prevSuccessAmount;

    /// 上期成交笔数
    @Schema(description = "上期成交笔数")
    private Long prevSuccessCount;

    /// 上期退款金额(分)
    @Schema(description = "上期退款金额(分)")
    private Long prevRefundAmount;

    /// 上期退款笔数
    @Schema(description = "上期退款笔数")
    private Long prevRefundCount;

    /// 上期总下单笔数
    @Schema(description = "上期总下单笔数")
    private Long prevTotalOrders;
}
