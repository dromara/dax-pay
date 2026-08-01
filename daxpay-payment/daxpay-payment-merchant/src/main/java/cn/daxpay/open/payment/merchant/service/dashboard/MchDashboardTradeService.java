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
import cn.daxpay.open.payment.trade.report.param.TradeRangeQuery;
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

    /// 交易概览(含上期环比)
    ///
    /// 区间模式(query 同时传 start + end)优先; 否则按 date 快捷模式(today/yesterday)。
    public TradeOverviewResult overview(TradeRangeQuery query) {
        String mchNo = requireMchNo();
        OffsetDateTime start;
        OffsetDateTime end;
        OffsetDateTime prevStart;
        OffsetDateTime prevEnd;
        if (query.getStart() != null && query.getEnd() != null) {
            // 区间模式: 上期为等长前移
            start = tradeReportSupport.parseDateStart(query.getStart());
            end = tradeReportSupport.parseDateEndExclusive(query.getEnd());
            long daysSpan = java.time.Duration.between(start, end).toDays();
            prevStart = start.minusDays(daysSpan);
            prevEnd = start;
        } else {
            // 快捷模式: today / yesterday, 上期为前一天
            LocalDate today = LocalDate.now(TradeReportSupport.ZONE_CST);
            LocalDate target = "yesterday".equalsIgnoreCase(query.getDate()) ? today.minusDays(1) : today;
            start = target.atStartOfDay(TradeReportSupport.ZONE_CST).toOffsetDateTime();
            end = target.plusDays(1).atStartOfDay(TradeReportSupport.ZONE_CST).toOffsetDateTime();
            prevStart = start.minusDays(1);
            prevEnd = start;
        }
        return aggregateOverview(mchNo, start, end, prevStart, prevEnd);
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

    public List<TradeTrendItemResult> trend(TradeRangeQuery query) {
        String mchNo = requireMchNo();
        OffsetDateTime[] range = tradeReportSupport.resolveRange(query);
        int safeDays = tradeReportSupport.clampDays((int) java.time.Duration.between(range[0], range[1]).toDays());
        LocalDate startDate = range[0].atZoneSameInstant(TradeReportSupport.ZONE_CST).toLocalDate();
        return tradeReportSupport.fillTrendDays(
                startDate, safeDays, mchTradeReportMapper.trend(range[0], range[1], mchNo));
    }

    public List<RefundTrendItemResult> refundTrend(TradeRangeQuery query) {
        String mchNo = requireMchNo();
        OffsetDateTime[] range = tradeReportSupport.resolveRange(query);
        int safeDays = tradeReportSupport.clampDays((int) java.time.Duration.between(range[0], range[1]).toDays());
        LocalDate startDate = range[0].atZoneSameInstant(TradeReportSupport.ZONE_CST).toLocalDate();
        return tradeReportSupport.fillRefundTrendDays(
                startDate, safeDays, mchTradeReportMapper.refundTrend(range[0], range[1], mchNo));
    }

    // ===== 渠道 =====

    public List<ProviderDistItemResult> providerDist(TradeRangeQuery query) {
        String mchNo = requireMchNo();
        OffsetDateTime[] range = tradeReportSupport.resolveRange(query);
        return mchTradeReportMapper.providerDist(range[0], range[1], mchNo);
    }

    public List<ProviderSuccessItemResult> providerSuccess(TradeRangeQuery query) {
        String mchNo = requireMchNo();
        OffsetDateTime[] range = tradeReportSupport.resolveRange(query);
        return mchTradeReportMapper.providerSuccess(range[0], range[1], mchNo);
    }

    // ===== 时段 / 金额 =====

    public List<HourlyDistItemResult> hourlyDist(TradeRangeQuery query) {
        String mchNo = requireMchNo();
        OffsetDateTime[] range = tradeReportSupport.resolveRange(query);
        long daysSpan = Math.max(1L, java.time.Duration.between(range[0], range[1]).toDays());
        return tradeReportSupport.toDailyAverage(
                tradeReportSupport.fillHourlyDist(mchTradeReportMapper.hourlyDist(range[0], range[1], mchNo)),
                daysSpan);
    }

    public List<AmountRangeItemResult> amountRange(TradeRangeQuery query) {
        String mchNo = requireMchNo();
        OffsetDateTime[] range = tradeReportSupport.resolveRange(query);
        return tradeReportSupport.fillAmountRange(mchTradeReportMapper.amountRange(range[0], range[1], mchNo));
    }

    // ===== 维度排名 =====

    /// dim = channelMch | app | store(为空时按 app 兜底)
    public List<DimRankItemResult> dimRank(TradeRangeQuery query) {
        OffsetDateTime[] range = tradeReportSupport.resolveRange(query);
        int limit = query.getLimit() == null ? 0 : query.getLimit();
        return dimRank(requireMchNo(), query.getDim(), range[0], range[1], limit);
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
