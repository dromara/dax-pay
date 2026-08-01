package cn.daxpay.open.payment.app.merchant.service.dashboard;

import cn.daxpay.open.payment.merchant.result.dashboard.MchDashboardHeaderCountResult;
import cn.daxpay.open.payment.merchant.service.dashboard.MchDashboardTradeService;
import cn.daxpay.open.payment.trade.report.param.TradeRangeQuery;
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

    /// 交易概览(区间模式优先, 否则按 date 快捷模式)
    public TradeOverviewResult overview(TradeRangeQuery query) {
        return mchDashboardTradeService.overview(query);
    }

    /// 交易趋势
    public List<TradeTrendItemResult> trend(TradeRangeQuery query) {
        return mchDashboardTradeService.trend(query);
    }

    /// 退款趋势
    public List<RefundTrendItemResult> refundTrend(TradeRangeQuery query) {
        return mchDashboardTradeService.refundTrend(query);
    }

    /// 支付渠道分布
    public List<ProviderDistItemResult> providerDist(TradeRangeQuery query) {
        return mchDashboardTradeService.providerDist(query);
    }

    /// 支付渠道成功率
    public List<ProviderSuccessItemResult> providerSuccess(TradeRangeQuery query) {
        return mchDashboardTradeService.providerSuccess(query);
    }

    /// 时段分布(日均)
    public List<HourlyDistItemResult> hourlyDist(TradeRangeQuery query) {
        return mchDashboardTradeService.hourlyDist(query);
    }

    /// 金额区间分桶
    public List<AmountRangeItemResult> amountRange(TradeRangeQuery query) {
        return mchDashboardTradeService.amountRange(query);
    }

    /// 维度排行: dim=channelMch|app|store
    public List<DimRankItemResult> dimRank(TradeRangeQuery query) {
        return mchDashboardTradeService.dimRank(query);
    }
}
