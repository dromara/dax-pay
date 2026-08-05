package cn.daxpay.open.payment.trade.runtime.job;

import cn.daxpay.open.payment.trade.enums.PayFundStatusEnum;
import cn.daxpay.open.payment.trade.order.dao.PayTradeManager;
import cn.daxpay.open.payment.trade.order.dao.RefundOrderManager;
import cn.daxpay.open.payment.trade.order.entity.PayTrade;
import cn.daxpay.open.payment.trade.order.entity.RefundOrder;
import cn.daxpay.open.payment.trade.runtime.service.sync.TradeSyncService;
import cn.daxpay.open.payment.trade.transfer.dao.TransferTradeManager;
import cn.daxpay.open.payment.trade.transfer.entity.TransferTrade;
import cn.daxpay.open.payment.trade.transfer.runtime.service.TransferSyncService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.javacrumbs.shedlock.spring.annotation.SchedulerLock;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.List;

/// # 交易订单定时同步任务
///
/// 分层窗口扫描支付/退款中间态订单, 调通道查单纠正本地状态。
///
/// ## 设计要点
/// - **支付 PROCESSING 段**(4 窗口): 回调丢失/超时关单失败后, 查通道真实状态纠正为 SUCCESS/CLOSE/FAIL
/// - **支付 CLOSE 纠正段**(1 窗口): 超时关单后通道实际已付款, 触发 CLOSE→SUCCESS 纠正
/// - **退款 PROGRESS 段**(4 窗口): 退款回调丢失后, 查通道真实退款状态
///
/// 订单越"新"查得越勤, 越久越稀疏, 超 7 天自然淘汰(不显式置 FAIL)。
/// 与 [NormalPayTimeoutJob] / [GatewayTimeoutJob] 共享 `payment:trade:{id}` Redis 锁, 天然互斥。
/// 单笔同步异常不阻断整批, 锁冲突(RepetitiveOperationException)跳过等下一轮。
///
/// 全局开关: `daxpay.platform.config.trade-sync-enabled`(默认 true)。
@Slf4j
@Component
@RequiredArgsConstructor
@ConditionalOnProperty(prefix = "daxpay.platform.config", name = "trade-sync-enabled", havingValue = "true", matchIfMissing = true)
public class TradeSyncJob {

    private final PayTradeManager payTradeManager;
    private final RefundOrderManager refundOrderManager;
    private final TransferTradeManager transferTradeManager;
    private final TransferSyncService transferSyncService;
    private final TradeSyncService tradeSyncService;

    // ==================== 支付 PROCESSING 同步(分层窗口) ====================

    /// 最新窗口: 创建 1~10 分钟的 PROCESSING 订单, 每分钟同步一次
    @Scheduled(cron = "0 */1 * * * ?")
    @SchedulerLock(name = "lock:tradeSync:payProc10M", lockAtMostFor = "50s", lockAtLeastFor = "5s")
    public void syncPayProcessing10M() {
        OffsetDateTime now = OffsetDateTime.now(ZoneOffset.UTC);
        syncPayBatch(PayFundStatusEnum.PROCESSING.getCode(), now.minusMinutes(10), now.minusMinutes(1));
    }

    /// 次新窗口: 创建 10 分钟~1 小时的 PROCESSING 订单, 每 10 分钟同步一次
    @Scheduled(cron = "0 */10 * * * ?")
    @SchedulerLock(name = "lock:tradeSync:payProc1H", lockAtMostFor = "8m", lockAtLeastFor = "30s")
    public void syncPayProcessing1H() {
        OffsetDateTime now = OffsetDateTime.now(ZoneOffset.UTC);
        syncPayBatch(PayFundStatusEnum.PROCESSING.getCode(), now.minusHours(1), now.minusMinutes(10));
    }

    /// 陈旧窗口: 创建 1~24 小时的 PROCESSING 订单, 每小时同步一次
    @Scheduled(cron = "0 0 */1 * * ?")
    @SchedulerLock(name = "lock:tradeSync:payProc1D", lockAtMostFor = "50m", lockAtLeastFor = "1m")
    public void syncPayProcessing1D() {
        OffsetDateTime now = OffsetDateTime.now(ZoneOffset.UTC);
        syncPayBatch(PayFundStatusEnum.PROCESSING.getCode(), now.minusHours(24), now.minusHours(1));
    }

    /// 死单兜底: 创建 1~7 天的 PROCESSING 订单, 每天凌晨同步一次
    @Scheduled(cron = "0 10 1 * * ?")
    @SchedulerLock(name = "lock:tradeSync:payProc7D", lockAtMostFor = "30m", lockAtLeastFor = "5m")
    public void syncPayProcessing7D() {
        OffsetDateTime now = OffsetDateTime.now(ZoneOffset.UTC);
        syncPayBatch(PayFundStatusEnum.PROCESSING.getCode(), now.minusDays(7), now.minusHours(24));
    }

