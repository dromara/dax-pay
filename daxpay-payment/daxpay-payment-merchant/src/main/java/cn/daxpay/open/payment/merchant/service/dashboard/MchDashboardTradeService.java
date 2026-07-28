package cn.daxpay.open.payment.merchant.service.dashboard;

import cn.daxpay.open.platform.core.code.CommonCode;
import cn.daxpay.open.platform.core.exception.BizInfoException;
import cn.daxpay.open.payment.common.context.PaymentContext;
import cn.daxpay.open.payment.merchant.dao.appinfo.MchAppInfoManager;
import cn.daxpay.open.payment.merchant.dao.channel.ChannelMerchantManager;
import cn.daxpay.open.payment.merchant.dao.dashboard.MchTradeReportMapper;
import cn.daxpay.open.payment.merchant.dao.store.MchStoreInfoManager;
import cn.daxpay.open.payment.merchant.entity.appinfo.MchAppInfo;
import cn.daxpay.open.payment.merchant.entity.channel.ChannelMerchant;
import cn.daxpay.open.payment.merchant.entity.store.MchStoreInfo;
import cn.daxpay.open.payment.merchant.result.dashboard.MchDashboardHeaderCountResult;
import cn.daxpay.open.payment.trade.report.result.AmountRangeItemResult;
import cn.daxpay.open.payment.trade.report.result.DimRankItemResult;
import cn.daxpay.open.payment.trade.report.result.HourlyDistItemResult;
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

/// # 商户端工作台/分析页交易统计
///
/// 编排 [MchTradeReportMapper]（强制 mch_no）与 [TradeReportSupport]。
/// 强制使用 [PaymentContext#getMchNo], 禁止请求参数指定任意商户号。
@Service
@RequiredArgsConstructor
public class MchDashboardTradeService {

    /// 维度: 通道商户
    public static final String DIM_CHANNEL_MCH = "channelMch";
    /// 维度: 应用
    public static final String DIM_APP = "app";
    /// 维度: 门店
    public static final String DIM_STORE = "store";

    private final PaymentContext paymentContext;
    private final MchTradeReportMapper mchTradeReportMapper;
    private final TradeReportSupport tradeReportSupport;
    private final MchAppInfoManager mchAppInfoManager;
    private final MchStoreInfoManager mchStoreInfoManager;
    private final ChannelMerchantManager channelMerchantManager;

    /// 解析并校验当前商户号
    private String requireMchNo() {
        String mchNo = paymentContext.getMchNo();
        if (mchNo == null || mchNo.isBlank()) {
            // 商户: 数据错误未发现商户号
            throw new BizInfoException(CommonCode.FAIL_CODE, "error.payment.merchant.dataErrorNoMchNo");
        }
        return mchNo;
    }

    /// 工作台头部: 应用 / 门店 / 通道商户 计数
    public MchDashboardHeaderCountResult headerCounts() {
        String mchNo = requireMchNo();
        long appCount = mchAppInfoManager.lambdaQuery()
                .eq(MchAppInfo::getMchNo, mchNo)
                .count();
        long storeCount = mchStoreInfoManager.lambdaQuery()
                .eq(MchStoreInfo::getMchNo, mchNo)
                .count();
        long channelMerchantCount = channelMerchantManager.lambdaQuery()
                .eq(ChannelMerchant::getMchNo, mchNo)
                .count();
        return new MchDashboardHeaderCountResult()
                .setAppCount(appCount)
                .setStoreCount(storeCount)
                .setChannelMerchantCount(channelMerchantCount);
    }

    // ===== 概览 =====

    public TradeOverviewResult overview(String date) {
        String mchNo = requireMchNo();
        LocalDate today = LocalDate.now(TradeReportSupport.ZONE_CST);
        LocalDate target = "yesterday".equalsIgnoreCase(date) ? today.minusDays(1) : today;
        OffsetDateTime start = target.atStartOfDay(TradeReportSupport.ZONE_CST).toOffsetDateTime();
        OffsetDateTime end = target.plusDays(1).atStartOfDay(TradeReportSupport.ZONE_CST).toOffsetDateTime();
        OffsetDateTime prevStart = start.minusDays(1);
        OffsetDateTime prevEnd = start;
        return aggregateOverview(mchNo, start, end, prevStart, prevEnd);
    }

