package cn.daxpay.open.payment.trade.report.support;

import cn.daxpay.open.payment.trade.report.param.TradeRangeQuery;
import cn.daxpay.open.payment.trade.report.result.AmountRangeItemResult;
import cn.daxpay.open.payment.trade.report.result.DimRankItemResult;
import cn.daxpay.open.payment.trade.report.result.HourlyDistItemResult;
import cn.daxpay.open.payment.trade.report.result.MerchantRankItemResult;
import cn.daxpay.open.payment.trade.report.result.RefundTrendItemResult;
import cn.daxpay.open.payment.trade.report.result.TradeTrendItemResult;
import cn.daxpay.open.platform.core.util.DateTimeUtil;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/// # 交易报表共享工具
///
/// 日期区间(CST 日界线)、趋势/时段/分桶补零、排名占比计算。无 Mapper 依赖, 供运营/商户端 Service 共用。
@Component
public class TradeReportSupport {

    /// 业务时区(东八区), 统一取平台常量避免多处副本
    public static final ZoneId ZONE_CST = DateTimeUtil.ZONE_CST;
    public static final DateTimeFormatter DATE_FMT = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    private static final int TREND_DAYS_MIN = 1;
    private static final int TREND_DAYS_MAX = 365;

    /// 天数模式默认值(未传 days 时兜底)
    private static final int DEFAULT_RANGE_DAYS = 7;

    private static final int RANK_LIMIT_MAX = 50;
    private static final int RANK_LIMIT_DEFAULT = 10;

    private static final List<String> AMOUNT_BUCKETS = List.of("0-50", "50-200", "200-1000", "1000-5000", "5000+");

    /// 钳制天数到 [1, 365]
    public int clampDays(int days) {
        return Math.max(TREND_DAYS_MIN, Math.min(days, TREND_DAYS_MAX));
    }

    /// 钳制排名条数; `<=0` 时用默认 10, 上限 50
    public int clampLimit(int limit) {
        if (limit <= 0) {
            return RANK_LIMIT_DEFAULT;
        }
        return Math.min(limit, RANK_LIMIT_MAX);
    }

    /// 近 N 天(含今天)半开区间 `[start, end)`
    public OffsetDateTime[] daysRange(int days) {
        int safeDays = clampDays(days);
        LocalDate today = LocalDate.now(ZONE_CST);
        OffsetDateTime start = today.minusDays(safeDays - 1L).atStartOfDay(ZONE_CST).toOffsetDateTime();
        OffsetDateTime end = today.plusDays(1).atStartOfDay(ZONE_CST).toOffsetDateTime();
        return new OffsetDateTime[]{start, end};
    }

    /// 统一解析查询条件为半开区间 `[start, end)`
    ///
    /// 区间模式优先(start + end 都非空), 否则按 days 天数模式(days 为空时按 [DEFAULT_RANGE_DAYS] 兜底)。
    /// 供运营端/商户端/移动端 Dashboard 各统计接口共用, 消除 Controller 层的 if 分支。
    public OffsetDateTime[] resolveRange(TradeRangeQuery query) {
        if (Objects.nonNull(query.getStart()) && Objects.nonNull(query.getEnd())) {
            return new OffsetDateTime[]{
                    parseDateStart(query.getStart()),
                    parseDateEndExclusive(query.getEnd())
            };
        }
        int days = Objects.nonNull(query.getDays()) ? query.getDays() : DEFAULT_RANGE_DAYS;
        return daysRange(days);
    }

    /// 解析 yyyy-MM-dd 为当日 00:00 CST
    public OffsetDateTime parseDateStart(String date) {
        return LocalDate.parse(date, DATE_FMT).atStartOfDay(ZONE_CST).toOffsetDateTime();
    }

    /// 解析 yyyy-MM-dd 为次日 00:00 CST(半开区间上界)
    public OffsetDateTime parseDateEndExclusive(String date) {
        return LocalDate.parse(date, DATE_FMT).plusDays(1).atStartOfDay(ZONE_CST).toOffsetDateTime();
    }

