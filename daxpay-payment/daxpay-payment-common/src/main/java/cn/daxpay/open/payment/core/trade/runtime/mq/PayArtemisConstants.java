package cn.daxpay.open.payment.core.trade.runtime.mq;

/// # 支付业务 Artemis 消息地址常量
///
/// 支付核心业务使用的 Artemis address 常量，地址命名遵循 kebab-case 约定。
/// Artemis 默认开启地址自动创建，无需在 broker 端预置。
///
/// 本包为共享消息契约层: 消息体与常量被 pay 侧(注册延时消息)和 close 侧(消费关单)共用,
/// 纯数据不依赖 service, 避免 pay → close 跨领域环依赖。
///
/// @see cn.daxpay.open.platform.common.artemis.service.ArtemisTemplateService
public interface PayArtemisConstants {

    /// 普通支付超时关单队列 address
    /// 下单时通过 `sendDelayAt(...)` 按订单过期时间定时投递，到期后由
    /// [NormalPayTimeoutConsumer] 消费触发自动关单。
    String NORMAL_TIMEOUT_QUEUE = "pay.normal.timeout";

    /// 网关支付超时关单队列 address
    /// 预下单时按容器过期时间投递，到期后由 [GatewayTimeoutConsumer] 消费。
    String GATEWAY_TIMEOUT_QUEUE = "pay.gateway.timeout";
}
