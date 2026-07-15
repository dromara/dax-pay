package cn.daxpay.open.payment.strategy.plugin;

import cn.daxpay.open.payment.trade.order.entity.PayRefundOrder;
import cn.daxpay.open.payment.trade.order.entity.PayTrade;

/// # 支付插件策略
///
/// 协议适配层（如易支付）在支付/退款生命周期上的扩展点。
/// 由 [cn.daxpay.open.payment.trade.runtime.service.plugin.PayPluginAssistService] 广播调用。
///
/// @author xxm
/// @since 2026/03/22
public interface AbsPayPluginStrategy {

    /// 支付成功
    default void paySuccess(PayTrade trade) {
    }

    /// 支付失败
    default void payFail(PayTrade trade) {
    }

    /// 支付关闭
    default void payClose(PayTrade trade) {
    }

    /// 退款成功
    default void refundSuccess(PayTrade trade, PayRefundOrder refundOrder) {
    }

    /// 退款关闭
    default void refundClose(PayTrade trade, PayRefundOrder refundOrder) {
    }
}
