package cn.daxpay.open.payment.core.trade.mq;

/// # 支付业务 Artemis 消息地址常量
///
/// 支付核心业务使用的 Artemis address 常量，地址命名遵循 kebab-case 约定。
/// Artemis 默认开启地址自动创建，无需在 broker 端预置。
///
/// @see cn.daxpay.open.platform.common.artemis.service.ArtemisTemplateService
public interface PayArtemisConstants {

    /// 普通支付超时关单队列 address
    /// 下单时通过 `sendDelayAt(...)` 按订单过期时间定时投递，到期后由
    /// [NormalPayTimeoutConsumer] 消费触发自动关单。
    String NORMAL_TIMEOUT_QUEUE = "pay.normal.timeout";
}
