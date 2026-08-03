package cn.daxpay.open.payment.trade.runtime.service.callback;

import cn.daxpay.open.payment.trade.runtime.bo.RefundCallbackData;
import cn.daxpay.open.payment.trade.enums.RefundOrderStatusEnum;
import cn.daxpay.open.payment.trade.order.dao.RefundOrderManager;
import cn.daxpay.open.payment.trade.order.entity.RefundOrder;
import cn.daxpay.open.payment.trade.runtime.service.refund.RefundSettleService;
import cn.daxpay.open.platform.common.redis.lock.LockExecutor;
import cn.daxpay.open.platform.core.enums.pay.notice.CallbackStatusEnum;
import cn.hutool.core.util.StrUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

/// # 退款回调处理
///
/// 与 [PayCallbackService] 对称:支付回调用 tradeNo 反查支付单,
/// 退款回调用 refundNo / relationOrderNo / outRefundNo 反查退款单。
/// 成功/失败结算委托 [RefundSettleService](预占模型, 与发起/同步共用 trade 级锁)。
///
/// 回调数据通过函数参数显式传递([RefundCallbackData]),不依赖线程上下文。
@Slf4j
@Service
@RequiredArgsConstructor
public class RefundCallbackService {

    private final RefundOrderManager refundOrderManager;
    private final RefundSettleService refundSettleService;
    private final LockExecutor lockExecutor;

    /// 退款统一回调处理
    @Transactional(rollbackFor = Exception.class)
    public void refundCallback(RefundCallbackData callbackData) {
        String lockId = StrUtil.blankToDefault(callbackData.getRefundNo(),
                StrUtil.blankToDefault(callbackData.getRelationOrderNo(), callbackData.getOutRefundNo()));
        if (!lockExecutor.tryRun("callback:refund:" + lockId, () -> {
            RefundOrder refundOrder = resolveRefundOrder(callbackData);
            if (Objects.isNull(refundOrder)) {
                callbackData.setCallbackStatus(CallbackStatusEnum.NOT_FOUND)
                        .setCallbackErrorMsg("退款订单不存在");
                log.warn("退款回调: 退款订单不存在 refundNo={} relationOrderNo={} outRefundNo={}",
                        callbackData.getRefundNo(), callbackData.getRelationOrderNo(), callbackData.getOutRefundNo());
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
            // 非终态回调(如通道返回 PROCESSING): 不触发结算, 保持 PROGRESS
            if (StrUtil.isBlank(callbackData.getTradeStatus())) {
                callbackData.setCallbackStatus(CallbackStatusEnum.IGNORE)
                        .setCallbackErrorMsg("退款回调非终态，忽略: " + callbackData.getTradeErrorMsg());
                log.warn("退款回调非终态，忽略: refundNo={} tradeStatus为空",
                        callbackData.getRefundNo());
                return;
            }
            if (Objects.equals(CallbackStatusEnum.SUCCESS.getCode(), callbackData.getTradeStatus())) {
                this.success(refundOrder, callbackData);
            } else if (Objects.equals(CallbackStatusEnum.CLOSE.getCode(), callbackData.getTradeStatus())) {
                this.close(refundOrder, callbackData);
            } else {
                this.fail(refundOrder, callbackData);
            }
        })) {
            callbackData.setCallbackStatus(CallbackStatusEnum.IGNORE)
                    .setCallbackErrorMsg("退款回调正在处理中，忽略本次回调请求");
            log.warn("退款号: {} 回调正在处理中，忽略本次回调请求", lockId);
        }
    }

    /// 反查退款单: refundNo → relationOrderNo → outRefundNo
    private RefundOrder resolveRefundOrder(RefundCallbackData callbackData) {
        if (StrUtil.isNotBlank(callbackData.getRefundNo())) {
            var byRefundNo = refundOrderManager.findByRefundNo(callbackData.getRefundNo());
            if (byRefundNo.isPresent()) {
                return byRefundNo.get();
            }
        }
        // 特殊通道仅回传变形上送串
        if (StrUtil.isNotBlank(callbackData.getRelationOrderNo())) {
            var byRelation = refundOrderManager.findByRelationOrderNo(callbackData.getRelationOrderNo());
            if (byRelation.isPresent()) {
                return byRelation.get();
            }
        }
        // 容错: 部分通道仅回传其内部退款号
        if (StrUtil.isNotBlank(callbackData.getOutRefundNo())) {
            return refundOrderManager.findByOutRefundNo(callbackData.getOutRefundNo()).orElse(null);
        }
        return null;
    }

    /// 退款成功: 仅改态(余额已在发起时预占)
    private void success(RefundOrder refundOrder, RefundCallbackData callbackData) {
        boolean settled = refundSettleService.settleSuccess(
                refundOrder.getId(),
                callbackData.getFinishTime(),
                callbackData.getOutRefundNo(),
                callbackData.getRelationOrderNo());
        if (!settled) {
            callbackData.setCallbackStatus(CallbackStatusEnum.IGNORE)
                    .setCallbackErrorMsg("退款单已处理，忽略回调");
        }
    }

    /// 退款失败: 改态 + 回滚预占
    private void fail(RefundOrder refundOrder, RefundCallbackData callbackData) {
        boolean settled = refundSettleService.settleFail(
                refundOrder.getId(),
                callbackData.getFinishTime(),
                callbackData.getOutRefundNo(),
                callbackData.getRelationOrderNo(),
                callbackData.getTradeErrorMsg());
        if (!settled) {
            callbackData.setCallbackStatus(CallbackStatusEnum.IGNORE)
                    .setCallbackErrorMsg("退款单已处理，忽略回调");
        }
    }

    /// 退款关闭: 改态 + 回滚预占(与失败的资金结果一致, 但状态语义不同, 商户通知事件为 refund.close)
    private void close(RefundOrder refundOrder, RefundCallbackData callbackData) {
        boolean settled = refundSettleService.settleClose(
                refundOrder.getId(),
                callbackData.getFinishTime(),
                callbackData.getOutRefundNo(),
                callbackData.getRelationOrderNo(),
                callbackData.getTradeErrorMsg());
        if (!settled) {
            callbackData.setCallbackStatus(CallbackStatusEnum.IGNORE)
                    .setCallbackErrorMsg("退款单已处理，忽略回调");
        }
    }
}
