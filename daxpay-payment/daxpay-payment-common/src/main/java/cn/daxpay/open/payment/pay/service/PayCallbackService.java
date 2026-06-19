package cn.daxpay.open.payment.pay.service;

import cn.daxpay.open.platform.core.enums.pay.notice.CallbackStatusEnum;
import cn.daxpay.open.platform.core.exception.system.DataErrorException;
import cn.daxpay.open.payment.common.context.CallbackInfo;
import cn.daxpay.open.payment.common.context.PaymentContext;
import cn.daxpay.open.payment.common.enums.PayFundStatusEnum;
import cn.daxpay.open.payment.pay.order.dao.PayNormalOrderManager;
import cn.daxpay.open.payment.pay.order.entity.PayNormalOrder;
import cn.daxpay.open.payment.pay.order.dao.PayTradeManager;
import cn.daxpay.open.payment.pay.order.entity.PayTrade;
import com.baomidou.lock.LockInfo;
import com.baomidou.lock.LockTemplate;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

/// # 支付回调处理
///
@Slf4j
@Service
@RequiredArgsConstructor
public class PayCallbackService {

    private final PayTradeManager payTradeManager;
    private final PayNormalOrderManager payNormalOrderManager;
    private final PayUniHandleService payUniHandleService;
    private final PaymentContext apiContext;
    private final LockTemplate lockTemplate;

    /// 支付统一回调处理，返回支付产品编码
    @Transactional(rollbackFor = Exception.class)
    public String payCallback() {
        CallbackInfo callbackInfo = apiContext.getCallbackInfo();
        LockInfo lock = lockTemplate.lock("callback:payment:" + callbackInfo.getTradeNo(), 10000, 200);
        if (Objects.isNull(lock)) {
            callbackInfo.setCallbackStatus(CallbackStatusEnum.IGNORE)
                    .setCallbackErrorMsg("回调正在处理中，忽略本次回调请求");
            log.warn("订单号: {} 回调正在处理中，忽略本次回调请求", callbackInfo.getTradeNo());
            return null;
        }
        try {
            PayTrade trade = payTradeManager.findByTradeNo(callbackInfo.getTradeNo())
                    .orElse(null);
            if (Objects.isNull(trade)) {
                trade = payTradeManager.findByOutOrderNo(callbackInfo.getOutTradeNo())
                        .orElse(null);
            }
            if (Objects.isNull(trade)) {
                callbackInfo.setCallbackStatus(CallbackStatusEnum.NOT_FOUND)
                        .setCallbackErrorMsg("支付订单不存在");
                return null;
            }
            if (Objects.nonNull(callbackInfo.getOutTradeNo())) {
                trade.setOutOrderNo(callbackInfo.getOutTradeNo());
            }
            PayNormalOrder normalOrder = payNormalOrderManager.findById(trade.getContainerId())
                    .orElseThrow(() -> new DataErrorException("error.payment.order.payOrderNotExist"));
            if (Objects.equals(CallbackStatusEnum.SUCCESS.getCode(), callbackInfo.getTradeStatus())) {
                this.success(trade, normalOrder, callbackInfo);
            } else {
                this.fail(trade, normalOrder, callbackInfo);
            }
            return trade.getProduct();
        } finally {
            lockTemplate.releaseLock(lock);
        }
    }

    /// 成功处理
    private void success(PayTrade trade, PayNormalOrder normalOrder, CallbackInfo callbackInfo) {
        trade.setStatus(PayFundStatusEnum.SUCCESS.getCode());
        trade.setPayTime(callbackInfo.getFinishTime());
        trade.setCloseTime(null);
        if (Objects.nonNull(callbackInfo.getOutTradeNo())) {
            trade.setOutOrderNo(callbackInfo.getOutTradeNo());
        }
        payUniHandleService.paySuccess(trade);
    }

    /// 失败处理
    private void fail(PayTrade trade, PayNormalOrder normalOrder, CallbackInfo callbackInfo) {
        if (!Objects.equals(trade.getStatus(), PayFundStatusEnum.PROCESSING.getCode())) {
            callbackInfo.setCallbackStatus(CallbackStatusEnum.IGNORE)
                    .setCallbackErrorMsg("订单不是支付中状态，忽略");
            return;
        }
        payUniHandleService.payClose(trade, normalOrder);
    }
}
