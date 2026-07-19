package cn.daxpay.open.payment.trade.report.dao;

import cn.daxpay.open.payment.trade.order.entity.PayTrade;
import cn.daxpay.open.payment.trade.report.result.AmountRangeItemResult;
import cn.daxpay.open.payment.trade.report.result.HourlyDistItemResult;
import cn.daxpay.open.payment.trade.report.result.MerchantRankItemResult;
import cn.daxpay.open.payment.trade.report.result.ProviderDistItemResult;
import cn.daxpay.open.payment.trade.report.result.ProviderSuccessItemResult;
import cn.daxpay.open.payment.trade.report.result.RefundTrendItemResult;
import cn.daxpay.open.payment.trade.report.result.TradeOverviewResult;
import cn.daxpay.open.payment.trade.report.result.TradeTrendItemResult;
import com.github.yulichang.base.MPJBaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.time.OffsetDateTime;
import java.util.List;

/// # 交易聚合统计 Mapper
///
/// 工作台仪表盘 + 分析页专用聚合查询, 直查 [PayTrade] 所在的 `pay_trade` 与 `pay_refund_order` 表,
/// 商户排名 JOIN `mch_info` 取商户名。
///
/// ## 成交口径
/// 以 `pay_trade` 资金凭证为准, 资金态 SUCCESS 且 `posted_amount > 0` 才计入成交;
/// `posted_amount` 对结算类动作(normal/gateway/capture 等)= 金额, 对预授权冻结恒为 0,
/// 以此自动排除冻结类资金动作(见 [PayTrade] 字段说明)。
///
/// ## 时间字段口径差异(重要)
/// - **成交相关**(`success_amount/success_count`, 退款金额趋势等): 按 `pay_time` 过滤
///   (支付成功时间), 失败/关闭单 pay_time 为 NULL, 自然落在本区间外。
/// - **总下单笔数**(`total_orders`, 用于成功率分母): 按 `create_time` 过滤(创建时间),
///   因为失败/关闭单也需计入分母。两者口径不同, 同一笔单的 create_time 与 pay_time
///   可能跨区间, 因此 `success_rate = success_count / total_orders` 仅作运营参考。
///
/// ## 时区处理
/// 数据库时间列为 `timestamptz`, MetaObjectHandler 按 UTC 写入。
/// 概览/渠道分布用 `pay_time BETWEEN` 做瞬时范围比较(OffsetDateTime 即时比较, 无歧义);
/// 趋势/时段/退款趋势按业务时区(Asia/Shanghai)日或小时分组, 用
/// `pay_time AT TIME ZONE 'Asia/Shanghai'` 转换后截取, 确保接近 CST 日界线的交易归入正确业务日。
///
/// ## 金额单位
/// 所有金额字段为分(最小货币单位), SUM 结果 `::bigint` 强转确保映射为 Java Long。
@Mapper
public interface TradeReportMapper extends MPJBaseMapper<PayTrade> {

    // ===== 概览 =====

    /// 成交聚合: 按 pay_time 过滤, 填充 success_amount / success_count
    @Select("SELECT COALESCE(SUM(posted_amount), 0)::bigint AS success_amount, "
            + "COUNT(*)::bigint AS success_count "
            + "FROM pay_trade "
            + "WHERE status = 'success' AND posted_amount > 0 "
            + "AND pay_time >= #{start} AND pay_time < #{end}")
    TradeOverviewResult successAggregate(
            @Param("start") OffsetDateTime start,
            @Param("end") OffsetDateTime end);

    /// 总下单笔数聚合: 按 create_time 过滤, 填充 total_orders(用于成功率分母)
    /// 注意口径与 success_count 不同(见类注释"时间字段口径差异")
    @Select("SELECT COUNT(*)::bigint AS total_orders "
            + "FROM pay_trade "
            + "WHERE create_time >= #{start} AND create_time < #{end}")
    TradeOverviewResult totalOrdersAggregate(
            @Param("start") OffsetDateTime start,
            @Param("end") OffsetDateTime end);

    /// 退款聚合: pay_refund_order 成功, 按 finish_time 范围, 填充 refund_amount / refund_count
    @Select("SELECT COALESCE(SUM(amount), 0)::bigint AS refund_amount, "
            + "COUNT(*)::bigint AS refund_count "
            + "FROM pay_refund_order "
            + "WHERE status = 'success' "
            + "AND finish_time >= #{start} AND finish_time < #{end}")
    TradeOverviewResult refundAggregate(
            @Param("start") OffsetDateTime start,
            @Param("end") OffsetDateTime end);

    // ===== 趋势 =====

    /// 成交趋势: 按 CST 业务日分组, 返回每日成交金额(分)与笔数, 仅含有成交的日期(无成交日由 Service 补零)
    @Select("SELECT TO_CHAR(pay_time AT TIME ZONE 'Asia/Shanghai', 'YYYY-MM-DD') AS date, "
            + "COALESCE(SUM(posted_amount), 0)::bigint AS amount, "
            + "COUNT(*)::bigint AS count "
            + "FROM pay_trade "
            + "WHERE status = 'success' AND posted_amount > 0 "
            + "AND pay_time >= #{start} AND pay_time < #{end} "
            + "GROUP BY 1 ORDER BY 1")
    List<TradeTrendItemResult> trend(
            @Param("start") OffsetDateTime start,
            @Param("end") OffsetDateTime end);

