package cn.daxpay.open.payment.app.admin.service.dashboard;

import cn.daxpay.open.payment.admin.result.dashboard.AdminDashboardHeaderCountResult;
import cn.daxpay.open.payment.admin.service.dashboard.AdminDashboardTradeService;
import cn.daxpay.open.payment.trade.report.param.TradeRangeQuery;
import cn.daxpay.open.payment.trade.report.result.AmountRangeItemResult;
import cn.daxpay.open.payment.trade.report.result.HourlyDistItemResult;
import cn.daxpay.open.payment.trade.report.result.MerchantRankItemResult;
import cn.daxpay.open.payment.trade.report.result.ProviderDistItemResult;
import cn.daxpay.open.payment.trade.report.result.ProviderSuccessItemResult;
import cn.daxpay.open.payment.trade.report.result.RefundTrendItemResult;
import cn.daxpay.open.payment.trade.report.result.TradeOverviewResult;
import cn.daxpay.open.payment.trade.report.result.TradeTrendItemResult;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/// # 运营移动端-工作台交易统计服务
///
/// 转发至 [AdminDashboardTradeService]
@Service
@RequiredArgsConstructor
public class AppAdminDashboardTradeService {

    private final AdminDashboardTradeService adminDashboardTradeService;

    /// 工作台头部计数: 商户 / 通道商户 / 运营用户
    public AdminDashboardHeaderCountResult headerCounts() {
        return adminDashboardTradeService.headerCounts();
    }

    /// 交易概览(区间模式优先, 否则按 date 快捷模式)
    public TradeOverviewResult overview(TradeRangeQuery query) {
        return adminDashboardTradeService.overview(query);
    }

    /// 交易趋势(按日粒度: 金额/笔数/均价)
    public List<TradeTrendItemResult> trend(TradeRangeQuery query) {
        return adminDashboardTradeService.trend(query);
    }

    /// 退款趋势(按日粒度: 金额/笔数)
    public List<RefundTrendItemResult> refundTrend(TradeRangeQuery query) {
        return adminDashboardTradeService.refundTrend(query);
    }

    /// 服务商(支付方式)交易分布: 按金额与笔数双口径
    public List<ProviderDistItemResult> providerDist(TradeRangeQuery query) {
        return adminDashboardTradeService.providerDist(query);
    }

    /// 服务商(通道)成功率
    public List<ProviderSuccessItemResult> providerSuccess(TradeRangeQuery query) {
        return adminDashboardTradeService.providerSuccess(query);
    }

    /// 24 小时分时分布(笔数)
    public List<HourlyDistItemResult> hourlyDist(TradeRangeQuery query) {
        return adminDashboardTradeService.hourlyDist(query);
    }

    /// 金额区间分布(5 档)
    public List<AmountRangeItemResult> amountRange(TradeRangeQuery query) {
        return adminDashboardTradeService.amountRange(query);
    }

    /// 商户交易排行(运营端专属维度)
    public List<MerchantRankItemResult> merchantRank(TradeRangeQuery query) {
        return adminDashboardTradeService.merchantRank(query);
    }
}