    // ==================== 支付 CLOSE 纠正(CLOSE→SUCCESS) ====================

    /// FAIL 纠正窗口: 创建 1~60 分钟的 FAIL 订单, 每 10 分钟同步一次
    ///
    /// 通道瞬时失败后实际已付款的纠正。FAIL 单既不被回调成功纠正(回调 CAS 守卫仅 PROCESSING/INIT),
    /// 也不在 PROCESSING 同步窗口, 需独立窗口覆盖; 否则资金已收但订单永久 FAIL。
    /// cron 偏移 20s 避免与 syncPayProcessing10M(0s)同瞬触发。
    @Scheduled(cron = "20 */10 * * * ?")
    @SchedulerLock(name = "lock:tradeSync:payFailFix", lockAtMostFor = "8m", lockAtLeastFor = "30s")
    public void syncPayFailFix() {
        OffsetDateTime now = OffsetDateTime.now(ZoneOffset.UTC);
        syncPayBatch(PayFundStatusEnum.FAIL.getCode(), now.minusMinutes(60), now.minusMinutes(1));
    }

    /// 超时关单后通道实际已付款的纠正窗口: close_time 在 1~60 分钟内的 CLOSE 订单, 每 5 分钟同步一次
    ///
    /// 按 close_time(非 create_time)扫描: 默认 30min 到期的订单超时关单时 create_time 已超 30min,
    /// 若按 create_time 扫描会永久漏扫(原 bug); 改按 close_time + 放宽到 60min 覆盖默认到期单。
    @Scheduled(cron = "0 */5 * * * ?")
    @SchedulerLock(name = "lock:tradeSync:payCloseFix", lockAtMostFor = "4m", lockAtLeastFor = "30s")
    public void syncPayCloseFix() {
        OffsetDateTime now = OffsetDateTime.now(ZoneOffset.UTC);
        List<PayTrade> trades = payTradeManager.findSyncTradesByCloseTime(
                PayFundStatusEnum.CLOSE.getCode(), now.minusMinutes(60), now.minusMinutes(1));
        if (trades.isEmpty()) {
            return;
        }
        log.info("定时同步扫描支付 CLOSE 纠正(close_time) 命中 {} 笔, 开始处理", trades.size());
        for (PayTrade trade : trades) {
            try {
                tradeSyncService.syncPayTrade(trade.getTradeNo());
            } catch (Exception e) {
                log.warn("支付同步跳过 tradeNo={}", trade.getTradeNo(), e);
            }
        }
    }

    // ==================== 退款 PROGRESS 同步(分层窗口) ====================

    /// 最新窗口: 创建 1~10 分钟的 PROGRESS 退款, 每分钟同步一次
    @Scheduled(cron = "0 */1 * * * ?")
    @SchedulerLock(name = "lock:tradeSync:refund10M", lockAtMostFor = "50s", lockAtLeastFor = "5s")
    public void syncRefund10M() {
        OffsetDateTime now = OffsetDateTime.now(ZoneOffset.UTC);
        syncRefundBatch(now.minusMinutes(10), now.minusMinutes(1));
    }

    /// 次新窗口: 创建 10 分钟~1 小时的 PROGRESS 退款, 每 10 分钟同步一次
    @Scheduled(cron = "0 */10 * * * ?")
    @SchedulerLock(name = "lock:tradeSync:refund1H", lockAtMostFor = "8m", lockAtLeastFor = "30s")
    public void syncRefund1H() {
        OffsetDateTime now = OffsetDateTime.now(ZoneOffset.UTC);
        syncRefundBatch(now.minusHours(1), now.minusMinutes(10));
    }

    /// 陈旧窗口: 创建 1~24 小时的 PROGRESS 退款, 每小时同步一次
    @Scheduled(cron = "0 0 */1 * * ?")
    @SchedulerLock(name = "lock:tradeSync:refund1D", lockAtMostFor = "50m", lockAtLeastFor = "1m")
    public void syncRefund1D() {
        OffsetDateTime now = OffsetDateTime.now(ZoneOffset.UTC);
        syncRefundBatch(now.minusHours(24), now.minusHours(1));
    }

    /// 死单兜底: 创建 1~7 天的 PROGRESS 退款, 每天凌晨同步一次
    @Scheduled(cron = "0 20 1 * * ?")
    @SchedulerLock(name = "lock:tradeSync:refund7D", lockAtMostFor = "30m", lockAtLeastFor = "5m")
    public void syncRefund7D() {
        OffsetDateTime now = OffsetDateTime.now(ZoneOffset.UTC);
        syncRefundBatch(now.minusDays(7), now.minusHours(24));
    }

