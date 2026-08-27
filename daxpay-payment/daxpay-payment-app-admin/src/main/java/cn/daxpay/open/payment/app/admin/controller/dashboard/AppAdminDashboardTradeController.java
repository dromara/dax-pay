package cn.daxpay.open.payment.app.admin.controller.dashboard;

import cn.daxpay.open.payment.app.admin.service.dashboard.AppAdminDashboardTradeService;
import cn.daxpay.open.payment.admin.result.dashboard.AdminDashboardHeaderCountResult;
import cn.daxpay.open.payment.trade.report.param.TradeRangeQuery;
import cn.daxpay.open.payment.trade.report.result.TradeOverviewResult;
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
}
