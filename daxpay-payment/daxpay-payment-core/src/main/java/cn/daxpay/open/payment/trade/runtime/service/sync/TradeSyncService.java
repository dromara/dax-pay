package cn.daxpay.open.payment.trade.runtime.service.sync;

import cn.daxpay.open.payment.common.context.PaymentContext;
import cn.daxpay.open.payment.trade.enums.PayFundStatusEnum;
import cn.daxpay.open.payment.trade.enums.RefundOrderStatusEnum;
import cn.daxpay.open.payment.trade.order.dao.PayTradeManager;
import cn.daxpay.open.payment.trade.order.dao.RefundOrderManager;
import cn.daxpay.open.payment.trade.order.entity.PayTrade;
import cn.daxpay.open.payment.trade.order.entity.RefundOrder;
import cn.daxpay.open.payment.trade.runtime.service.refund.RefundSyncService;
import cn.hutool.core.util.StrUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.Set;

/// # 交易定时同步服务
///
/// 定时任务无商户登录上下文, 本服务负责:
/// ① 引导读(NotTenant)定位订单获取 mchNo;
/// ② 通过 [PaymentContext#runAs] + `setMchNo` 装载租户身份(仅 setMchNo, 不校验商户启用);
/// ③ 委托 [PaySyncService#syncPayOrder] / [RefundSyncService#syncById] 执行租户内同步。
///
/// 与 [PayCloseService#closeForTimeout] 的上下文装载范式完全对称,
/// 共享 `payment:trade:{id}` Redis 锁, 同步与关单天然互斥。
@Slf4j
@Service
@RequiredArgsConstructor
public class TradeSyncService {

    /// 需要同步的支付资金态: 处理中(回调丢失纠正) + 已失败(FAIL→SUCCESS 纠正) + 已关闭(CLOSE→SUCCESS 纠正)
    /// - PROCESSING: 回调丢失/超时关单失败后, 查通道真实状态纠正
    /// - FAIL: 通道瞬时失败后实际已付款的纠正(配合 ChannelResultUnknownException 后新 FAIL 单大幅减少)
    /// - CLOSE: 超时关单后通道实际已付款的场景, 触发 CLOSE→SUCCESS 纠正
    private static final Set<String> SYNC_PAY_STATUSES = Set.of(
            PayFundStatusEnum.PROCESSING.getCode(),
            PayFundStatusEnum.FAIL.getCode(),
            PayFundStatusEnum.CLOSE.getCode());

    private final PayTradeManager payTradeManager;
    private final RefundOrderManager refundOrderManager;
    private final PaySyncService paySyncService;
    private final RefundSyncService refundSyncService;
    private final PaymentContext paymentContext;

    /// 定时同步支付订单(幂等)
    ///
    /// 仅处理 PROCESSING / FAIL / CLOSE 状态:
    /// - PROCESSING: 回调丢失/超时关单失败后, 查通道真实状态纠正
    /// - FAIL/CLOSE: 通道实际已付款的收款证据不再自动翻转(2026-08-29 决策), 落异常订单人工处置
    public void syncPayTrade(String tradeNo) {
        // 引导读: 跨租户定位订单
        PayTrade boot = payTradeManager.findByTradeNoNotTenant(tradeNo).orElse(null);
        if (Objects.isNull(boot)) {
            return;
        }
        if (!SYNC_PAY_STATUSES.contains(boot.getStatus())) {
            return;
        }
        if (StrUtil.isBlank(boot.getMchNo())) {
            log.error("定时同步交易缺少 mchNo, tradeNo={}", tradeNo);
            return;
        }
        // 装载租户身份后执行同步(syncPayOrderFromJob 自带 Redis 锁 + REQUIRES_NEW 事务)
        paymentContext.runAs(() -> {
            paymentContext.setMchNo(boot.getMchNo());
            paySyncService.syncPayOrderFromJob(boot);
        });
    }

    /// 定时同步退款订单(幂等)
    ///
    /// 仅处理 PROGRESS 状态(退款中), 查通道真实退款状态后回写结算。
    public void syncRefundOrder(String refundNo) {
        // 引导读: 跨租户定位退款单
        RefundOrder boot = refundOrderManager.findByRefundNoNotTenant(refundNo).orElse(null);
        if (Objects.isNull(boot)) {
            return;
        }
        if (!Objects.equals(RefundOrderStatusEnum.PROGRESS.getCode(), boot.getStatus())) {
            return;
        }
        if (StrUtil.isBlank(boot.getMchNo())) {
            log.error("定时同步退款缺少 mchNo, refundNo={}", refundNo);
            return;
        }
        // 装载租户身份后执行同步
        paymentContext.runAs(() -> {
            paymentContext.setMchNo(boot.getMchNo());
            refundSyncService.syncById(boot.getId());
        });
    }
}
