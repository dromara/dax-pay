package cn.daxpay.open.payment.admin.service.dashboard;

import cn.daxpay.open.payment.trade.report.dao.TradeReportMapper;
import cn.daxpay.open.payment.trade.report.result.AmountRangeItemResult;
import cn.daxpay.open.payment.trade.report.result.HourlyDistItemResult;
import cn.daxpay.open.payment.trade.report.result.MerchantRankItemResult;
import cn.daxpay.open.payment.trade.report.result.ProviderDistItemResult;
import cn.daxpay.open.payment.trade.report.result.ProviderSuccessItemResult;
import cn.daxpay.open.payment.trade.report.result.RefundTrendItemResult;
import cn.daxpay.open.payment.trade.report.result.TradeOverviewResult;
import cn.daxpay.open.payment.trade.report.result.TradeTrendItemResult;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/// # 工作台/分析页交易统计服务
///
/// 编排 [TradeReportMapper] 的聚合查询, 处理业务时区(Asia/Shanghai)下的日界线计算、
/// 趋势/时段/分桶补零、商户排名占比计算、概览环比上一周期对比。
///
/// ## 时区
/// 数据库 `timestamptz` 按 UTC 写入, 这里以 Asia/Shanghai 为业务日界线计算时间区间,
/// 传给 Mapper 做即时比较, 确保接近 CST 日界线的交易归入正确的业务日。
///
/// ## 时间区间参数
/// 所有方法支持 (start, end) 半开区间, 与分析页的 dateRange 直接对应。
/// overview 额外计算"上一周期"(与本区间等长)用于环比, 前端可直接用 prev* / curr* 计算百分比。
@Slf4j
@Service
@RequiredArgsConstructor
public class DashboardTradeService {

    /// 业务时区(支付平台默认 Asia/Shanghai)
    private static final ZoneId ZONE_CST = ZoneId.of("Asia/Shanghai");
    private static final DateTimeFormatter DATE_FMT = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    /// 趋势/分析页天数上下限, 防止前端传参滥用导致大范围全表聚合
    private static final int TREND_DAYS_MIN = 1;
    private static final int TREND_DAYS_MAX = 365;

    /// 商户排名最大返回数
    private static final int MERCHANT_RANK_LIMIT_MAX = 50;
    private static final int MERCHANT_RANK_LIMIT_DEFAULT = 10;

    /// 金额区间分桶的固定顺序(Service 按此顺序补齐缺失桶)
    private static final List<String> AMOUNT_BUCKETS = List.of("0-50", "50-200", "200-1000", "1000-5000", "5000+");

    private final TradeReportMapper tradeReportMapper;

    // ===== 概览 =====

    /// 交易概览(今日/昨日)
    ///
    /// @param date "today" 或 "yesterday", 其它值按 today 处理
    public TradeOverviewResult overview(String date) {
        LocalDate today = LocalDate.now(ZONE_CST);
        LocalDate target = "yesterday".equalsIgnoreCase(date) ? today.minusDays(1) : today;
        // [start, end) 半开区间, end 指向次日 00:00 避免边界重复
        OffsetDateTime start = target.atStartOfDay(ZONE_CST).toOffsetDateTime();
        OffsetDateTime end = target.plusDays(1).atStartOfDay(ZONE_CST).toOffsetDateTime();
        // 上一周期: 同长度的前一天(单日概览时即昨日, 与 date=yesterday 不同语义, 这里 prev 用于环比)
        OffsetDateTime prevStart = start.minusDays(1);
        OffsetDateTime prevEnd = start;
        return aggregateOverview(start, end, prevStart, prevEnd);
    }

    /// 交易概览(自定义区间, 含上期对比)
    ///
    /// @param start 起始日期(yyyy-MM-dd, 包含), 业务时区 Asia/Shanghai
    /// @param end   结束日期(yyyy-MM-dd, **包含**), Service 内部转半开区间 [start, start+1day)
    public TradeOverviewResult overview(String start, String end) {
        OffsetDateTime startUtc = parseDateStart(start);
        OffsetDateTime endUtc = parseDateEndExclusive(end);
        // 上一周期: 与本区间等长, 紧接本区间之前
        long daysSpan = java.time.Duration.between(startUtc, endUtc).toDays();
        OffsetDateTime prevStart = startUtc.minusDays(daysSpan);
        OffsetDateTime prevEnd = startUtc;
        return aggregateOverview(startUtc, endUtc, prevStart, prevEnd);
    }

