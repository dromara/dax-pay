package cn.daxpay.open.payment.app.merchant.controller.dashboard;

import cn.daxpay.open.payment.app.merchant.service.dashboard.AppMerchantDashboardTradeService;
import cn.daxpay.open.payment.merchant.result.dashboard.MchDashboardHeaderCountResult;
import cn.daxpay.open.payment.trade.report.result.AmountRangeItemResult;
import cn.daxpay.open.payment.trade.report.result.DimRankItemResult;
import cn.daxpay.open.payment.trade.report.result.HourlyDistItemResult;
import cn.daxpay.open.payment.trade.report.result.ProviderDistItemResult;
import cn.daxpay.open.payment.trade.report.result.ProviderSuccessItemResult;
import cn.daxpay.open.payment.trade.report.result.RefundTrendItemResult;
import cn.daxpay.open.payment.trade.report.result.TradeOverviewResult;
import cn.daxpay.open.payment.trade.report.result.TradeTrendItemResult;
import cn.daxpay.open.platform.core.rest.Res;
import cn.daxpay.open.platform.core.rest.result.Result;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/// # 工作台交易统计(商户移动端)
///
/// 面向商户移动端工作台的交易概览统计。
/// 不挂菜单权限码: 工作台为登录即达的页面, 任何已认证商户用户均可查看。
@Tag(name = "工作台交易统计(商户移动端)")
@Validated
@RestController
@RequestMapping("/app-merchant/dashboard/trade")
@RequiredArgsConstructor
public class AppMerchantDashboardTradeController {

    private final AppMerchantDashboardTradeService dashboardTradeService;

    @Operation(summary = "工作台头部计数(应用/门店/通道商户)")
    @GetMapping("/header-counts")
    public Result<MchDashboardHeaderCountResult> headerCounts() {
        return Res.ok(dashboardTradeService.headerCounts());
    }

    /// 交易概览: 支持 today/yesterday 快捷模式 + 自定义区间模式(含环比)
    @Operation(summary = "交易概览(今日/昨日或自定义区间)")
    @GetMapping("/overview")
    public Result<TradeOverviewResult> overview(
            @RequestParam(required = false) String date,
            @RequestParam(required = false) String start,
            @RequestParam(required = false) String end) {
        if (start != null && end != null) {
            return Res.ok(dashboardTradeService.overview(start, end));
        }
        return Res.ok(dashboardTradeService.overview(date != null ? date : "today"));
    }

    @Operation(summary = "交易趋势")
    @GetMapping("/trend")
    public Result<List<TradeTrendItemResult>> trend(
            @RequestParam(defaultValue = "7") int days,
            @RequestParam(required = false) String start,
            @RequestParam(required = false) String end) {
        if (start != null && end != null) {
            return Res.ok(dashboardTradeService.trend(start, end));
        }
        return Res.ok(dashboardTradeService.trend(days));
    }

    @Operation(summary = "退款趋势")
    @GetMapping("/refund-trend")
    public Result<List<RefundTrendItemResult>> refundTrend(
            @RequestParam(defaultValue = "7") int days,
            @RequestParam(required = false) String start,
            @RequestParam(required = false) String end) {
        if (start != null && end != null) {
            return Res.ok(dashboardTradeService.refundTrend(start, end));
        }
        return Res.ok(dashboardTradeService.refundTrend(days));
    }

    @Operation(summary = "支付渠道分布")
    @GetMapping("/provider-dist")
    public Result<List<ProviderDistItemResult>> providerDist(
            @RequestParam(defaultValue = "30") int days,
            @RequestParam(required = false) String start,
            @RequestParam(required = false) String end) {
        if (start != null && end != null) {
            return Res.ok(dashboardTradeService.providerDist(start, end));
        }
        return Res.ok(dashboardTradeService.providerDist(days));
    }

    @Operation(summary = "支付渠道成功率")
    @GetMapping("/provider-success")
    public Result<List<ProviderSuccessItemResult>> providerSuccess(
            @RequestParam(defaultValue = "30") int days,
            @RequestParam(required = false) String start,
            @RequestParam(required = false) String end) {
        if (start != null && end != null) {
            return Res.ok(dashboardTradeService.providerSuccess(start, end));
        }
        return Res.ok(dashboardTradeService.providerSuccess(days));
    }

    @Operation(summary = "时段分布(日均)")
    @GetMapping("/hourly-dist")
    public Result<List<HourlyDistItemResult>> hourlyDist(
            @RequestParam(defaultValue = "7") int days,
            @RequestParam(required = false) String start,
            @RequestParam(required = false) String end) {
        if (start != null && end != null) {
            return Res.ok(dashboardTradeService.hourlyDist(start, end));
        }
        return Res.ok(dashboardTradeService.hourlyDist(days));
    }

    @Operation(summary = "金额区间分桶")
    @GetMapping("/amount-range")
    public Result<List<AmountRangeItemResult>> amountRange(
            @RequestParam(defaultValue = "7") int days,
            @RequestParam(required = false) String start,
            @RequestParam(required = false) String end) {
        if (start != null && end != null) {
            return Res.ok(dashboardTradeService.amountRange(start, end));
        }
        return Res.ok(dashboardTradeService.amountRange(days));
    }

    /// 维度排行: dim=channelMch|app|store
    @Operation(summary = "维度交易额排名(通道商户/应用/门店)")
    @GetMapping("/dim-rank")
    public Result<List<DimRankItemResult>> dimRank(
            @RequestParam(defaultValue = "app") String dim,
            @RequestParam(defaultValue = "7") int days,
            @RequestParam(defaultValue = "10") int limit,
            @RequestParam(required = false) String start,
            @RequestParam(required = false) String end) {
        if (start != null && end != null) {
            return Res.ok(dashboardTradeService.dimRank(dim, start, end, limit));
        }
        return Res.ok(dashboardTradeService.dimRank(dim, days, limit));
    }
}
