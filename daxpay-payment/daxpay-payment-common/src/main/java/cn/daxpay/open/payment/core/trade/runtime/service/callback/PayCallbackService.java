package cn.daxpay.open.payment.core.trade.runtime.service.callback;

import cn.daxpay.open.payment.common.callback.CallbackData;
import cn.daxpay.open.payment.common.enums.PayFundStatusEnum;
import cn.daxpay.open.payment.common.enums.PayTradeTypeEnum;
import cn.daxpay.open.payment.gateway.dao.GatewayPayOrderManager;
import cn.daxpay.open.payment.core.trade.order.dao.NormalPayOrderManager;
import cn.daxpay.open.payment.core.trade.order.dao.PayTradeManager;
import cn.daxpay.open.payment.gateway.entity.GatewayPayOrder;
import cn.daxpay.open.payment.core.trade.order.entity.NormalPayOrder;
import cn.daxpay.open.payment.core.trade.order.entity.PayTrade;
import cn.daxpay.open.payment.core.trade.runtime.service.pay.PayUniHandleService;
import cn.daxpay.open.platform.core.enums.pay.notice.CallbackStatusEnum;
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
    private final GatewayPayOrderManager gatewayPayOrderManager;
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
            if (Objects.equals(CallbackStatusEnum.SUCCESS.getCode(), callbackData.getTradeStatus())) {
                this.success(trade, callbackData);
            } else {
                this.fail(trade, callbackData);
            }
            return resolveProduct(trade);
        } finally {
            lockTemplate.releaseLock(lock);
        }
    }

    /// 从容器获取 product(回调返回供通道策略识别来源)
    private String resolveProduct(PayTrade trade) {
        if (Objects.equals(trade.getTradeType(), PayTradeTypeEnum.GATEWAY.getCode())) {
            return gatewayPayOrderManager.findById(trade.getContainerId())
                    .map(GatewayPayOrder::getProduct).orElse(null);
        }
        return payNormalOrderManager.findById(trade.getContainerId())
                .map(NormalPayOrder::getProduct).orElse(null);
    }

    private void success(PayTrade trade, CallbackData callbackData) {
        trade.setStatus(PayFundStatusEnum.SUCCESS.getCode());
        trade.setPayTime(callbackData.getFinishTime());
        trade.setCloseTime(null);
        if (Objects.nonNull(callbackData.getOutTradeNo())) {
            trade.setOutOrderNo(callbackData.getOutTradeNo());
        }
        payUniHandleService.paySuccess(trade);
    }

    private void fail(PayTrade trade, CallbackData callbackData) {
        if (!Objects.equals(trade.getStatus(), PayFundStatusEnum.PROCESSING.getCode())) {
            callbackData.setCallbackStatus(CallbackStatusEnum.IGNORE)
                    .setCallbackErrorMsg("订单不是支付中状态，忽略");
            return;
        }
        payUniHandleService.payClose(trade, false);
    }
}
