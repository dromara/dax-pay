package cn.daxpay.open.payment.admin.service.dashboard;

import cn.daxpay.open.payment.admin.dao.dashboard.AdminTradeReportMapper;
import cn.daxpay.open.payment.trade.report.result.AmountRangeItemResult;
import cn.daxpay.open.payment.trade.report.result.HourlyDistItemResult;
import cn.daxpay.open.payment.trade.report.result.MerchantRankItemResult;
import cn.daxpay.open.payment.trade.report.result.ProviderDistItemResult;
import cn.daxpay.open.payment.trade.report.result.ProviderSuccessItemResult;
import cn.daxpay.open.payment.trade.report.result.RefundTrendItemResult;
import cn.daxpay.open.payment.trade.report.result.TradeOverviewResult;
import cn.daxpay.open.payment.trade.report.result.TradeTrendItemResult;
import cn.daxpay.open.payment.trade.report.support.TradeReportSupport;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.List;

/// # 工作台/分析页交易统计(运营端)
///
/// 编排 [AdminTradeReportMapper]（全平台口径）与 [TradeReportSupport]。
@Service
@RequiredArgsConstructor
public class AdminDashboardTradeService {

    private final AdminTradeReportMapper adminTradeReportMapper;
    private final TradeReportSupport tradeReportSupport;

    // ===== 概览 =====

    public TradeOverviewResult overview(String date) {
        LocalDate today = LocalDate.now(TradeReportSupport.ZONE_CST);
        LocalDate target = "yesterday".equalsIgnoreCase(date) ? today.minusDays(1) : today;
        OffsetDateTime start = target.atStartOfDay(TradeReportSupport.ZONE_CST).toOffsetDateTime();
        OffsetDateTime end = target.plusDays(1).atStartOfDay(TradeReportSupport.ZONE_CST).toOffsetDateTime();
        OffsetDateTime prevStart = start.minusDays(1);
        OffsetDateTime prevEnd = start;
        return aggregateOverview(start, end, prevStart, prevEnd);
    }

    public TradeOverviewResult overview(String start, String end) {
        OffsetDateTime startUtc = tradeReportSupport.parseDateStart(start);
        OffsetDateTime endUtc = tradeReportSupport.parseDateEndExclusive(end);
        long daysSpan = java.time.Duration.between(startUtc, endUtc).toDays();
        OffsetDateTime prevStart = startUtc.minusDays(daysSpan);
        OffsetDateTime prevEnd = startUtc;
        return aggregateOverview(startUtc, endUtc, prevStart, prevEnd);
    }