    /// 聚合概览(本/上期 6 次查询组装到一个 Result)
    private TradeOverviewResult aggregateOverview(
            OffsetDateTime start, OffsetDateTime end,
            OffsetDateTime prevStart, OffsetDateTime prevEnd) {
        // 本期
        TradeOverviewResult currSuccess = tradeReportMapper.successAggregate(start, end);
        TradeOverviewResult currTotal = tradeReportMapper.totalOrdersAggregate(start, end);
        TradeOverviewResult currRefund = tradeReportMapper.refundAggregate(start, end);
        // 上期(用于环比)
        TradeOverviewResult prevSuccess = tradeReportMapper.successAggregate(prevStart, prevEnd);
        TradeOverviewResult prevTotal = tradeReportMapper.totalOrdersAggregate(prevStart, prevEnd);
        TradeOverviewResult prevRefund = tradeReportMapper.refundAggregate(prevStart, prevEnd);

        return new TradeOverviewResult()
                .setSuccessAmount(currSuccess.getSuccessAmount())
                .setSuccessCount(currSuccess.getSuccessCount())
                .setRefundAmount(currRefund.getRefundAmount())
                .setRefundCount(currRefund.getRefundCount())
                .setTotalOrders(currTotal.getTotalOrders())
                .setPrevSuccessAmount(prevSuccess.getSuccessAmount())
                .setPrevSuccessCount(prevSuccess.getSuccessCount())
                .setPrevRefundAmount(prevRefund.getRefundAmount())
                .setPrevRefundCount(prevRefund.getRefundCount())
                .setPrevTotalOrders(prevTotal.getTotalOrders());
    }

    // ===== 趋势 =====

    /// 交易趋势(指定天数, 近 N 天每日成交金额与笔数)
    ///
    /// @param days 近 N 天(含今天), 钳制到 [1, 365]
    public List<TradeTrendItemResult> trend(int days) {
        int safeDays = clampDays(days);
        LocalDate today = LocalDate.now(ZONE_CST);
        LocalDate startDate = today.minusDays(safeDays - 1L);
        OffsetDateTime start = startDate.atStartOfDay(ZONE_CST).toOffsetDateTime();
        OffsetDateTime end = today.plusDays(1).atStartOfDay(ZONE_CST).toOffsetDateTime();
        return fillTrendDays(startDate, safeDays, tradeReportMapper.trend(start, end));
    }

    /// 交易趋势(自定义区间)
    public List<TradeTrendItemResult> trend(String startStr, String endStr) {
        OffsetDateTime start = parseDateStart(startStr);
        OffsetDateTime end = parseDateEndExclusive(endStr);
        long days = java.time.Duration.between(start, end).toDays();
        int safeDays = clampDays((int) days);
        LocalDate startDate = start.atZoneSameInstant(ZONE_CST).toLocalDate();
        List<TradeTrendItemResult> rows = tradeReportMapper.trend(start, end);
        return fillTrendDays(startDate, safeDays, rows);
    }

    /// 退款趋势(指定天数)
    public List<RefundTrendItemResult> refundTrend(int days) {
        int safeDays = clampDays(days);
        LocalDate today = LocalDate.now(ZONE_CST);
        LocalDate startDate = today.minusDays(safeDays - 1L);
        OffsetDateTime start = startDate.atStartOfDay(ZONE_CST).toOffsetDateTime();
        OffsetDateTime end = today.plusDays(1).atStartOfDay(ZONE_CST).toOffsetDateTime();
        return fillRefundTrendDays(startDate, safeDays, tradeReportMapper.refundTrend(start, end));
    }

    /// 退款趋势(自定义区间)
    public List<RefundTrendItemResult> refundTrend(String startStr, String endStr) {
        OffsetDateTime start = parseDateStart(startStr);
        OffsetDateTime end = parseDateEndExclusive(endStr);
        long days = java.time.Duration.between(start, end).toDays();
        int safeDays = clampDays((int) days);
        LocalDate startDate = start.atZoneSameInstant(ZONE_CST).toLocalDate();
        return fillRefundTrendDays(startDate, safeDays, tradeReportMapper.refundTrend(start, end));
    }

    // ===== 渠道分布 =====

    /// 支付渠道分布(指定天数, 各支付渠道成交金额与笔数)
    public List<ProviderDistItemResult> providerDist(int days) {
        OffsetDateTime[] range = daysRange(days);
        return tradeReportMapper.providerDist(range[0], range[1]);
    }

    /// 支付渠道分布(自定义区间)
    public List<ProviderDistItemResult> providerDist(String startStr, String endStr) {
        return tradeReportMapper.providerDist(parseDateStart(startStr), parseDateEndExclusive(endStr));
    }

