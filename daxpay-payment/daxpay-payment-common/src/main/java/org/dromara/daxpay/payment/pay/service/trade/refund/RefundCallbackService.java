package org.dromara.daxpay.payment.pay.service.trade.refund;

import org.dromara.daxpay.platform.core.exception.DataNotExistException;
import org.dromara.daxpay.platform.common.i18n.util.I18nUtil;
import org.dromara.daxpay.payment.common.context.CallbackInfo;
import org.dromara.daxpay.payment.pay.dao.order.pay.PayOrderManager;
import org.dromara.daxpay.payment.pay.dao.order.refund.RefundOrderManager;
import org.dromara.daxpay.payment.pay.entity.order.pay.PayOrder;
import org.dromara.daxpay.payment.pay.entity.order.refund.RefundOrder;
import org.dromara.daxpay.platform.core.enums.pay.notice.CallbackStatusEnum;
import org.dromara.daxpay.platform.core.enums.pay.refund.RefundStatusEnum;
import org.dromara.daxpay.payment.common.context.PaymentContext;
import org.dromara.daxpay.payment.pay.service.trade.TradeUniHandleService;
import com.baomidou.lock.LockInfo;
import com.baomidou.lock.LockTemplate;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Objects;

/// # 退款回调
///
@Slf4j
@Service
@RequiredArgsConstructor
public class RefundCallbackService {
    private final RefundOrderManager refundOrderManager;
    private final LockTemplate lockTemplate;
    private final PayOrderManager payOrderManager;
    private final TradeUniHandleService tradeUniHandleService;

    private final PaymentContext apiContext;

    /// 退款回调统一处理, 返回退款产品编码
    public String refundCallback() {

        CallbackInfo callbackInfo = apiContext.getCallbackInfo();
        // 加锁
        LockInfo lock = lockTemplate.lock("callback:refund:" + callbackInfo.getTradeNo(),10000, 200);
        if (Objects.isNull(lock)){
            callbackInfo.setCallbackStatus(CallbackStatusEnum.IGNORE).setCallbackErrorMsg(I18nUtil.get("pay.error.callback.processing"));
            log.warn("订单号: {} 回调正在处理中，忽略本次回调请求", callbackInfo.getTradeNo());
            return null;
        }
        try {
            // 获取退款单
            RefundOrder refundOrder = refundOrderManager.findByRefundNo(callbackInfo.getTradeNo()).orElse(null);
            // 退款单不存在,记录回调记录
            if (Objects.isNull(refundOrder)) {
                callbackInfo.setCallbackStatus(CallbackStatusEnum.NOT_FOUND).setCallbackErrorMsg(I18nUtil.get("pay.error.callback.refundNotFound"));
                return null;
            }
            // 退款单已经被处理, 记录回调记录
            if (!Objects.equals(RefundStatusEnum.PROGRESS.getCode(), refundOrder.getStatus())) {
                callbackInfo.setCallbackStatus(CallbackStatusEnum.IGNORE).setCallbackErrorMsg(I18nUtil.get("pay.error.callback.refundAlreadyProcessed"));
                return null;
            }

            // 退款成功
            if (Objects.equals(RefundStatusEnum.SUCCESS.getCode(), callbackInfo.getTradeStatus())) {
                this.success(refundOrder);
            }
            // 退款失败
            if (Objects.equals(RefundStatusEnum.FAIL.getCode(), callbackInfo.getTradeStatus())){
                this.close(refundOrder);
            }
            // 退款异常
            if (Objects.equals(RefundStatusEnum.CLOSE.getCode(), callbackInfo.getTradeStatus())){
                this.close(refundOrder);
            }
            return refundOrder.getProduct();
        } finally {
            lockTemplate.releaseLock(lock);
        }
    }

    /// 退款成功, 更新退款单和支付单
    private void success(RefundOrder refundOrder) {
        var callbackInfo = apiContext.getCallbackInfo();
        PayOrder payOrder = payOrderManager.findById(refundOrder.getOrderId())
                .orElseThrow(DataNotExistException::new);
        // 设置退款完成时间
        refundOrder.setFinishTime(callbackInfo.getFinishTime());
        // 更新订单和退款相关订单
        tradeUniHandleService.refundSuccess(payOrder,refundOrder);
    }

    /// 退款失败, 关闭退款单并将失败的退款金额归还回订单
    private void close(RefundOrder refundOrder) {
        var callbackInfo = apiContext.getCallbackInfo();

        var payOrder = payOrderManager.findById(refundOrder.getOrderId())
                .orElseThrow(() -> new DataNotExistException("error.payment.order.refundPayOrderNotExist"));
        refundOrder.setErrorMsg(callbackInfo.getTradeErrorMsg());

        // 退款关闭相关处理
        tradeUniHandleService.refundClose(payOrder,refundOrder);
    }
}
