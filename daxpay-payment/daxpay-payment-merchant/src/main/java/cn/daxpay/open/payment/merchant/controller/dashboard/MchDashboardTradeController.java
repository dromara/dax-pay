package cn.daxpay.open.payment.merchant.controller.dashboard;

import cn.daxpay.open.platform.core.rest.Res;
import cn.daxpay.open.platform.core.rest.result.Result;
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
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/// # 工作台/分析页交易统计(商户端)
///
/// 参数约定与运营端 `/admin/dashboard/trade` 对齐; 数据强制按当前登录商户隔离。
@Tag(name = "工作台/分析页交易统计(商户端)")
@Validated
@RestController
@RequestMapping("/mch/dashboard/trade")
@RequiredArgsConstructor
public class MchDashboardTradeController {

    private final MchDashboardTradeService mchDashboardTradeService;

    @Operation(summary = "工作台头部计数(应用/门店/通道商户)")
    @GetMapping("/header-counts")
    public Result<MchDashboardHeaderCountResult> headerCounts() {
        return Res.ok(mchDashboardTradeService.headerCounts());
    }

    @Operation(summary = "交易概览(今日/昨日或自定义区间)")
    @GetMapping("/overview")
    public Result<TradeOverviewResult> overview(TradeRangeQuery query) {
        return Res.ok(mchDashboardTradeService.overview(query));
    }

    @Operation(summary = "交易趋势")
    @GetMapping("/trend")
    public Result<List<TradeTrendItemResult>> trend(TradeRangeQuery query) {
        return Res.ok(mchDashboardTradeService.trend(query));
    }

    @Operation(summary = "退款趋势")
    @GetMapping("/refund-trend")
    public Result<List<RefundTrendItemResult>> refundTrend(TradeRangeQuery query) {
        return Res.ok(mchDashboardTradeService.refundTrend(query));
    }

    @Operation(summary = "支付渠道分布")
    @GetMapping("/provider-dist")
    public Result<List<ProviderDistItemResult>> providerDist(TradeRangeQuery query) {
        return Res.ok(mchDashboardTradeService.providerDist(query));
    }

    @Operation(summary = "支付渠道成功率")
    @GetMapping("/provider-success")
    public Result<List<ProviderSuccessItemResult>> providerSuccess(TradeRangeQuery query) {
        return Res.ok(mchDashboardTradeService.providerSuccess(query));
    }

    @Operation(summary = "时段分布(日均)")
    @GetMapping("/hourly-dist")
    public Result<List<HourlyDistItemResult>> hourlyDist(TradeRangeQuery query) {
        return Res.ok(mchDashboardTradeService.hourlyDist(query));
    }

    @Operation(summary = "金额区间分桶")
    @GetMapping("/amount-range")
    public Result<List<AmountRangeItemResult>> amountRange(TradeRangeQuery query) {
        return Res.ok(mchDashboardTradeService.amountRange(query));
    }

    /// 维度排行: dim=channelMch|app|store
    @Operation(summary = "维度交易额排名(通道商户/应用/门店)")
    @GetMapping("/dim-rank")
    public Result<List<DimRankItemResult>> dimRank(TradeRangeQuery query) {
        return Res.ok(mchDashboardTradeService.dimRank(query));
    }
}