    public TradeOverviewResult overview(String start, String end) {
        String mchNo = requireMchNo();
        OffsetDateTime startUtc = tradeReportSupport.parseDateStart(start);
        OffsetDateTime endUtc = tradeReportSupport.parseDateEndExclusive(end);
        long daysSpan = java.time.Duration.between(startUtc, endUtc).toDays();
        OffsetDateTime prevStart = startUtc.minusDays(daysSpan);
        OffsetDateTime prevEnd = startUtc;
        return aggregateOverview(mchNo, startUtc, endUtc, prevStart, prevEnd);
    }

    private TradeOverviewResult aggregateOverview(
            String mchNo,
            OffsetDateTime start, OffsetDateTime end,
            OffsetDateTime prevStart, OffsetDateTime prevEnd) {
        TradeOverviewResult currSuccess = mchTradeReportMapper.successAggregate(start, end, mchNo);
        TradeOverviewResult currTotal = mchTradeReportMapper.totalOrdersAggregate(start, end, mchNo);
        TradeOverviewResult currRefund = mchTradeReportMapper.refundAggregate(start, end, mchNo);
        TradeOverviewResult prevSuccess = mchTradeReportMapper.successAggregate(prevStart, prevEnd, mchNo);
        TradeOverviewResult prevTotal = mchTradeReportMapper.totalOrdersAggregate(prevStart, prevEnd, mchNo);
        TradeOverviewResult prevRefund = mchTradeReportMapper.refundAggregate(prevStart, prevEnd, mchNo);

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
        String mchNo = requireMchNo();
        int safeDays = tradeReportSupport.clampDays(days);
        LocalDate today = LocalDate.now(TradeReportSupport.ZONE_CST);
        LocalDate startDate = today.minusDays(safeDays - 1L);
        OffsetDateTime start = startDate.atStartOfDay(TradeReportSupport.ZONE_CST).toOffsetDateTime();
        OffsetDateTime end = today.plusDays(1).atStartOfDay(TradeReportSupport.ZONE_CST).toOffsetDateTime();
        return tradeReportSupport.fillTrendDays(
                startDate, safeDays, mchTradeReportMapper.trend(start, end, mchNo));
    }

    public List<TradeTrendItemResult> trend(String startStr, String endStr) {
        String mchNo = requireMchNo();
        OffsetDateTime start = tradeReportSupport.parseDateStart(startStr);
        OffsetDateTime end = tradeReportSupport.parseDateEndExclusive(endStr);
        long days = java.time.Duration.between(start, end).toDays();
        int safeDays = tradeReportSupport.clampDays((int) days);
        LocalDate startDate = start.atZoneSameInstant(TradeReportSupport.ZONE_CST).toLocalDate();
        return tradeReportSupport.fillTrendDays(
                startDate, safeDays, mchTradeReportMapper.trend(start, end, mchNo));
    }

    public List<RefundTrendItemResult> refundTrend(int days) {
        String mchNo = requireMchNo();
        int safeDays = tradeReportSupport.clampDays(days);
        LocalDate today = LocalDate.now(TradeReportSupport.ZONE_CST);
        LocalDate startDate = today.minusDays(safeDays - 1L);
        OffsetDateTime start = startDate.atStartOfDay(TradeReportSupport.ZONE_CST).toOffsetDateTime();
        OffsetDateTime end = today.plusDays(1).atStartOfDay(TradeReportSupport.ZONE_CST).toOffsetDateTime();
        return tradeReportSupport.fillRefundTrendDays(
                startDate, safeDays, mchTradeReportMapper.refundTrend(start, end, mchNo));
    }

    public List<RefundTrendItemResult> refundTrend(String startStr, String endStr) {
        String mchNo = requireMchNo();
        OffsetDateTime start = tradeReportSupport.parseDateStart(startStr);
        OffsetDateTime end = tradeReportSupport.parseDateEndExclusive(endStr);
        long days = java.time.Duration.between(start, end).toDays();
        int safeDays = tradeReportSupport.clampDays((int) days);
        LocalDate startDate = start.atZoneSameInstant(TradeReportSupport.ZONE_CST).toLocalDate();
        return tradeReportSupport.fillRefundTrendDays(
                startDate, safeDays, mchTradeReportMapper.refundTrend(start, end, mchNo));
    }

    // ===== 渠道 =====

