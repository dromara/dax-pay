package cn.daxpay.open.payment.pay.service;

import cn.daxpay.open.platform.core.enums.pay.notice.CallbackStatusEnum;
import cn.daxpay.open.platform.core.exception.system.DataErrorException;
import cn.daxpay.open.payment.common.callback.CallbackData;
import cn.daxpay.open.payment.common.enums.PayFundStatusEnum;
import cn.daxpay.open.payment.pay.order.dao.NormalPayOrderManager;
import cn.daxpay.open.payment.pay.order.entity.NormalPayOrder;
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
/// 回调数据通过函数参数显式传递([CallbackData]),不依赖线程上下文。
@Slf4j
@Service
@RequiredArgsConstructor
public class PayCallbackService {

    private final PayTradeManager payTradeManager;
    private final NormalPayOrderManager payNormalOrderManager;
    private final PayUniHandleService payUniHandleService;
    private final LockTemplate lockTemplate;

    /// 支付统一回调处理，返回支付产品编码
    @Transactional(rollbackFor = Exception.class)
    public String payCallback(CallbackData callbackData) {
        LockInfo lock = lockTemplate.lock("callback:payment:" + callbackData.getTradeNo(), 10000, 200);
        if (Objects.isNull(lock)) {
            callbackData.setCallbackStatus(CallbackStatusEnum.IGNORE)
                    .setCallbackErrorMsg("回调正在处理中，忽略本次回调请求");
            log.warn("订单号: {} 回调正在处理中，忽略本次回调请求", callbackData.getTradeNo());
            return null;
        }
        try {
            PayTrade trade = payTradeManager.findByTradeNo(callbackData.getTradeNo())
                    .orElse(null);
            if (Objects.isNull(trade)) {
                trade = payTradeManager.findByOutOrderNo(callbackData.getOutTradeNo())
                        .orElse(null);
            }
            if (Objects.isNull(trade)) {
                callbackData.setCallbackStatus(CallbackStatusEnum.NOT_FOUND)
                        .setCallbackErrorMsg("支付订单不存在");
                return null;
            }
            if (Objects.nonNull(callbackData.getOutTradeNo())) {
                trade.setOutOrderNo(callbackData.getOutTradeNo());
            }
            NormalPayOrder normalOrder = payNormalOrderManager.findById(trade.getContainerId())
                    .orElseThrow(() -> new DataErrorException("error.payment.order.payOrderNotExist"));
            if (Objects.equals(CallbackStatusEnum.SUCCESS.getCode(), callbackData.getTradeStatus())) {
                this.success(trade, normalOrder, callbackData);
            } else {
                this.fail(trade, normalOrder, callbackData);
            }
            return trade.getProduct();
        } finally {
            lockTemplate.releaseLock(lock);
        }
    }

    /// 成功处理
    private void success(PayTrade trade, NormalPayOrder normalOrder, CallbackData callbackData) {
        trade.setStatus(PayFundStatusEnum.SUCCESS.getCode());
        trade.setPayTime(callbackData.getFinishTime());
        trade.setCloseTime(null);
        if (Objects.nonNull(callbackData.getOutTradeNo())) {
            trade.setOutOrderNo(callbackData.getOutTradeNo());
        }
        payUniHandleService.paySuccess(trade);
    }

    /// 失败处理
    private void fail(PayTrade trade, NormalPayOrder normalOrder, CallbackData callbackData) {
        if (!Objects.equals(trade.getStatus(), PayFundStatusEnum.PROCESSING.getCode())) {
            callbackData.setCallbackStatus(CallbackStatusEnum.IGNORE)
                    .setCallbackErrorMsg("订单不是支付中状态，忽略");
            return;
        }
        payUniHandleService.payClose(trade, normalOrder);
    }
}
