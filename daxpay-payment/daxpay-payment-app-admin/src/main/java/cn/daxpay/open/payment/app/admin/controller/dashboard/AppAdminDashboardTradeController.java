package cn.daxpay.open.payment.app.admin.controller.dashboard;

import cn.daxpay.open.payment.app.admin.service.dashboard.AppAdminDashboardTradeService;
import cn.daxpay.open.payment.admin.result.dashboard.AdminDashboardHeaderCountResult;
import cn.daxpay.open.payment.trade.report.param.TradeRangeQuery;
import cn.daxpay.open.payment.trade.report.result.AmountRangeItemResult;
import cn.daxpay.open.payment.trade.report.result.HourlyDistItemResult;
import cn.daxpay.open.payment.trade.report.result.MerchantRankItemResult;
import cn.daxpay.open.payment.trade.report.result.ProviderDistItemResult;
import cn.daxpay.open.payment.trade.report.result.ProviderSuccessItemResult;
import cn.daxpay.open.payment.trade.report.result.RefundTrendItemResult;
import cn.daxpay.open.payment.trade.report.result.TradeOverviewResult;
import cn.daxpay.open.payment.trade.report.result.TradeTrendItemResult;
import cn.daxpay.open.platform.core.annotation.IgnoreAuth;
import cn.daxpay.open.platform.core.rest.Res;
import cn.daxpay.open.platform.core.rest.result.Result;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/// # 工作台交易统计(运营移动端)
///
/// 面向运营移动端工作台的交易概览统计。
/// 不挂菜单权限码: 工作台为登录即达的页面, 任何已认证运营用户均可查看。
@Tag(name = "工作台交易统计(运营移动端)")
@IgnoreAuth(login = true)
@Validated
@RestController
@RequestMapping("/app-admin/dashboard/trade")
@RequiredArgsConstructor
public class AppAdminDashboardTradeController {

    private final AppAdminDashboardTradeService dashboardTradeService;

    /// 工作台头部计数: 商户 / 通道商户 / 运营用户总量
    @Operation(summary = "工作台头部计数(商户/通道商户/用户)")
    @GetMapping("/header-counts")
    public Result<AdminDashboardHeaderCountResult> headerCounts() {
        return Res.ok(dashboardTradeService.headerCounts());
    }

    /// 交易概览: 支持 today/yesterday 快捷模式 + 自定义区间模式(含环比)
    /// 同时传 start/end 时按区间模式; 否则按 date 快捷模式
    @Operation(summary = "交易概览(今日/昨日或自定义区间)")
    @GetMapping("/overview")
    public Result<TradeOverviewResult> overview(TradeRangeQuery query) {
        return Res.ok(dashboardTradeService.overview(query));
    }

    /// 交易趋势(按日粒度: 金额/笔数/均价)
    @Operation(summary = "交易趋势(按日)")
    @GetMapping("/trend")
    public Result<List<TradeTrendItemResult>> trend(TradeRangeQuery query) {
        return Res.ok(dashboardTradeService.trend(query));
    }

    /// 退款趋势(按日粒度: 金额/笔数)
    @Operation(summary = "退款趋势(按日)")
    @GetMapping("/refund-trend")
    public Result<List<RefundTrendItemResult>> refundTrend(TradeRangeQuery query) {
        return Res.ok(dashboardTradeService.refundTrend(query));
    }

    /// 服务商(支付方式)交易分布: 按金额与笔数双口径
    @Operation(summary = "支付方式交易分布")
    @GetMapping("/provider-dist")
    public Result<List<ProviderDistItemResult>> providerDist(TradeRangeQuery query) {
        return Res.ok(dashboardTradeService.providerDist(query));
    }

    /// 服务商(通道)成功率
    @Operation(summary = "通道成功率")
    @GetMapping("/provider-success")
    public Result<List<ProviderSuccessItemResult>> providerSuccess(TradeRangeQuery query) {
        return Res.ok(dashboardTradeService.providerSuccess(query));
    }

    /// 24 小时分时分布(笔数)
    @Operation(summary = "24小时分时分布")
    @GetMapping("/hourly-dist")
    public Result<List<HourlyDistItemResult>> hourlyDist(TradeRangeQuery query) {
        return Res.ok(dashboardTradeService.hourlyDist(query));
    }

    /// 金额区间分布(5 档)
    @Operation(summary = "金额区间分布")
    @GetMapping("/amount-range")
    public Result<List<AmountRangeItemResult>> amountRange(TradeRangeQuery query) {
        return Res.ok(dashboardTradeService.amountRange(query));
    }

    /// 商户交易排行(运营端专属维度)
    @Operation(summary = "商户交易排行")
    @GetMapping("/merchant-rank")
    public Result<List<MerchantRankItemResult>> merchantRank(TradeRangeQuery query) {
        return Res.ok(dashboardTradeService.merchantRank(query));
    }
}
