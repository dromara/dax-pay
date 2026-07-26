package cn.daxpay.open.payment.app.admin.service.dashboard;

import cn.daxpay.open.payment.admin.result.dashboard.AdminDashboardHeaderCountResult;
import cn.daxpay.open.payment.admin.service.dashboard.AdminDashboardTradeService;
import cn.daxpay.open.payment.trade.report.result.TradeOverviewResult;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

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

    /// 交易概览（快捷模式：today / yesterday）
    public TradeOverviewResult overview(String date) {
        return adminDashboardTradeService.overview(date);
    }

    /// 交易概览（区间模式：start + end，yyyy-MM-dd）
    public TradeOverviewResult overview(String start, String end) {
        return adminDashboardTradeService.overview(start, end);
    }
}