    /// 交易趋势按日补零
    public List<TradeTrendItemResult> fillTrendDays(LocalDate startDate, int days, List<TradeTrendItemResult> rows) {
        Map<String, TradeTrendItemResult> indexed = new HashMap<>(rows.size());
        for (TradeTrendItemResult row : rows) {
            indexed.put(row.getDate(), row);
        }
        List<TradeTrendItemResult> result = new ArrayList<>(days);
        for (int i = 0; i < days; i++) {
            String key = startDate.plusDays(i).format(DATE_FMT);
            TradeTrendItemResult row = indexed.get(key);
            if (Objects.nonNull(row)) {
                if (Objects.isNull(row.getCount())) {
                    row.setCount(0L);
                }
                result.add(row);
            } else {
                result.add(new TradeTrendItemResult().setDate(key).setAmount(0L).setCount(0L));
            }
        }
        return result;
    }

    /// 退款趋势按日补零
    public List<RefundTrendItemResult> fillRefundTrendDays(LocalDate startDate, int days, List<RefundTrendItemResult> rows) {
        Map<String, RefundTrendItemResult> indexed = new HashMap<>(rows.size());
        for (RefundTrendItemResult row : rows) {
            indexed.put(row.getDate(), row);
        }
        List<RefundTrendItemResult> result = new ArrayList<>(days);
        for (int i = 0; i < days; i++) {
            String key = startDate.plusDays(i).format(DATE_FMT);
            RefundTrendItemResult row = indexed.get(key);
            if (Objects.nonNull(row)) {
                if (Objects.isNull(row.getCount())) {
                    row.setCount(0L);
                }
                result.add(row);
            } else {
                result.add(new RefundTrendItemResult().setDate(key).setAmount(0L).setCount(0L));
            }
        }
        return result;
    }

    /// 时段分布补齐 0-23
    public List<HourlyDistItemResult> fillHourlyDist(List<HourlyDistItemResult> rows) {
        Map<Integer, HourlyDistItemResult> indexed = new HashMap<>(rows.size());
        for (HourlyDistItemResult row : rows) {
            indexed.put(row.getHour(), row);
        }
        List<HourlyDistItemResult> result = new ArrayList<>(24);
        for (int h = 0; h < 24; h++) {
            HourlyDistItemResult row = indexed.get(h);
            if (Objects.nonNull(row)) {
                if (Objects.isNull(row.getCount())) {
                    row.setCount(0D);
                }
                if (Objects.isNull(row.getAmount())) {
                    row.setAmount(0L);
                }
                result.add(row);
            } else {
                result.add(new HourlyDistItemResult().setHour(h).setAmount(0L).setCount(0D));
            }
        }
        return result;
    }

    /// 时段汇总转为日均(金额四舍五入到分, 笔数保留一位小数)
    public List<HourlyDistItemResult> toDailyAverage(List<HourlyDistItemResult> rows, long daysSpan) {
        long days = Math.max(1L, daysSpan);
        for (HourlyDistItemResult row : rows) {
            long amount = Objects.isNull(row.getAmount()) ? 0L : row.getAmount();
            double count = Objects.isNull(row.getCount()) ? 0D : row.getCount();
            row.setAmount(Math.round((double) amount / days));
            row.setCount(Math.round(count * 10.0 / days) / 10.0);
        }
        return rows;
    }

    /// 金额区间按固定桶序补零
    public List<AmountRangeItemResult> fillAmountRange(List<AmountRangeItemResult> rows) {
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

    /// 运营端商户排名占比(一位小数百分比)
    public List<MerchantRankItemResult> computeMerchantProportion(List<MerchantRankItemResult> rows) {
        long total = rows.stream().mapToLong(MerchantRankItemResult::getAmount).sum();
        for (MerchantRankItemResult row : rows) {
            double proportion = total > 0
                    ? Math.round(row.getAmount() * 1000.0 / total) / 10.0
                    : 0.0;
            row.setProportion(proportion);
        }
        return rows;
    }

    /// 商户端维度排名占比
    public List<DimRankItemResult> computeDimProportion(List<DimRankItemResult> rows) {
        long total = rows.stream()
                .mapToLong(r -> Objects.isNull(r.getAmount()) ? 0L : r.getAmount())
                .sum();
        for (DimRankItemResult row : rows) {
            long amount = Objects.isNull(row.getAmount()) ? 0L : row.getAmount();
            double proportion = total > 0
                    ? Math.round(amount * 1000.0 / total) / 10.0
                    : 0.0;
            row.setProportion(proportion);
        }
        return rows;
    }
}
