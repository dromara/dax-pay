package cn.daxpay.open.payment.trade.runtime.service.callback;

import cn.daxpay.open.payment.trade.runtime.bo.RefundCallbackData;
import cn.daxpay.open.payment.trade.enums.RefundOrderStatusEnum;
import cn.daxpay.open.payment.trade.order.dao.PayRefundOrderManager;
import cn.daxpay.open.payment.trade.order.dao.PayTradeManager;
import cn.daxpay.open.payment.trade.order.entity.PayRefundOrder;
import cn.daxpay.open.platform.core.enums.pay.notice.CallbackStatusEnum;
import com.baomidou.lock.LockInfo;
import com.baomidou.lock.LockTemplate;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

/// # 退款回调处理
///
/// 与 [PayCallbackService] 对称:支付回调用 tradeNo 反查支付单,
/// 退款回调用 refundNo 反查退款单, 完成退款单状态流转与可退余额扣减。
///
/// 回调数据通过函数参数显式传递([RefundCallbackData]),不依赖线程上下文。
@Slf4j
@Service
@RequiredArgsConstructor
public class RefundCallbackService {

    private final PayRefundOrderManager payRefundOrderManager;
    private final PayTradeManager payTradeManager;
    private final LockTemplate lockTemplate;

    /// 退款统一回调处理
    @Transactional(rollbackFor = Exception.class)
    public void refundCallback(RefundCallbackData callbackData) {
        LockInfo lock = lockTemplate.lock("callback:refund:" + callbackData.getRefundNo(), 10000, 200);
        if (Objects.isNull(lock)) {
            callbackData.setCallbackStatus(CallbackStatusEnum.IGNORE)
                    .setCallbackErrorMsg("退款回调正在处理中，忽略本次回调请求");
            log.warn("退款号: {} 回调正在处理中，忽略本次回调请求", callbackData.getRefundNo());
            return;
        }
        try {
            PayRefundOrder refundOrder = payRefundOrderManager.findByRefundNo(callbackData.getRefundNo())
                    .orElse(null);
            // 容错:部分通道仅回传其内部退款号, 用通道退款流水号反查
            if (Objects.isNull(refundOrder) && Objects.nonNull(callbackData.getOutRefundNo())) {
                refundOrder = payRefundOrderManager.findByOutRefundNo(callbackData.getOutRefundNo())
                        .orElse(null);
            }
            if (Objects.isNull(refundOrder)) {
                callbackData.setCallbackStatus(CallbackStatusEnum.NOT_FOUND)
                        .setCallbackErrorMsg("退款订单不存在");
                log.warn("退款回调: 退款订单不存在 refundNo={} outRefundNo={}",
                        callbackData.getRefundNo(), callbackData.getOutRefundNo());
                return;
            }
            // 终态守卫: 已成功/失败/关闭的退款单不再重复处理
            String oldStatus = refundOrder.getStatus();
            if (Objects.equals(oldStatus, RefundOrderStatusEnum.SUCCESS.getCode())
                    || Objects.equals(oldStatus, RefundOrderStatusEnum.FAIL.getCode())
                    || Objects.equals(oldStatus, RefundOrderStatusEnum.CLOSE.getCode())) {
                callbackData.setCallbackStatus(CallbackStatusEnum.IGNORE)
                        .setCallbackErrorMsg("退款单已处于终态，忽略回调");
                log.warn("退款回调: 退款单 {} 已处于终态 {}，忽略", refundOrder.getRefundNo(), oldStatus);
                return;
            }
            // 回填通道退款流水号
            if (Objects.nonNull(callbackData.getOutRefundNo())) {
                refundOrder.setOutRefundNo(callbackData.getOutRefundNo());
            }
            if (Objects.equals(CallbackStatusEnum.SUCCESS.getCode(), callbackData.getTradeStatus())) {
                this.success(refundOrder, callbackData);
            } else {
                this.fail(refundOrder, callbackData);
            }
        } finally {
            lockTemplate.releaseLock(lock);
        }
    }

    /// 退款成功: 状态置成功, 记完成时间, 扣减原支付订单可退余额
    private void success(PayRefundOrder refundOrder, RefundCallbackData callbackData) {
        refundOrder.setStatus(RefundOrderStatusEnum.SUCCESS.getCode());
        if (Objects.nonNull(callbackData.getFinishTime())) {
            refundOrder.setFinishTime(callbackData.getFinishTime());
        }
        refundOrder.setErrorMsg(null);
        payRefundOrderManager.updateById(refundOrder);
        // 扣减原支付订单可退余额(与 PayRefundSyncService 同口径)
        payTradeManager.findByTradeNo(refundOrder.getOrderNo()).ifPresent(trade -> {
            long newBalance = (trade.getRefundableBalance() == null ? 0 : trade.getRefundableBalance())
                    - refundOrder.getAmount();
            trade.setRefundableBalance(Math.max(newBalance, 0));
            payTradeManager.updateById(trade);
        });
    }

    /// 退款失败: 状态置失败, 记错误信息
    private void fail(PayRefundOrder refundOrder, RefundCallbackData callbackData) {
        refundOrder.setStatus(RefundOrderStatusEnum.FAIL.getCode());
        if (Objects.nonNull(callbackData.getFinishTime())) {
            refundOrder.setFinishTime(callbackData.getFinishTime());
        }
        refundOrder.setErrorMsg(callbackData.getTradeErrorMsg());
        payRefundOrderManager.updateById(refundOrder);
    }
}
