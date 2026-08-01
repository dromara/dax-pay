package cn.daxpay.open.payment.admin.service.dashboard;

import cn.daxpay.open.payment.admin.dao.dashboard.AdminTradeReportMapper;
import cn.daxpay.open.payment.admin.result.dashboard.AdminDashboardHeaderCountResult;
import cn.daxpay.open.payment.merchant.dao.channel.ChannelMerchantManager;
import cn.daxpay.open.payment.merchant.dao.info.MerchantInfoManager;
import cn.daxpay.open.payment.trade.report.param.TradeRangeQuery;
import cn.daxpay.open.payment.trade.report.result.AmountRangeItemResult;
import cn.daxpay.open.payment.trade.report.result.HourlyDistItemResult;
import cn.daxpay.open.payment.trade.report.result.MerchantRankItemResult;
import cn.daxpay.open.payment.trade.report.result.ProviderDistItemResult;
import cn.daxpay.open.payment.trade.report.result.ProviderSuccessItemResult;
import cn.daxpay.open.payment.trade.report.result.RefundTrendItemResult;
import cn.daxpay.open.payment.trade.report.result.TradeOverviewResult;
import cn.daxpay.open.payment.trade.report.result.TradeTrendItemResult;
import cn.daxpay.open.payment.trade.report.support.TradeReportSupport;
import cn.daxpay.open.platform.core.enums.client.ClientEnum;
import cn.daxpay.open.platform.iam.dao.user.UserInfoManager;
import cn.daxpay.open.platform.iam.entity.user.UserInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.List;

/// # 工作台/分析页交易统计(运营端)
///
/// 编排 [AdminTradeReportMapper]（全平台口径）与 [TradeReportSupport]。
@Service
@RequiredArgsConstructor
public class AdminDashboardTradeService {

    private final AdminTradeReportMapper adminTradeReportMapper;
    private final TradeReportSupport tradeReportSupport;
    private final MerchantInfoManager merchantInfoManager;
    private final ChannelMerchantManager channelMerchantManager;
    private final UserInfoManager userInfoManager;

    // ===== 头部计数 =====

    /// 工作台头部: 商户 / 通道商户 / 运营用户 全量计数
    public AdminDashboardHeaderCountResult headerCounts() {
        long merchantCount = merchantInfoManager.lambdaQuery().count();
        long channelMerchantCount = channelMerchantManager.lambdaQuery().count();
        // 运营端用户: 仅 admin 终端, 排除内置超管(与 UserAdminService 列表口径一致)
        long userCount = userInfoManager.lambdaQuery()
                .eq(UserInfo::getClientCode, ClientEnum.ADMIN.getCode())
                .eq(UserInfo::isAdministrator, false)
                .count();
        return new AdminDashboardHeaderCountResult()
                .setMerchantCount(merchantCount)
                .setChannelMerchantCount(channelMerchantCount)
                .setUserCount(userCount);
    }

    // ===== 概览 =====

    /// 交易概览(含上期环比)
    ///
    /// 区间模式(query 同时传 start + end)优先; 否则按 date 快捷模式(today/yesterday)。
    public TradeOverviewResult overview(TradeRangeQuery query) {
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
        return aggregateOverview(start, end, prevStart, prevEnd);
    }

    private TradeOverviewResult aggregateOverview(
            OffsetDateTime start, OffsetDateTime end,
            OffsetDateTime prevStart, OffsetDateTime prevEnd) {
        TradeOverviewResult currSuccess = adminTradeReportMapper.successAggregate(start, end);
        TradeOverviewResult currTotal = adminTradeReportMapper.totalOrdersAggregate(start, end);
        TradeOverviewResult currRefund = adminTradeReportMapper.refundAggregate(start, end);
        TradeOverviewResult prevSuccess = adminTradeReportMapper.successAggregate(prevStart, prevEnd);
        TradeOverviewResult prevTotal = adminTradeReportMapper.totalOrdersAggregate(prevStart, prevEnd);
        TradeOverviewResult prevRefund = adminTradeReportMapper.refundAggregate(prevStart, prevEnd);

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
        OffsetDateTime[] range = tradeReportSupport.resolveRange(query);
        int safeDays = tradeReportSupport.clampDays((int) java.time.Duration.between(range[0], range[1]).toDays());
        LocalDate startDate = range[0].atZoneSameInstant(TradeReportSupport.ZONE_CST).toLocalDate();
        return tradeReportSupport.fillTrendDays(startDate, safeDays, adminTradeReportMapper.trend(range[0], range[1]));
    }

    public List<RefundTrendItemResult> refundTrend(TradeRangeQuery query) {
        OffsetDateTime[] range = tradeReportSupport.resolveRange(query);
        int safeDays = tradeReportSupport.clampDays((int) java.time.Duration.between(range[0], range[1]).toDays());
        LocalDate startDate = range[0].atZoneSameInstant(TradeReportSupport.ZONE_CST).toLocalDate();
        return tradeReportSupport.fillRefundTrendDays(
                startDate, safeDays, adminTradeReportMapper.refundTrend(range[0], range[1]));
    }

    // ===== 渠道 =====

    public List<ProviderDistItemResult> providerDist(TradeRangeQuery query) {
        OffsetDateTime[] range = tradeReportSupport.resolveRange(query);
        return adminTradeReportMapper.providerDist(range[0], range[1]);
    }

    public List<ProviderSuccessItemResult> providerSuccess(TradeRangeQuery query) {
        OffsetDateTime[] range = tradeReportSupport.resolveRange(query);
        return adminTradeReportMapper.providerSuccess(range[0], range[1]);
    }

    // ===== 时段 / 金额 =====

    public List<HourlyDistItemResult> hourlyDist(TradeRangeQuery query) {
        OffsetDateTime[] range = tradeReportSupport.resolveRange(query);
        long daysSpan = Math.max(1L, java.time.Duration.between(range[0], range[1]).toDays());
        return tradeReportSupport.toDailyAverage(
                tradeReportSupport.fillHourlyDist(adminTradeReportMapper.hourlyDist(range[0], range[1])),
                daysSpan);
    }

    public List<AmountRangeItemResult> amountRange(TradeRangeQuery query) {
        OffsetDateTime[] range = tradeReportSupport.resolveRange(query);
        return tradeReportSupport.fillAmountRange(adminTradeReportMapper.amountRange(range[0], range[1]));
    }

    // ===== 商户排名 =====

    public List<MerchantRankItemResult> merchantRank(TradeRangeQuery query) {
        OffsetDateTime[] range = tradeReportSupport.resolveRange(query);
        int limit = tradeReportSupport.clampLimit(query.getLimit() == null ? 0 : query.getLimit());
        return tradeReportSupport.computeMerchantProportion(
                adminTradeReportMapper.merchantRank(range[0], range[1], limit));
    }
}
