package cn.daxpay.open.payment.merchant.dao.dashboard;

import cn.daxpay.open.payment.trade.order.entity.PayTrade;
import cn.daxpay.open.payment.trade.report.result.AmountRangeItemResult;
import cn.daxpay.open.payment.trade.report.result.DimRankItemResult;
import cn.daxpay.open.payment.trade.report.result.HourlyDistItemResult;
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

/// # 商户端交易聚合统计 Mapper
///
/// 强制 `AND mch_no = #{mchNo}`, 禁止可空全平台语义。含维度排名; 不含运营端商户排名。
///
/// ## 成交口径
/// 以 `pay_trade` 资金凭证为准, 资金态 SUCCESS 且 `posted_amount > 0` 才计入成交。
///
/// ## 金额单位
/// 所有金额字段为分(最小货币单位), SUM 结果 `::bigint` 强转确保映射为 Java Long。
@Mapper
public interface MchTradeReportMapper extends MPJBaseMapper<PayTrade> {

    // ===== 概览 =====

    /// 成交聚合: 按 pay_time 过滤, 填充 success_amount / success_count
    @Select("""
            SELECT COALESCE(SUM(posted_amount), 0)::bigint AS success_amount,
                   COUNT(*)::bigint AS success_count
            FROM pay_trade
            WHERE status = 'success' AND posted_amount > 0
              AND pay_time >= #{start} AND pay_time < #{end}
              AND mch_no = #{mchNo}
            """)
    TradeOverviewResult successAggregate(
            @Param("start") OffsetDateTime start,
            @Param("end") OffsetDateTime end,
            @Param("mchNo") String mchNo);

    /// 总下单笔数聚合: 按 create_time 过滤, 填充 total_orders
    @Select("""
            SELECT COUNT(*)::bigint AS total_orders
            FROM pay_trade
            WHERE create_time >= #{start} AND create_time < #{end}
              AND mch_no = #{mchNo}
            """)
    TradeOverviewResult totalOrdersAggregate(
            @Param("start") OffsetDateTime start,
            @Param("end") OffsetDateTime end,
            @Param("mchNo") String mchNo);

    /// 退款聚合
    @Select("""
            SELECT COALESCE(SUM(amount), 0)::bigint AS refund_amount,
                   COUNT(*)::bigint AS refund_count
            FROM pay_refund_order
            WHERE status = 'success'
              AND finish_time >= #{start} AND finish_time < #{end}
              AND mch_no = #{mchNo}
            """)
    TradeOverviewResult refundAggregate(
            @Param("start") OffsetDateTime start,
            @Param("end") OffsetDateTime end,
            @Param("mchNo") String mchNo);

    // ===== 趋势 =====

    @Select("""
            SELECT TO_CHAR(pay_time AT TIME ZONE 'Asia/Shanghai', 'YYYY-MM-DD') AS date,
                   COALESCE(SUM(posted_amount), 0)::bigint AS amount,
                   COUNT(*)::bigint AS count
            FROM pay_trade
            WHERE status = 'success' AND posted_amount > 0
              AND pay_time >= #{start} AND pay_time < #{end}
              AND mch_no = #{mchNo}
            GROUP BY 1 ORDER BY 1
            """)
    List<TradeTrendItemResult> trend(
            @Param("start") OffsetDateTime start,
            @Param("end") OffsetDateTime end,
            @Param("mchNo") String mchNo);

    @Select("""
            SELECT TO_CHAR(finish_time AT TIME ZONE 'Asia/Shanghai', 'YYYY-MM-DD') AS date,
                   COALESCE(SUM(amount), 0)::bigint AS amount,
                   COUNT(*)::bigint AS count
            FROM pay_refund_order
            WHERE status = 'success'
              AND finish_time >= #{start} AND finish_time < #{end}
              AND mch_no = #{mchNo}
            GROUP BY 1 ORDER BY 1
            """)
    List<RefundTrendItemResult> refundTrend(
            @Param("start") OffsetDateTime start,
            @Param("end") OffsetDateTime end,
            @Param("mchNo") String mchNo);

    // ===== 渠道分布 =====

    @Select("""
            SELECT provider,
                   COALESCE(SUM(posted_amount), 0)::bigint AS amount,
                   COUNT(*)::bigint AS count
            FROM pay_trade
            WHERE status = 'success' AND posted_amount > 0
              AND provider IS NOT NULL
              AND pay_time >= #{start} AND pay_time < #{end}
              AND mch_no = #{mchNo}
            GROUP BY provider ORDER BY amount DESC
            """)
    List<ProviderDistItemResult> providerDist(
            @Param("start") OffsetDateTime start,
            @Param("end") OffsetDateTime end,
            @Param("mchNo") String mchNo);

    @Select("""
            SELECT provider,
                   ROUND(COUNT(*) FILTER (WHERE status = 'success' AND posted_amount > 0)::numeric
                         / NULLIF(COUNT(*), 0) * 100, 1) AS rate
            FROM pay_trade
            WHERE provider IS NOT NULL
              AND create_time >= #{start} AND create_time < #{end}
              AND mch_no = #{mchNo}
            GROUP BY provider
            HAVING COUNT(*) > 0
            ORDER BY rate DESC
            """)
    List<ProviderSuccessItemResult> providerSuccess(
            @Param("start") OffsetDateTime start,
            @Param("end") OffsetDateTime end,
            @Param("mchNo") String mchNo);