    private TradeOverviewResult aggregateOverview(
            OffsetDateTime start, OffsetDateTime end,
            OffsetDateTime prevStart, OffsetDateTime prevEnd) {
        TradeOverviewResult currSuccess = adminTradeReportMapper.successAggregate(start, end);
        TradeOverviewResult currTotal = adminTradeReportMapper.totalOrdersAggregate(start, end);
        TradeOverviewResult currRefund = adminTradeReportMapper.refundAggregate(start, end);
        TradeOverviewResult prevSuccess = adminTradeReportMapper.successAggregate(prevStart, prevEnd);
        TradeOverviewResult prevTotal = adminTradeReportMapper.totalOrdersAggregate(prevStart, prevEnd);
        TradeOverviewResult prevRefund = adminTradeReportMapper.refundAggregate(prevStart, prevEnd);

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

    public List<TradeTrendItemResult> trend(int days) {
        int safeDays = tradeReportSupport.clampDays(days);
        LocalDate today = LocalDate.now(TradeReportSupport.ZONE_CST);
        LocalDate startDate = today.minusDays(safeDays - 1L);
        OffsetDateTime start = startDate.atStartOfDay(TradeReportSupport.ZONE_CST).toOffsetDateTime();
        OffsetDateTime end = today.plusDays(1).atStartOfDay(TradeReportSupport.ZONE_CST).toOffsetDateTime();
        return tradeReportSupport.fillTrendDays(startDate, safeDays, adminTradeReportMapper.trend(start, end));
    }

    public List<TradeTrendItemResult> trend(String startStr, String endStr) {
        OffsetDateTime start = tradeReportSupport.parseDateStart(startStr);
        OffsetDateTime end = tradeReportSupport.parseDateEndExclusive(endStr);
        long days = java.time.Duration.between(start, end).toDays();
        int safeDays = tradeReportSupport.clampDays((int) days);
        LocalDate startDate = start.atZoneSameInstant(TradeReportSupport.ZONE_CST).toLocalDate();
        return tradeReportSupport.fillTrendDays(startDate, safeDays, adminTradeReportMapper.trend(start, end));
    }

    public List<RefundTrendItemResult> refundTrend(int days) {
        int safeDays = tradeReportSupport.clampDays(days);
        LocalDate today = LocalDate.now(TradeReportSupport.ZONE_CST);
        LocalDate startDate = today.minusDays(safeDays - 1L);
        OffsetDateTime start = startDate.atStartOfDay(TradeReportSupport.ZONE_CST).toOffsetDateTime();
        OffsetDateTime end = today.plusDays(1).atStartOfDay(TradeReportSupport.ZONE_CST).toOffsetDateTime();
        return tradeReportSupport.fillRefundTrendDays(
                startDate, safeDays, adminTradeReportMapper.refundTrend(start, end));
    }

    public List<RefundTrendItemResult> refundTrend(String startStr, String endStr) {
        OffsetDateTime start = tradeReportSupport.parseDateStart(startStr);
        OffsetDateTime end = tradeReportSupport.parseDateEndExclusive(endStr);
        long days = java.time.Duration.between(start, end).toDays();
        int safeDays = tradeReportSupport.clampDays((int) days);
        LocalDate startDate = start.atZoneSameInstant(TradeReportSupport.ZONE_CST).toLocalDate();
        return tradeReportSupport.fillRefundTrendDays(
                startDate, safeDays, adminTradeReportMapper.refundTrend(start, end));
    }

    // ===== 渠道 =====

    public List<ProviderDistItemResult> providerDist(int days) {
        OffsetDateTime[] range = tradeReportSupport.daysRange(days);
        return adminTradeReportMapper.providerDist(range[0], range[1]);
    }

    public List<ProviderDistItemResult> providerDist(String startStr, String endStr) {
        return adminTradeReportMapper.providerDist(
                tradeReportSupport.parseDateStart(startStr),
                tradeReportSupport.parseDateEndExclusive(endStr));
    }

    public List<ProviderSuccessItemResult> providerSuccess(int days) {
        OffsetDateTime[] range = tradeReportSupport.daysRange(days);
        return adminTradeReportMapper.providerSuccess(range[0], range[1]);
    }

    public List<ProviderSuccessItemResult> providerSuccess(String startStr, String endStr) {
        return adminTradeReportMapper.providerSuccess(
                tradeReportSupport.parseDateStart(startStr),
                tradeReportSupport.parseDateEndExclusive(endStr));
    }

    // ===== 时段 / 金额 =====

    public List<HourlyDistItemResult> hourlyDist(int days) {
        OffsetDateTime[] range = tradeReportSupport.daysRange(days);
        int daysSpan = tradeReportSupport.clampDays(days);
        return tradeReportSupport.toDailyAverage(
                tradeReportSupport.fillHourlyDist(adminTradeReportMapper.hourlyDist(range[0], range[1])),
                daysSpan);
    }

    public List<HourlyDistItemResult> hourlyDist(String startStr, String endStr) {
        OffsetDateTime start = tradeReportSupport.parseDateStart(startStr);
        OffsetDateTime end = tradeReportSupport.parseDateEndExclusive(endStr);
        long daysSpan = Math.max(1L, java.time.Duration.between(start, end).toDays());
        return tradeReportSupport.toDailyAverage(
                tradeReportSupport.fillHourlyDist(adminTradeReportMapper.hourlyDist(start, end)),
                daysSpan);
    }

    public List<AmountRangeItemResult> amountRange(int days) {
        OffsetDateTime[] range = tradeReportSupport.daysRange(days);
        return tradeReportSupport.fillAmountRange(adminTradeReportMapper.amountRange(range[0], range[1]));
    }

    public List<AmountRangeItemResult> amountRange(String startStr, String endStr) {
        return tradeReportSupport.fillAmountRange(adminTradeReportMapper.amountRange(
                tradeReportSupport.parseDateStart(startStr),
                tradeReportSupport.parseDateEndExclusive(endStr)));
    }

    // ===== 商户排名 =====

    public List<MerchantRankItemResult> merchantRank(int days, int limit) {
        OffsetDateTime[] range = tradeReportSupport.daysRange(days);
        return tradeReportSupport.computeMerchantProportion(
                adminTradeReportMapper.merchantRank(range[0], range[1], tradeReportSupport.clampLimit(limit)));
    }

    public List<MerchantRankItemResult> merchantRank(String startStr, String endStr, int limit) {
        return tradeReportSupport.computeMerchantProportion(adminTradeReportMapper.merchantRank(
                tradeReportSupport.parseDateStart(startStr),
                tradeReportSupport.parseDateEndExclusive(endStr),
                tradeReportSupport.clampLimit(limit)));
    }
}