    /// 退款趋势: 按 CST 业务日分组, 返回每日退款金额(分)与笔数, 仅含有退款的日期(Service 补零)
    @Select("SELECT TO_CHAR(finish_time AT TIME ZONE 'Asia/Shanghai', 'YYYY-MM-DD') AS date, "
            + "COALESCE(SUM(amount), 0)::bigint AS amount, "
            + "COUNT(*)::bigint AS count "
            + "FROM pay_refund_order "
            + "WHERE status = 'success' "
            + "AND finish_time >= #{start} AND finish_time < #{end} "
            + "GROUP BY 1 ORDER BY 1")
    List<RefundTrendItemResult> refundTrend(
            @Param("start") OffsetDateTime start,
            @Param("end") OffsetDateTime end);

    // ===== 渠道分布 =====

    /// 支付渠道分布: 按支付渠道(provider code)分组, 返回各渠道成交金额(分)与笔数, 按金额倒序
    /// provider 为支付渠道编码(如 wechat/alipay/union_pay), 取自资金凭证冗余字段(权威在容器 provider)
    @Select("SELECT provider, "
            + "COALESCE(SUM(posted_amount), 0)::bigint AS amount, "
            + "COUNT(*)::bigint AS count "
            + "FROM pay_trade "
            + "WHERE status = 'success' AND posted_amount > 0 "
            + "AND provider IS NOT NULL "
            + "AND pay_time >= #{start} AND pay_time < #{end} "
            + "GROUP BY provider ORDER BY amount DESC")
    List<ProviderDistItemResult> providerDist(
            @Param("start") OffsetDateTime start,
            @Param("end") OffsetDateTime end);

    /// 支付渠道成功率: 按 provider 分组, 成功率 = success_count / total_count * 100
    /// total_count 含所有非初始化态(success/fail/close/cancel)的单, 按 create_time 过滤(与 totalOrders 同口径)
    /// NULLIF 防止除零, 结果四舍五入保留 1 位小数
    @Select("SELECT provider, "
            + "ROUND(COUNT(*) FILTER (WHERE status = 'success' AND posted_amount > 0)::numeric "
            + "      / NULLIF(COUNT(*), 0) * 100, 1) AS rate "
            + "FROM pay_trade "
            + "WHERE provider IS NOT NULL "
            + "AND create_time >= #{start} AND create_time < #{end} "
            + "GROUP BY provider "
            + "HAVING COUNT(*) > 0 "
            + "ORDER BY rate DESC")
    List<ProviderSuccessItemResult> providerSuccess(
            @Param("start") OffsetDateTime start,
            @Param("end") OffsetDateTime end);

    // ===== 时段分布 =====

    /// 24 小时时段分布: 按 CST 小时(0-23)分组, 返回各时段成交金额(分)与笔数
    /// 仅含有成交的小时(Service 补齐 0-23 缺失时段)
    @Select("SELECT EXTRACT(HOUR FROM pay_time AT TIME ZONE 'Asia/Shanghai')::int AS hour, "
            + "COALESCE(SUM(posted_amount), 0)::bigint AS amount, "
            + "COUNT(*)::bigint AS count "
            + "FROM pay_trade "
            + "WHERE status = 'success' AND posted_amount > 0 "
            + "AND pay_time >= #{start} AND pay_time < #{end} "
            + "GROUP BY 1 ORDER BY 1")
    List<HourlyDistItemResult> hourlyDist(
            @Param("start") OffsetDateTime start,
            @Param("end") OffsetDateTime end);

    // ===== 金额区间分桶 =====

    /// 金额区间分桶: 按 amount(订单金额, 分) CASE 分桶, 桶口径与前端 analytics 一致
    /// 桶定义: 0-50 / 50-200 / 200-1000 / 1000-5000 / 5000+ (单位: 元, 即 0-5000 / 5000-20000 / ... 分)
    /// 仅含成功单(status='success' AND posted_amount>0), 按 pay_time 过滤
    /// bucket 字段用固定 key 返回(如 '0-50'), Service 负责补齐 5 个缺失桶
    @Select("SELECT CASE "
            + "WHEN amount < 5000 THEN '0-50' "
            + "WHEN amount < 20000 THEN '50-200' "
            + "WHEN amount < 100000 THEN '200-1000' "
            + "WHEN amount < 500000 THEN '1000-5000' "
            + "ELSE '5000+' END AS bucket, "
            + "COUNT(*)::bigint AS count "
            + "FROM pay_trade "
            + "WHERE status = 'success' AND posted_amount > 0 "
            + "AND pay_time >= #{start} AND pay_time < #{end} "
            + "GROUP BY 1")
    List<AmountRangeItemResult> amountRange(
            @Param("start") OffsetDateTime start,
            @Param("end") OffsetDateTime end);

    // ===== 商户排名 =====

    /// 商户交易额排名: 按 mch_no 分组, JOIN mch_info 取商户名, 按成交金额倒序
    /// 仅含成功单(status='success' AND posted_amount>0), 按 pay_time 过滤
    /// proportion(占比)由 Service 计算(基于本期总成交额)
    /// @param limit 返回前 N 名(通常 10)
    @Select("SELECT t.mch_no AS mchNo, m.mch_name AS merchantName, "
            + "COALESCE(SUM(t.posted_amount), 0)::bigint AS amount, "
            + "COUNT(*)::bigint AS orders "
            + "FROM pay_trade t "
            + "LEFT JOIN mch_info m ON m.mch_no = t.mch_no AND m.deleted = false "
            + "WHERE t.status = 'success' AND t.posted_amount > 0 "
            + "AND t.pay_time >= #{start} AND t.pay_time < #{end} "
            + "GROUP BY t.mch_no, m.mch_name "
            + "ORDER BY amount DESC "
            + "LIMIT #{limit}")
    List<MerchantRankItemResult> merchantRank(
            @Param("start") OffsetDateTime start,
            @Param("end") OffsetDateTime end,
            @Param("limit") int limit);
}
