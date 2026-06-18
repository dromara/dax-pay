package org.dromara.daxpay.payment.admin.controller.demo.constant;

/// # Artemis 消息队列演示常量
///
/// 演示用的 address 与 tag 常量，地址命名遵循 kebab-case 约定。
/// Artemis 默认开启地址自动创建，无需在 broker 端预置。
///
/// @see org.dromara.daxpay.platform.common.artemis.service.ArtemisTemplateService
public interface DemoArtemisConstants {

    /// 点对点队列 address
    String QUEUE = "demo.queue";

    /// 发布订阅 topic address（broker 端路由类型需为 multicast）
    String TOPIC = "demo.topic";

    /// 延时消息队列 address
    String DELAY_QUEUE = "demo.delay";

    /// Tag 过滤演示队列 address
    String TAG_QUEUE = "demo.tag";

    /// 重要消息 Tag
    String TAG_IMPORTANT = "important";

    /// 普通消息 Tag
    String TAG_NORMAL = "normal";
}