    // ===== 时段分布 =====

    @Select("""
            SELECT EXTRACT(HOUR FROM pay_time AT TIME ZONE 'Asia/Shanghai')::int AS hour,
                   COALESCE(SUM(posted_amount), 0)::bigint AS amount,
                   COUNT(*)::bigint AS count
            FROM pay_trade
            WHERE status = 'success' AND posted_amount > 0
              AND pay_time >= #{start} AND pay_time < #{end}
              AND mch_no = #{mchNo}
            GROUP BY 1 ORDER BY 1
            """)
    List<HourlyDistItemResult> hourlyDist(
            @Param("start") OffsetDateTime start,
            @Param("end") OffsetDateTime end,
            @Param("mchNo") String mchNo);

    // ===== 金额区间分桶 =====

    @Select("""
            SELECT CASE
                     WHEN amount < 5000 THEN '0-50'
                     WHEN amount < 20000 THEN '50-200'
                     WHEN amount < 100000 THEN '200-1000'
                     WHEN amount < 500000 THEN '1000-5000'
                     ELSE '5000+' END AS bucket,
                   COUNT(*)::bigint AS count
            FROM pay_trade
            WHERE status = 'success' AND posted_amount > 0
              AND pay_time >= #{start} AND pay_time < #{end}
              AND mch_no = #{mchNo}
            GROUP BY 1
            """)
    List<AmountRangeItemResult> amountRange(
            @Param("start") OffsetDateTime start,
            @Param("end") OffsetDateTime end,
            @Param("mchNo") String mchNo);

    // ===== 维度排名 =====

    /// 按通道商户号分组排名
    @Select("""
            SELECT t.channel_mch_no AS dimKey,
                   COALESCE(cm.channel_merchant_name, t.channel_mch_no) AS dimName,
                   COALESCE(SUM(t.posted_amount), 0)::bigint AS amount,
                   COUNT(*)::bigint AS orders
            FROM pay_trade t
            LEFT JOIN mch_channel_merchant cm
              ON cm.channel_mch_no = t.channel_mch_no AND cm.mch_no = t.mch_no AND cm.deleted = false
            WHERE t.status = 'success' AND t.posted_amount > 0
              AND t.channel_mch_no IS NOT NULL AND t.channel_mch_no <> ''
              AND t.pay_time >= #{start} AND t.pay_time < #{end}
              AND t.mch_no = #{mchNo}
            GROUP BY t.channel_mch_no, cm.channel_merchant_name
            ORDER BY amount DESC
            LIMIT #{limit}
            """)
    List<DimRankItemResult> dimRankByChannelMch(
            @Param("start") OffsetDateTime start,
            @Param("end") OffsetDateTime end,
            @Param("mchNo") String mchNo,
            @Param("limit") int limit);

    /// 按应用分组排名
    @Select("""
            SELECT t.app_id AS dimKey,
                   COALESCE(a.app_name, t.app_id) AS dimName,
                   COALESCE(SUM(t.posted_amount), 0)::bigint AS amount,
                   COUNT(*)::bigint AS orders
            FROM pay_trade t
            LEFT JOIN mch_app_info a
              ON a.app_id = t.app_id AND a.mch_no = t.mch_no AND a.deleted = false
            WHERE t.status = 'success' AND t.posted_amount > 0
              AND t.app_id IS NOT NULL AND t.app_id <> ''
              AND t.pay_time >= #{start} AND t.pay_time < #{end}
              AND t.mch_no = #{mchNo}
            GROUP BY t.app_id, a.app_name
            ORDER BY amount DESC
            LIMIT #{limit}
            """)
    List<DimRankItemResult> dimRankByApp(
            @Param("start") OffsetDateTime start,
            @Param("end") OffsetDateTime end,
            @Param("mchNo") String mchNo,
            @Param("limit") int limit);

    /// 按门店分组排名; 空 store_no 归到 dimKey='' (前端展示「未指定门店」)
    @Select("""
            SELECT COALESCE(NULLIF(t.store_no, ''), '') AS dimKey,
                   s.store_name AS dimName,
                   COALESCE(SUM(t.posted_amount), 0)::bigint AS amount,
                   COUNT(*)::bigint AS orders
            FROM pay_trade t
            LEFT JOIN mch_store_info s
              ON s.store_no = t.store_no AND s.mch_no = t.mch_no AND s.deleted = false
            WHERE t.status = 'success' AND t.posted_amount > 0
              AND t.pay_time >= #{start} AND t.pay_time < #{end}
              AND t.mch_no = #{mchNo}
            GROUP BY COALESCE(NULLIF(t.store_no, ''), ''), s.store_name
            ORDER BY amount DESC
            LIMIT #{limit}
            """)
    List<DimRankItemResult> dimRankByStore(
            @Param("start") OffsetDateTime start,
            @Param("end") OffsetDateTime end,
            @Param("mchNo") String mchNo,
            @Param("limit") int limit);
}