    // ==================== 转账 PROCESSING 同步(分层窗口) ====================
    ///
    /// 转账回调丢失/延迟消息失败后, 查通道真实状态纠正为 SUCCESS/CLOSE。
    /// 与支付/退款同步共享转账凭证级 Redis 锁(payment:transfer-trade:{id}), 天然互斥。

    /// 最新窗口: 创建 1~10 分钟的 PROCESSING 转账, 每分钟同步一次
    @Scheduled(cron = "0 */1 * * * ?")
    @SchedulerLock(name = "lock:tradeSync:transfer10M", lockAtMostFor = "50s", lockAtLeastFor = "5s")
    public void syncTransfer10M() {
        OffsetDateTime now = OffsetDateTime.now(ZoneOffset.UTC);
        syncTransferBatch(now.minusMinutes(10), now.minusMinutes(1));
    }

    /// 次新窗口: 创建 10 分钟~1 小时的 PROCESSING 转账, 每 10 分钟同步一次
    @Scheduled(cron = "0 */10 * * * ?")
    @SchedulerLock(name = "lock:tradeSync:transfer1H", lockAtMostFor = "8m", lockAtLeastFor = "30s")
    public void syncTransfer1H() {
        OffsetDateTime now = OffsetDateTime.now(ZoneOffset.UTC);
        syncTransferBatch(now.minusHours(1), now.minusMinutes(10));
    }

    /// 陈旧窗口: 创建 1~24 小时的 PROCESSING 转账, 每小时同步一次
    @Scheduled(cron = "0 0 */1 * * ?")
    @SchedulerLock(name = "lock:tradeSync:transfer1D", lockAtMostFor = "50m", lockAtLeastFor = "1m")
    public void syncTransfer1D() {
        OffsetDateTime now = OffsetDateTime.now(ZoneOffset.UTC);
        syncTransferBatch(now.minusHours(24), now.minusHours(1));
    }

    /// 死单兜底: 创建 1~7 天的 PROCESSING 转账, 每天凌晨同步一次
    @Scheduled(cron = "0 30 1 * * ?")
    @SchedulerLock(name = "lock:tradeSync:transfer7D", lockAtMostFor = "30m", lockAtLeastFor = "5m")
    public void syncTransfer7D() {
        OffsetDateTime now = OffsetDateTime.now(ZoneOffset.UTC);
        syncTransferBatch(now.minusDays(7), now.minusHours(24));
    }

    // ==================== 批量处理 ====================

    /// 支付同步批量处理(逐笔容错, 单笔锁冲突/异常不阻断整批)
    private void syncPayBatch(String status, OffsetDateTime start, OffsetDateTime end) {
        List<PayTrade> trades = payTradeManager.findSyncTrades(status, start, end);
        if (trades.isEmpty()) {
            return;
        }
        log.info("定时同步扫描支付 status={} 命中 {} 笔, 开始处理", status, trades.size());
        for (PayTrade trade : trades) {
            try {
                tradeSyncService.syncPayTrade(trade.getTradeNo());
            } catch (Exception e) {
                // 单笔失败不阻断整批(锁冲突/通道异常等), 记录后继续
                log.warn("支付同步跳过 tradeNo={}", trade.getTradeNo(), e);
            }
        }
    }

    /// 退款同步批量处理(逐笔容错)
    private void syncRefundBatch(OffsetDateTime start, OffsetDateTime end) {
        List<RefundOrder> refunds = refundOrderManager.findProgressRefunds(start, end);
        if (refunds.isEmpty()) {
            return;
        }
        log.info("定时同步扫描退款命中 {} 笔, 开始处理", refunds.size());
        for (RefundOrder refund : refunds) {
            try {
                tradeSyncService.syncRefundOrder(refund.getRefundNo());
            } catch (Exception e) {
                log.warn("退款同步跳过 refundNo={}", refund.getRefundNo(), e);
            }
        }
    }

    /// 转账同步批量处理(逐笔容错)
    private void syncTransferBatch(OffsetDateTime start, OffsetDateTime end) {
        List<TransferTrade> trades = transferTradeManager.findSyncTransfers(
                PayFundStatusEnum.PROCESSING.getCode(), start, end);
        if (trades.isEmpty()) {
            return;
        }
        log.info("定时同步扫描转账命中 {} 笔, 开始处理", trades.size());
        for (TransferTrade trade : trades) {
            try {
                transferSyncService.autoSync(trade.getTradeNo());
            } catch (Exception e) {
                log.warn("转账同步跳过 tradeNo={}", trade.getTradeNo(), e);
            }
        }
    }
}
