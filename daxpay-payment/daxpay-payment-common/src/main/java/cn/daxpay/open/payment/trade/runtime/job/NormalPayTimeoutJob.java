package cn.daxpay.open.payment.trade.runtime.job;

import cn.daxpay.open.payment.trade.order.dao.PayTradeManager;
import cn.daxpay.open.payment.trade.order.entity.PayTrade;
import cn.daxpay.open.payment.trade.runtime.service.close.PayCloseService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.List;

/// # 普通支付超时关单兜底定时任务
///
/// 作为 Artemis 延时消息的兜底机制：当 broker 不可用导致下单时消息未发出、
/// 或消息因异常未能正常消费时，由本任务定期扫描已过期但仍处于处理中的普通支付订单并关闭。
///
/// 触发频率：每 5 分钟执行一次(fixedDelay=上次执行结束后间隔 5 分钟)。
/// 单次扫描上限由 [PayTradeManager#findNormalTimeoutTrades] 内部 limit 控制，防止积压爆量。
///
/// 幂等性：复用 [PayCloseService#closeForTimeout]，与 MQ 消费路径共享状态校验与分布式锁，
/// 两条路径并发触发同一订单只会生效一次。
@Slf4j
@Component
@RequiredArgsConstructor
public class NormalPayTimeoutJob {

    private final PayTradeManager payTradeManager;
    private final PayCloseService payCloseService;

    /// 兜底扫描超时未关闭的普通支付订单
    @Scheduled(fixedDelay = 300_000L)
    public void scanTimeoutOrders() {
        OffsetDateTime now = OffsetDateTime.now(ZoneOffset.UTC);
        List<PayTrade> timeoutTrades = payTradeManager.findNormalTimeoutTrades(now);
        if (timeoutTrades.isEmpty()) {
            return;
        }
        log.info("超时关单兜底扫描命中 {} 笔, 开始处理", timeoutTrades.size());
        for (PayTrade trade : timeoutTrades) {
            String tradeNo = trade.getTradeNo();
            try {
                payCloseService.closeForTimeout(tradeNo);
            } catch (Exception e) {
                // 单笔失败不阻断整批，记录后继续
                log.error("超时关单兜底处理失败, tradeNo={}", tradeNo, e);
            }
        }
    }
}
