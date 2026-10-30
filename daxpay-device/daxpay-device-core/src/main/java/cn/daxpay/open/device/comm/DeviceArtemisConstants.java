package cn.daxpay.open.device.comm;

/// # 设备域 Artemis 消息地址常量
///
/// 设备域使用的 Artemis address 常量，地址命名遵循 kebab-case 约定。
/// Artemis 默认开启地址自动创建，无需在 broker 端预置。
///
/// 命名红线: `daxpay.device.*` 是 broker 端为设备 MQTT 主题保留的领地
/// (open 面板 MQTT 批已配 `daxpay.device.#` 安全矩阵: consume 权限仅 app-device 角色,
/// 且消息 10 分钟过期/队列自动删除, security-setting 最具体匹配独占、不继承 `#` 段权限),
/// 服务端内部 JMS 队列落入该前缀将无权消费——设备域内部队列一律挂 `daxpay.pay.device.*`。
///
/// @see cn.daxpay.open.platform.common.artemis.service.ArtemisTemplateService
public interface DeviceArtemisConstants {

    /// 支付成功设备通知队列 address
    /// 支付成功事务提交后由支付成功钩子链路投递(仅带 tradeNo),
    /// 由设备域消费者(虚拟设备播报/打印编排)消费执行播报/打印。
    /// 属服务端内部队列(非设备 MQTT 主题), 不得挂 `daxpay.device.*` 前缀(见类注释命名红线)。
    String PAY_SUCCESS_QUEUE = "daxpay.pay.device.success";
}