    /// 支付渠道成功率(指定天数)
    public List<ProviderSuccessItemResult> providerSuccess(int days) {
        OffsetDateTime[] range = daysRange(days);
        return tradeReportMapper.providerSuccess(range[0], range[1]);
    }

    /// 支付渠道成功率(自定义区间)
    public List<ProviderSuccessItemResult> providerSuccess(String startStr, String endStr) {
        return tradeReportMapper.providerSuccess(parseDateStart(startStr), parseDateEndExclusive(endStr));
    }

    // ===== 时段分布 =====

    /// 时段分布(日均, 指定天数): 区间内按小时汇总后 ÷ 天数, 补齐 0-23
    public List<HourlyDistItemResult> hourlyDist(int days) {
        OffsetDateTime[] range = daysRange(days);
        int daysSpan = clampDays(days);
        return toDailyAverage(fillHourlyDist(tradeReportMapper.hourlyDist(range[0], range[1])), daysSpan);
    }

    /// 时段分布(日均, 自定义区间): 区间内按小时汇总后 ÷ 天数, 补齐 0-23
    public List<HourlyDistItemResult> hourlyDist(String startStr, String endStr) {
        OffsetDateTime start = parseDateStart(startStr);
        OffsetDateTime end = parseDateEndExclusive(endStr);
        long daysSpan = Math.max(1L, java.time.Duration.between(start, end).toDays());
        return toDailyAverage(fillHourlyDist(tradeReportMapper.hourlyDist(start, end)), daysSpan);
    }

    // ===== 金额区间分桶 =====

    /// 金额区间分桶(指定天数), 补齐 5 个缺失桶
    public List<AmountRangeItemResult> amountRange(int days) {
        OffsetDateTime[] range = daysRange(days);
        return fillAmountRange(tradeReportMapper.amountRange(range[0], range[1]));
    }

    /// 金额区间分桶(自定义区间)
    public List<AmountRangeItemResult> amountRange(String startStr, String endStr) {
        return fillAmountRange(tradeReportMapper.amountRange(parseDateStart(startStr), parseDateEndExclusive(endStr)));
    }

    // ===== 商户排名 =====

    /// 商户交易额排名(指定天数, 含占比计算)
    ///
    /// @param days  近 N 天(含今天)
    /// @param limit 返回前 N 名(默认 10, 上限 50)
    public List<MerchantRankItemResult> merchantRank(int days, int limit) {
        OffsetDateTime[] range = daysRange(days);
        return computeProportion(tradeReportMapper.merchantRank(range[0], range[1], clampLimit(limit)));
    }

    /// 商户交易额排名(自定义区间)
    public List<MerchantRankItemResult> merchantRank(String startStr, String endStr, int limit) {
        return computeProportion(tradeReportMapper.merchantRank(
                parseDateStart(startStr), parseDateEndExclusive(endStr), clampLimit(limit)));
    }

    // ===== 内部工具 =====

    /// 钳制天数到 [1, 365]
    private int clampDays(int days) {
        return Math.max(TREND_DAYS_MIN, Math.min(days, TREND_DAYS_MAX));
    }

    /// 钳制商户排名 limit 到 [1, 50], 默认 10
    private int clampLimit(int limit) {
        if (limit <= 0) {
            return MERCHANT_RANK_LIMIT_DEFAULT;
        }
        return Math.min(limit, MERCHANT_RANK_LIMIT_MAX);
    }

    /// 近 N 天(含今天)的 [start, end) 区间
    private OffsetDateTime[] daysRange(int days) {
        int safeDays = clampDays(days);
        LocalDate today = LocalDate.now(ZONE_CST);
        OffsetDateTime start = today.minusDays(safeDays - 1L).atStartOfDay(ZONE_CST).toOffsetDateTime();
        OffsetDateTime end = today.plusDays(1).atStartOfDay(ZONE_CST).toOffsetDateTime();
        return new OffsetDateTime[]{start, end};
    }

    /// 解析 yyyy-MM-dd 为该日 00:00:00 Asia/Shanghai 的 OffsetDateTime
    private OffsetDateTime parseDateStart(String date) {
        return LocalDate.parse(date, DATE_FMT).atStartOfDay(ZONE_CST).toOffsetDateTime();
    }

    /// 解析 yyyy-MM-dd 为次日 00:00:00 Asia/Shanghai 的 OffsetDateTime (半开区间 end)
    private OffsetDateTime parseDateEndExclusive(String date) {
        return LocalDate.parse(date, DATE_FMT).plusDays(1).atStartOfDay(ZONE_CST).toOffsetDateTime();
    }

