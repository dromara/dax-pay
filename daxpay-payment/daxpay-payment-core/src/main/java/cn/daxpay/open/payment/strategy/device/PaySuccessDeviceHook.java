package cn.daxpay.open.payment.strategy.device;

import cn.daxpay.open.payment.trade.order.entity.PayTrade;

/// # 支付成功设备通知钩子(可短路 SPI)
///
/// 实现放在设备域模块 `daxpay-device`, 无 Bean 时支付主链路跳过设备通知(视为无外设)。
/// 由 [cn.daxpay.open.payment.trade.runtime.service.pay.common.PayUniHandleService#afterSuccess] 在支付成功统一入口调用。
///
/// **契约**: 仅普通支付与网关支付成功触发(实现须按交易形态自行过滤); 实现内部异步执行且自行吞异常,
/// 旁路不反压支付主链路, 未装配时不得影响资金态。
public interface PaySuccessDeviceHook {

    /// 支付成功后播报与小票下发(事务提交后异步执行)
    void broadcast(PayTrade trade);
}
