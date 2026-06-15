package org.dromara.daxpay.payment.pay.service.order.pay;

import org.dromara.daxpay.platform.core.exception.operation.OperationFailException;

import org.dromara.daxpay.payment.pay.dao.order.pay.PayOrderManager;
import org.dromara.daxpay.payment.pay.entity.order.pay.PayOrder;
import org.dromara.daxpay.payment.pay.exception.TradeNotExistException;
import org.dromara.daxpay.payment.pay.service.assist.PaymentAssistService;
import org.dromara.daxpay.payment.pay.service.trade.pay.PayCloseService;
import org.dromara.daxpay.payment.pay.service.trade.pay.PaySyncService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.dromara.daxpay.platform.core.code.CommonCode;

/// # 支付订单服务
///
@Slf4j
@Service
@RequiredArgsConstructor
public class PayOrderService {
    private final PayOrderManager payOrderManager;
    private final PaySyncService paySyncService;

    private final PaymentAssistService paymentAssistService;
    private final PayCloseService payCloseService;

    /// 同步
    public void sync(Long id) {
        // 订单: 支付订单不存在
        PayOrder payOrder = payOrderManager.findById(id).orElseThrow(() -> new TradeNotExistException("error.payment.order.payOrderNotExist"));
        // 初始化商户和应用
        paymentAssistService.initMchAndApp(payOrder.getMchNo(),payOrder.getAppId());
        paySyncService.syncPayOrder(payOrder);
    }

    /// 关闭订单
    public void close(Long id) {
        // 订单: 支付订单不存在
        PayOrder payOrder = payOrderManager.findById(id).orElseThrow(() -> new TradeNotExistException("error.payment.order.payOrderNotExist"));
        // 初始化商户和应用
        paymentAssistService.initMchAndApp(payOrder.getMchNo(),payOrder.getAppId());
        payCloseService.closeOrder(payOrder,false);
    }

    /// 撤销订单
    public void cancel(Long id) {
        // 订单: 支付订单不存在
        PayOrder payOrder = payOrderManager.findById(id).orElseThrow(() -> new TradeNotExistException("error.payment.order.payOrderNotExist"));
        // 初始化商户和应用
        paymentAssistService.initMchAndApp(payOrder.getMchNo(),payOrder.getAppId());
        payCloseService.closeOrder(payOrder,true);
    }

    
    
}
