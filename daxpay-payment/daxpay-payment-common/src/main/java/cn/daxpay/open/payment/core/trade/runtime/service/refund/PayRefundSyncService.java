package cn.daxpay.open.payment.core.trade.runtime.service.refund;

import cn.daxpay.open.platform.core.code.DaxPayErrorCode;
import cn.daxpay.open.platform.core.exception.BizInfoException;
import cn.daxpay.open.payment.common.enums.RefundOrderStatusEnum;
import cn.daxpay.open.payment.core.strategy.PaymentStrategyFactory;
import cn.daxpay.open.payment.core.strategy.refund.AbsSyncRefundStrategy;
import cn.daxpay.open.payment.core.trade.runtime.bo.RefundResultBo;
import cn.daxpay.open.payment.core.trade.order.dao.PayRefundOrderManager;
import cn.daxpay.open.payment.core.trade.order.dao.PayTradeManager;
import cn.daxpay.open.payment.core.trade.order.entity.PayRefundOrder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Objects;

/// # 退款同步服务
///
/// 查询通道网关方的退款最终状态, 回写退款单与可退余额。
/// 适用于退款发起时 fund_change=N(未即时成功)的场景。
@Slf4j
@Service
@RequiredArgsConstructor
public class PayRefundSyncService {

    private final PayRefundOrderManager payRefundOrderManager;
    private final PayTradeManager payTradeManager;

    /// 退款同步(传入退款单ID)
    public PayRefundOrder syncById(Long refundOrderId) {
        PayRefundOrder refundOrder = payRefundOrderManager.findById(refundOrderId)
                .orElseThrow(() -> new BizInfoException(DaxPayErrorCode.TRADE_STATUS_ERROR, "pay.error.refund.orderNotFound"));
        return this.sync(refundOrder);
    }

    /// 退款同步
    public PayRefundOrder sync(PayRefundOrder refundOrder) {
        // 终态不重复同步
        if (Objects.equals(refundOrder.getStatus(), RefundOrderStatusEnum.SUCCESS.getCode())
                || Objects.equals(refundOrder.getStatus(), RefundOrderStatusEnum.FAIL.getCode())) {
            return refundOrder;
        }

        // 调用通道退款同步策略
        AbsSyncRefundStrategy strategy = PaymentStrategyFactory.createByProduct(
                refundOrder.getProduct(), AbsSyncRefundStrategy.class);
        RefundResultBo result = strategy.doSync(refundOrder);

        // 同步失败(通道未返回明确结果)
        if (!result.isSyncSuccess() || result.getStatus() == null) {
            log.warn("退款同步未获取明确结果, refundNo={}, error={}", refundOrder.getRefundNo(), result.getSyncErrorMsg());
            return refundOrder;
        }

        // 回写退款单状态
        String oldStatus = refundOrder.getStatus();
        refundOrder.setStatus(result.getStatus().getCode());
        if (result.getFinishTime() != null) {
            refundOrder.setFinishTime(result.getFinishTime());
        }
        payRefundOrderManager.updateById(refundOrder);

        // 若从未成功变为成功, 扣减可退余额
        if (Objects.equals(result.getStatus(), RefundOrderStatusEnum.SUCCESS)
                && !Objects.equals(oldStatus, RefundOrderStatusEnum.SUCCESS.getCode())) {
            payTradeManager.findByTradeNo(refundOrder.getOrderNo()).ifPresent(trade -> {
                long newBalance = (trade.getRefundableBalance() == null ? 0 : trade.getRefundableBalance())
                        - refundOrder.getAmount();
                trade.setRefundableBalance(Math.max(newBalance, 0));
                payTradeManager.updateById(trade);
            });
        }
        return refundOrder;
    }
}
