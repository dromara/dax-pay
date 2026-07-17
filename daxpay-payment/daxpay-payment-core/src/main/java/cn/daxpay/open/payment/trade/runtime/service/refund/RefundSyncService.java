package cn.daxpay.open.payment.trade.runtime.service.refund;

import cn.daxpay.open.platform.core.code.DaxPayErrorCode;
import cn.daxpay.open.platform.core.exception.BizInfoException;
import cn.daxpay.open.payment.trade.enums.RefundOrderStatusEnum;
import cn.daxpay.open.payment.strategy.PaymentStrategyFactory;
import cn.daxpay.open.payment.strategy.refund.AbsSyncRefundStrategy;
import cn.daxpay.open.payment.trade.runtime.bo.RefundResultBo;
import cn.daxpay.open.payment.trade.order.dao.RefundOrderManager;
import cn.daxpay.open.payment.trade.order.entity.RefundOrder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Objects;

/// # 退款同步服务
///
/// 查询通道网关方的退款最终状态, 回写退款单。
/// 成功/失败结算委托 [RefundSettleService](预占模型: SUCCESS 不二次扣, FAIL/CLOSE 回滚)。
@Slf4j
@Service
@RequiredArgsConstructor
public class RefundSyncService {

    private final RefundOrderManager refundOrderManager;
    private final RefundSettleService refundSettleService;

    /// 退款同步(传入退款单ID)
    public RefundOrder syncById(Long refundOrderId) {
        RefundOrder refundOrder = refundOrderManager.findById(refundOrderId)
                .orElseThrow(() -> new BizInfoException(DaxPayErrorCode.TRADE_STATUS_ERROR, "pay.error.refund.orderNotFound"));
        return this.sync(refundOrder);
    }

    /// 退款同步
    public RefundOrder sync(RefundOrder refundOrder) {
        // 终态不重复同步
        if (Objects.equals(refundOrder.getStatus(), RefundOrderStatusEnum.SUCCESS.getCode())
                || Objects.equals(refundOrder.getStatus(), RefundOrderStatusEnum.FAIL.getCode())
                || Objects.equals(refundOrder.getStatus(), RefundOrderStatusEnum.CLOSE.getCode())) {
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

        if (Objects.equals(result.getStatus(), RefundOrderStatusEnum.SUCCESS)) {
            refundSettleService.settleSuccess(
                    refundOrder.getId(), result.getFinishTime(),
                    result.getOutRefundNo(), result.getRelationOrderNo());
        } else if (Objects.equals(result.getStatus(), RefundOrderStatusEnum.FAIL)) {
            refundSettleService.settleFail(
                    refundOrder.getId(), result.getFinishTime(),
                    result.getOutRefundNo(), result.getRelationOrderNo(), result.getSyncErrorMsg());
        } else if (Objects.equals(result.getStatus(), RefundOrderStatusEnum.CLOSE)) {
            refundSettleService.settleClose(
                    refundOrder.getId(), result.getFinishTime(),
                    result.getOutRefundNo(), result.getRelationOrderNo(), result.getSyncErrorMsg());
        } else {
            // PROGRESS: 补写字段, 不改余额
            refundSettleService.applyProgressResult(
                    refundOrder, result.getFinishTime(),
                    result.getOutRefundNo(), result.getRelationOrderNo(), result.getSyncErrorMsg());
        }
        return refundOrderManager.findById(refundOrder.getId()).orElse(refundOrder);
    }
}
