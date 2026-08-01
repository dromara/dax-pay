package cn.daxpay.open.payment.trade.report.param;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

/// # 交易统计区间查询参数
///
/// 工作台/分析页聚合统计的统一查询条件, 适用于运营端/商户端/移动端 Dashboard。
/// 作为 GET 请求的查询对象, 由 Spring 自动绑定 query string。
///
/// ## 参数约定
/// - **天数模式**: [days] 参数(近 N 天含今天)
/// - **区间模式**: [start] + [end] 参数(yyyy-MM-dd, 均包含)
/// - 两者二选一: 同时传时优先区间模式
///   (由 [cn.daxpay.open.payment.trade.report.support.TradeReportSupport#resolveRange] 统一解析)
@Data
@Accessors(chain = true)
@Schema(title = "交易统计区间查询参数")
public class TradeRangeQuery {

    /// 快捷模式日期: today / yesterday(仅 overview 接口使用)
    @Schema(description = "快捷模式日期: today / yesterday")
    private String date;

    /// 区间模式开始日期(yyyy-MM-dd, 包含)
    @Schema(description = "区间开始日期(yyyy-MM-dd)")
    private String start;

    /// 区间模式结束日期(yyyy-MM-dd, 包含)
    @Schema(description = "区间结束日期(yyyy-MM-dd)")
    private String end;

    /// 天数模式: 近 N 天含今天(为空时由解析器按 7 天兜底)
    @Schema(description = "近 N 天含今天")
    private Integer days;

    /// 排行榜返回条数(为空时由解析器按 10 兜底, 上限 50)
    @Schema(description = "排行榜返回条数")
    private Integer limit;

    /// 维度: channelMch / app / store(仅维度排行接口使用, 为空时按 app 兜底)
    @Schema(description = "维度: channelMch / app / store")
    private String dim;
}
