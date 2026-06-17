package org.dromara.daxpay.payment.old.pay.service.assist;

import org.dromara.daxpay.payment.old.pay.entity.notice.callback.MerchantCallbackTask;
import org.dromara.daxpay.payment.old.pay.entity.order.pay.PayOrder;
import org.dromara.daxpay.payment.old.pay.entity.order.pay.PayOrderExpand;
import org.dromara.daxpay.payment.old.pay.entity.order.refund.RefundOrder;
import org.dromara.daxpay.payment.strategy.pay.AbsPayPluginStrategy;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

/// # 支付插件辅助服务类
///
@Slf4j
@Service
@RequiredArgsConstructor
public class PayPluginAssistService {

    private final List<AbsPayPluginStrategy> absPayPluginStrategies;

    /// 支付成功处理
    public void paySuccess(PayOrder payOrder, PayOrderExpand orderExpand){
        for (var strategy : absPayPluginStrategies) {
            try {
                strategy.paySuccess(payOrder, orderExpand);
            } catch (Exception e) {
                log.error("支付成功处理失败: {}:",  e.getMessage());
            }
        }
    }

    /// 支付失败处理
    public void payFail(PayOrder order){
        for (var strategy : absPayPluginStrategies) {
            try {
                strategy.payFail(order);
            } catch (Exception e) {
                log.error("支付失败处理失败: {}:",  e.getMessage());
            }
        }
    }

    /// 支付关闭处理
    public void payClose(PayOrder order){
        for (var strategy : absPayPluginStrategies) {
            try {
                strategy.payClose(order);
            } catch (Exception e) {
                log.error("支付关闭处理失败: {}:",  e.getMessage());
            }
        }
    }

    /// 退款成功处理
    public void refundSuccess(PayOrder payOrder, RefundOrder refundOrder){
        for (var strategy : absPayPluginStrategies) {
            try {
                strategy.refundSuccess(payOrder, refundOrder);
            } catch (Exception e) {
                log.error("退款成功处理失败: {}:",  e.getMessage());
            }
        }
    }

    /// 退款关闭处理
    public void refundClose(PayOrder payOrder, RefundOrder refundOrder){
        for (var strategy : absPayPluginStrategies) {
            try {
                strategy.refundClose(payOrder,refundOrder);
            } catch (Exception e) {
                log.error("退款关闭处理失败: {}:",  e.getMessage());
            }
        }
    }

    /// 通知发送处理
    public void noticeSend(MerchantCallbackTask task, boolean autoSend){
        for (var strategy : absPayPluginStrategies){
            try {
                strategy.noticeSend(task, autoSend);
            } catch (Exception e) {
                log.error("通知发送处理失败: {}:",  e.getMessage());
            }
        }
    }

}