    public List<ProviderDistItemResult> providerDist(int days) {
        String mchNo = requireMchNo();
        OffsetDateTime[] range = tradeReportSupport.daysRange(days);
        return mchTradeReportMapper.providerDist(range[0], range[1], mchNo);
    }

    public List<ProviderDistItemResult> providerDist(String startStr, String endStr) {
        String mchNo = requireMchNo();
        return mchTradeReportMapper.providerDist(
                tradeReportSupport.parseDateStart(startStr),
                tradeReportSupport.parseDateEndExclusive(endStr),
                mchNo);
    }

    public List<ProviderSuccessItemResult> providerSuccess(int days) {
        String mchNo = requireMchNo();
        OffsetDateTime[] range = tradeReportSupport.daysRange(days);
        return mchTradeReportMapper.providerSuccess(range[0], range[1], mchNo);
    }

    public List<ProviderSuccessItemResult> providerSuccess(String startStr, String endStr) {
        String mchNo = requireMchNo();
        return mchTradeReportMapper.providerSuccess(
                tradeReportSupport.parseDateStart(startStr),
                tradeReportSupport.parseDateEndExclusive(endStr),
                mchNo);
    }

    // ===== 时段 / 金额 =====

    public List<HourlyDistItemResult> hourlyDist(int days) {
        String mchNo = requireMchNo();
        OffsetDateTime[] range = tradeReportSupport.daysRange(days);
        int daysSpan = tradeReportSupport.clampDays(days);
        return tradeReportSupport.toDailyAverage(
                tradeReportSupport.fillHourlyDist(mchTradeReportMapper.hourlyDist(range[0], range[1], mchNo)),
                daysSpan);
    }

    public List<HourlyDistItemResult> hourlyDist(String startStr, String endStr) {
        String mchNo = requireMchNo();
        OffsetDateTime start = tradeReportSupport.parseDateStart(startStr);
        OffsetDateTime end = tradeReportSupport.parseDateEndExclusive(endStr);
        long daysSpan = Math.max(1L, java.time.Duration.between(start, end).toDays());
        return tradeReportSupport.toDailyAverage(
                tradeReportSupport.fillHourlyDist(mchTradeReportMapper.hourlyDist(start, end, mchNo)),
                daysSpan);
    }

    public List<AmountRangeItemResult> amountRange(int days) {
        String mchNo = requireMchNo();
        OffsetDateTime[] range = tradeReportSupport.daysRange(days);
        return tradeReportSupport.fillAmountRange(mchTradeReportMapper.amountRange(range[0], range[1], mchNo));
    }

    public List<AmountRangeItemResult> amountRange(String startStr, String endStr) {
        String mchNo = requireMchNo();
        return tradeReportSupport.fillAmountRange(mchTradeReportMapper.amountRange(
                tradeReportSupport.parseDateStart(startStr),
                tradeReportSupport.parseDateEndExclusive(endStr),
                mchNo));
    }

    // ===== 维度排名 =====

    /// @param dim channelMch | app | store
    public List<DimRankItemResult> dimRank(String dim, int days, int limit) {
        OffsetDateTime[] range = tradeReportSupport.daysRange(days);
        return dimRank(requireMchNo(), dim, range[0], range[1], limit);
    }

    public List<DimRankItemResult> dimRank(String dim, String startStr, String endStr, int limit) {
        return dimRank(
                requireMchNo(),
                dim,
                tradeReportSupport.parseDateStart(startStr),
                tradeReportSupport.parseDateEndExclusive(endStr),
                limit);
    }

    private List<DimRankItemResult> dimRank(
            String mchNo, String dim, OffsetDateTime start, OffsetDateTime end, int limit) {
        int safeLimit = tradeReportSupport.clampLimit(limit);
        String normalized = dim == null ? DIM_APP : dim.trim();
        List<DimRankItemResult> rows = switch (normalized) {
            case DIM_CHANNEL_MCH -> mchTradeReportMapper.dimRankByChannelMch(start, end, mchNo, safeLimit);
            case DIM_STORE -> mchTradeReportMapper.dimRankByStore(start, end, mchNo, safeLimit);
            default -> mchTradeReportMapper.dimRankByApp(start, end, mchNo, safeLimit);
        };
        return tradeReportSupport.computeDimProportion(rows);
    }
}
