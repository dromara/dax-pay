package org.dromara.daxpay.payment.strategy.pay;

import org.dromara.daxpay.payment.old.pay.entity.notice.callback.MerchantCallbackTask;
import org.dromara.daxpay.payment.old.pay.entity.order.pay.PayOrder;
import org.dromara.daxpay.payment.old.pay.entity.order.pay.PayOrderExpand;

/// # 支付插件
///
public interface AbsPayPluginStrategy {

    /// 支付成功
    default void paySuccess(PayOrder payOrder, PayOrderExpand expand){}

    /// 支付失败
    default void payFail(PayOrder payOrder){}

    /// 支付关闭
    default void payClose(PayOrder payOrder){}

    /// 回调消息发送
    default void noticeSend(MerchantCallbackTask task, boolean autoSend){}

}
