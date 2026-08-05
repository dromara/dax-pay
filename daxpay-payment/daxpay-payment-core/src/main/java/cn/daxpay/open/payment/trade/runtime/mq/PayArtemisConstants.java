package cn.daxpay.open.payment.trade.runtime.mq;

import cn.daxpay.open.payment.trade.runtime.consumer.GatewayTimeoutConsumer;
import cn.daxpay.open.payment.trade.runtime.consumer.NormalPayTimeoutConsumer;

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

    /// 商户出站通知发送队列 address
    /// 任务落库后 afterCommit 投递（含失败延时重试），由 [cn.daxpay.open.payment.trade.notice.consumer.MchNoticeSendConsumer] 消费。
    String MCH_NOTICE_SEND = "mch.notice.send";

    /// 转账延迟同步队列 address
    /// 转账返回处理中后延时 2 分钟投递, 由
    /// [cn.daxpay.open.payment.trade.transfer.runtime.consumer.TransferSyncConsumer] 消费触发状态同步。
    String TRANSFER_SYNC_QUEUE = "pay.transfer.sync";

    /// 商户出站 MQ 通知 Topic 前缀
    /// MQ 投递方式(notifyWay=mq)下, 按应用隔离的 Topic: `<前缀>.<appId>`, 商户侧用 JMS 持久订阅消费。
    /// broker 端该 address 需配为 multicast 路由类型。
    String MCH_NOTICE_TOPIC_PREFIX = "daxpay.notice";
}
