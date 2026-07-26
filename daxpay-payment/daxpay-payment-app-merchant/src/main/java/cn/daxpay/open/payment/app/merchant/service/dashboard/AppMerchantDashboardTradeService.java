package cn.daxpay.open.payment.app.merchant.service.dashboard;

import cn.daxpay.open.payment.merchant.result.dashboard.MchDashboardHeaderCountResult;
import cn.daxpay.open.payment.merchant.service.dashboard.MchDashboardTradeService;
import cn.daxpay.open.payment.trade.report.result.AmountRangeItemResult;
import cn.daxpay.open.payment.trade.report.result.DimRankItemResult;
import cn.daxpay.open.payment.trade.report.result.HourlyDistItemResult;
import cn.daxpay.open.payment.trade.report.result.ProviderDistItemResult;
import cn.daxpay.open.payment.trade.report.result.ProviderSuccessItemResult;
import cn.daxpay.open.payment.trade.report.result.RefundTrendItemResult;
import cn.daxpay.open.payment.trade.report.result.TradeOverviewResult;
import cn.daxpay.open.payment.trade.report.result.TradeTrendItemResult;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/// # 商户移动端-工作台交易统计服务
///
/// 转发至 [MchDashboardTradeService]
@Service
@RequiredArgsConstructor
public class AppMerchantDashboardTradeService {

    private final MchDashboardTradeService mchDashboardTradeService;

    /// 工作台头部计数
    public MchDashboardHeaderCountResult headerCounts() {
        return mchDashboardTradeService.headerCounts();
    }

    /// 交易概览（快捷模式：today / yesterday）
    public TradeOverviewResult overview(String date) {
        return mchDashboardTradeService.overview(date);
    }

    /// 交易概览（区间模式）
    public TradeOverviewResult overview(String start, String end) {
        return mchDashboardTradeService.overview(start, end);
    }

    /// 交易趋势（快捷天数）
    public List<TradeTrendItemResult> trend(int days) {
        return mchDashboardTradeService.trend(days);
    }

    /// 交易趋势（区间模式）
    public List<TradeTrendItemResult> trend(String start, String end) {
        return mchDashboardTradeService.trend(start, end);
    }

    /// 退款趋势（快捷天数）
    public List<RefundTrendItemResult> refundTrend(int days) {
        return mchDashboardTradeService.refundTrend(days);
    }

    /// 退款趋势（区间模式）
    public List<RefundTrendItemResult> refundTrend(String start, String end) {
        return mchDashboardTradeService.refundTrend(start, end);
    }

    /// 支付渠道分布（快捷天数）
    public List<ProviderDistItemResult> providerDist(int days) {
        return mchDashboardTradeService.providerDist(days);
    }

    /// 支付渠道分布（区间模式）
    public List<ProviderDistItemResult> providerDist(String start, String end) {
        return mchDashboardTradeService.providerDist(start, end);
    }

    /// 支付渠道成功率（快捷天数）
    public List<ProviderSuccessItemResult> providerSuccess(int days) {
        return mchDashboardTradeService.providerSuccess(days);
    }

    /// 支付渠道成功率（区间模式）
    public List<ProviderSuccessItemResult> providerSuccess(String start, String end) {
        return mchDashboardTradeService.providerSuccess(start, end);
    }

    /// 时段分布（快捷天数）
    public List<HourlyDistItemResult> hourlyDist(int days) {
        return mchDashboardTradeService.hourlyDist(days);
    }

    /// 时段分布（区间模式）
    public List<HourlyDistItemResult> hourlyDist(String start, String end) {
        return mchDashboardTradeService.hourlyDist(start, end);
    }

    /// 金额区间分桶（快捷天数）
    public List<AmountRangeItemResult> amountRange(int days) {
        return mchDashboardTradeService.amountRange(days);
    }

    /// 金额区间分桶（区间模式）
    public List<AmountRangeItemResult> amountRange(String start, String end) {
        return mchDashboardTradeService.amountRange(start, end);
    }

    /// 维度排行（快捷天数）
    public List<DimRankItemResult> dimRank(String dim, int days, int limit) {
        return mchDashboardTradeService.dimRank(dim, days, limit);
    }

    /// 维度排行（区间模式）
    public List<DimRankItemResult> dimRank(String dim, String start, String end, int limit) {
        return mchDashboardTradeService.dimRank(dim, start, end, limit);
    }
}