    /// 趋势补零: 对完整日期区间补齐缺失日(amount=0, count=0)
    private List<TradeTrendItemResult> fillTrendDays(LocalDate startDate, int days, List<TradeTrendItemResult> rows) {
        Map<String, TradeTrendItemResult> indexed = new HashMap<>(rows.size());
        for (TradeTrendItemResult row : rows) {
            indexed.put(row.getDate(), row);
        }
        List<TradeTrendItemResult> result = new ArrayList<>(days);
        for (int i = 0; i < days; i++) {
            String key = startDate.plusDays(i).format(DATE_FMT);
            TradeTrendItemResult row = indexed.get(key);
            if (row != null) {
                if (row.getCount() == null) {
                    row.setCount(0L);
                }
                result.add(row);
            } else {
                result.add(new TradeTrendItemResult().setDate(key).setAmount(0L).setCount(0L));
            }
        }
        return result;
    }

    /// 退款趋势补零
    private List<RefundTrendItemResult> fillRefundTrendDays(LocalDate startDate, int days, List<RefundTrendItemResult> rows) {
        Map<String, RefundTrendItemResult> indexed = new HashMap<>(rows.size());
        for (RefundTrendItemResult row : rows) {
            indexed.put(row.getDate(), row);
        }
        List<RefundTrendItemResult> result = new ArrayList<>(days);
        for (int i = 0; i < days; i++) {
            String key = startDate.plusDays(i).format(DATE_FMT);
            RefundTrendItemResult row = indexed.get(key);
            if (row != null) {
                if (row.getCount() == null) {
                    row.setCount(0L);
                }
                result.add(row);
            } else {
                result.add(new RefundTrendItemResult().setDate(key).setAmount(0L).setCount(0L));
            }
        }
        return result;
    }

    /// 时段分布补齐 0-23 缺失小时(此时仍为区间合计, 日均见 [toDailyAverage])
    private List<HourlyDistItemResult> fillHourlyDist(List<HourlyDistItemResult> rows) {
        Map<Integer, HourlyDistItemResult> indexed = new HashMap<>(rows.size());
        for (HourlyDistItemResult row : rows) {
            indexed.put(row.getHour(), row);
        }
        List<HourlyDistItemResult> result = new ArrayList<>(24);
        for (int h = 0; h < 24; h++) {
            HourlyDistItemResult row = indexed.get(h);
            if (row != null) {
                if (row.getCount() == null) {
                    row.setCount(0D);
                }
                if (row.getAmount() == null) {
                    row.setAmount(0L);
                }
                result.add(row);
            } else {
                result.add(new HourlyDistItemResult().setHour(h).setAmount(0L).setCount(0D));
            }
        }
        return result;
    }

    /// 将区间内按小时汇总的金额/笔数转为日均(天数至少为 1)
    /// 笔数保留 1 位小数; 金额(分)四舍五入为 Long
    private List<HourlyDistItemResult> toDailyAverage(List<HourlyDistItemResult> rows, long daysSpan) {
        long days = Math.max(1L, daysSpan);
        for (HourlyDistItemResult row : rows) {
            long amount = row.getAmount() == null ? 0L : row.getAmount();
            double count = row.getCount() == null ? 0D : row.getCount();
            row.setAmount(Math.round((double) amount / days));
            row.setCount(Math.round(count * 10.0 / days) / 10.0);
        }
        return rows;
    }

    /// 金额区间补齐 5 个缺失桶(保持固定顺序)
    private List<AmountRangeItemResult> fillAmountRange(List<AmountRangeItemResult> rows) {
        Map<String, Long> indexed = new HashMap<>(rows.size());
        for (AmountRangeItemResult row : rows) {
            indexed.put(row.getBucket(), row.getCount());
        }
        List<AmountRangeItemResult> result = new ArrayList<>(AMOUNT_BUCKETS.size());
        for (String bucket : AMOUNT_BUCKETS) {
            result.add(new AmountRangeItemResult()
                    .setBucket(bucket)
                    .setCount(indexed.getOrDefault(bucket, 0L)));
        }
        return result;
    }

    /// 商户排名占比计算(基于本期返回的 amount 总和, 非全平台总额; 取 top N 内部相对占比)
    /// 注意: 这只是 top N 内部的相对占比, 分母为 top N 总额, 不是全平台总额
    private List<MerchantRankItemResult> computeProportion(List<MerchantRankItemResult> rows) {
        long total = rows.stream().mapToLong(MerchantRankItemResult::getAmount).sum();
        for (MerchantRankItemResult row : rows) {
            double proportion = total > 0
                    ? Math.round(row.getAmount() * 1000.0 / total) / 10.0
                    : 0.0;
            row.setProportion(proportion);
        }
        return rows;
    }
}
