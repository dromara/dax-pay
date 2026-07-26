package cn.daxpay.open.payment.admin.controller.dashboard;

import cn.daxpay.open.platform.core.rest.Res;
import cn.daxpay.open.platform.core.rest.result.Result;
import cn.daxpay.open.payment.admin.result.dashboard.AdminDashboardHeaderCountResult;
import cn.daxpay.open.payment.admin.service.dashboard.AdminDashboardTradeService;
import cn.daxpay.open.payment.trade.report.result.AmountRangeItemResult;
import cn.daxpay.open.payment.trade.report.result.HourlyDistItemResult;
import cn.daxpay.open.payment.trade.report.result.MerchantRankItemResult;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/// # 工作台/分析页交易统计(运营端)
///
/// 面向运营端工作台仪表盘与分析页的聚合统计:
/// 概览(含环比)、交易趋势、退款趋势、支付渠道分布、渠道成功率、时段分布、金额区间、商户排名。
///
/// ## 参数约定
/// - **天数模式**: `days` 参数(近 N 天含今天, 钳制到 [1, 365])
/// - **区间模式**: `start` + `end` 参数(yyyy-MM-dd, 均包含, 半开区间处理)
/// - 两者二选一: 同时传时优先区间模式
///
/// 不挂菜单权限码: 工作台/分析页为登录即达的页面, 任何已认证运营用户均可查看聚合统计。
/// 敏感维度(商户/金额明细)仍由对应业务单管理页与权限码控制。
@Tag(name = "工作台/分析页交易统计")
@Validated
@RestController
@RequestMapping("/admin/dashboard/trade")
@RequiredArgsConstructor
public class AdminDashboardTradeController {

    private final AdminDashboardTradeService adminDashboardTradeService;

    // ===== 头部计数 =====

    /// 工作台头部计数: 商户 / 通道商户 / 运营用户总量
    @Operation(summary = "工作台头部计数(商户/通道商户/用户)")
    @GetMapping("/header-counts")
    public Result<AdminDashboardHeaderCountResult> headerCounts() {
        return Res.ok(adminDashboardTradeService.headerCounts());
    }

    // ===== 概览 =====

    /// 交易概览: 支持今日/昨日快捷模式 + 自定义区间模式(含上期对比用于环比)
    /// 同时传 start/end 时按区间模式; 否则按 date 快捷模式
    @Operation(summary = "交易概览(今日/昨日或自定义区间)")
    @GetMapping("/overview")
    public Result<TradeOverviewResult> overview(
            @RequestParam(required = false) String date,
            @RequestParam(required = false) String start,
            @RequestParam(required = false) String end) {
        if (start != null && end != null) {
            return Res.ok(adminDashboardTradeService.overview(start, end));
        }
        return Res.ok(adminDashboardTradeService.overview(date != null ? date : "today"));
    }

    // ===== 趋势 =====

    @Operation(summary = "交易趋势(指定天数或自定义区间)")
    @GetMapping("/trend")
    public Result<List<TradeTrendItemResult>> trend(
            @RequestParam(defaultValue = "7") int days,
            @RequestParam(required = false) String start,
            @RequestParam(required = false) String end) {
        if (start != null && end != null) {
            return Res.ok(adminDashboardTradeService.trend(start, end));
        }
        return Res.ok(adminDashboardTradeService.trend(days));
    }

    @Operation(summary = "退款趋势(指定天数或自定义区间)")
    @GetMapping("/refund-trend")
    public Result<List<RefundTrendItemResult>> refundTrend(
            @RequestParam(defaultValue = "7") int days,
            @RequestParam(required = false) String start,
            @RequestParam(required = false) String end) {
        if (start != null && end != null) {
            return Res.ok(adminDashboardTradeService.refundTrend(start, end));
        }
        return Res.ok(adminDashboardTradeService.refundTrend(days));
    }

    // ===== 渠道 =====

    @Operation(summary = "支付渠道分布(指定天数或自定义区间)")
    @GetMapping("/provider-dist")
    public Result<List<ProviderDistItemResult>> providerDist(
            @RequestParam(defaultValue = "30") int days,
            @RequestParam(required = false) String start,
            @RequestParam(required = false) String end) {
        if (start != null && end != null) {
            return Res.ok(adminDashboardTradeService.providerDist(start, end));
        }
        return Res.ok(adminDashboardTradeService.providerDist(days));
    }

    @Operation(summary = "支付渠道成功率(指定天数或自定义区间)")
    @GetMapping("/provider-success")
    public Result<List<ProviderSuccessItemResult>> providerSuccess(
            @RequestParam(defaultValue = "30") int days,
            @RequestParam(required = false) String start,
            @RequestParam(required = false) String end) {
        if (start != null && end != null) {
            return Res.ok(adminDashboardTradeService.providerSuccess(start, end));
        }
        return Res.ok(adminDashboardTradeService.providerSuccess(days));
    }

    // ===== 时段 =====

    @Operation(summary = "时段分布(日均): 区间内按小时汇总后除以天数, 补齐 0-23")
    @GetMapping("/hourly-dist")
    public Result<List<HourlyDistItemResult>> hourlyDist(
            @RequestParam(defaultValue = "7") int days,
            @RequestParam(required = false) String start,
            @RequestParam(required = false) String end) {
        if (start != null && end != null) {
            return Res.ok(adminDashboardTradeService.hourlyDist(start, end));
        }
        return Res.ok(adminDashboardTradeService.hourlyDist(days));
    }

    // ===== 金额区间 =====

    @Operation(summary = "金额区间分桶(指定天数或自定义区间)")
    @GetMapping("/amount-range")
    public Result<List<AmountRangeItemResult>> amountRange(
            @RequestParam(defaultValue = "7") int days,
            @RequestParam(required = false) String start,
            @RequestParam(required = false) String end) {
        if (start != null && end != null) {
            return Res.ok(adminDashboardTradeService.amountRange(start, end));
        }
        return Res.ok(adminDashboardTradeService.amountRange(days));
    }

    // ===== 商户排名 =====

    @Operation(summary = "商户交易额排名(指定天数或自定义区间)")
    @GetMapping("/merchant-rank")
    public Result<List<MerchantRankItemResult>> merchantRank(
            @RequestParam(defaultValue = "7") int days,
            @RequestParam(defaultValue = "10") int limit,
            @RequestParam(required = false) String start,
            @RequestParam(required = false) String end) {
        if (start != null && end != null) {
            return Res.ok(adminDashboardTradeService.merchantRank(start, end, limit));
        }
        return Res.ok(adminDashboardTradeService.merchantRank(days, limit));
    }
}
