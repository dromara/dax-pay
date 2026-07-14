package cn.daxpay.open.payment.trade.runtime.service.callback;

import cn.daxpay.open.payment.trade.runtime.bo.RefundCallbackData;
import cn.daxpay.open.payment.trade.enums.RefundOrderStatusEnum;
import cn.daxpay.open.payment.trade.order.dao.PayRefundOrderManager;
import cn.daxpay.open.payment.trade.order.entity.PayRefundOrder;
import cn.daxpay.open.payment.trade.runtime.service.refund.PayRefundSettleService;
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
/// 退款回调用 refundNo 反查退款单。
/// 成功/失败结算委托 [PayRefundSettleService](预占模型, 与发起/同步共用 trade 级锁)。
///
/// 回调数据通过函数参数显式传递([RefundCallbackData]),不依赖线程上下文。
@Slf4j
@Service
@RequiredArgsConstructor
public class RefundCallbackService {

    private final PayRefundOrderManager payRefundOrderManager;
    private final PayRefundSettleService payRefundSettleService;
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
            if (Objects.equals(CallbackStatusEnum.SUCCESS.getCode(), callbackData.getTradeStatus())) {
                this.success(refundOrder, callbackData);
            } else {
                this.fail(refundOrder, callbackData);
            }
        } finally {
            lockTemplate.releaseLock(lock);
        }
    }

    /// 退款成功: 仅改态(余额已在发起时预占)
    private void success(PayRefundOrder refundOrder, RefundCallbackData callbackData) {
        boolean settled = payRefundSettleService.settleSuccess(
                refundOrder.getId(), callbackData.getFinishTime(), callbackData.getOutRefundNo());
        if (!settled) {
            callbackData.setCallbackStatus(CallbackStatusEnum.IGNORE)
                    .setCallbackErrorMsg("退款单已处理，忽略回调");
        }
    }

    /// 退款失败: 改态 + 回滚预占
    private void fail(PayRefundOrder refundOrder, RefundCallbackData callbackData) {
        boolean settled = payRefundSettleService.settleFail(
                refundOrder.getId(),
                callbackData.getFinishTime(),
                callbackData.getOutRefundNo(),
                callbackData.getTradeErrorMsg());
        if (!settled) {
            callbackData.setCallbackStatus(CallbackStatusEnum.IGNORE)
                    .setCallbackErrorMsg("退款单已处理，忽略回调");
        }
    }
}
