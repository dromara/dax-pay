package cn.daxpay.open.device.virtual.client.result;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

/// # 虚拟音箱激活结果(凭证六件套)
///
/// 服务器地址由 APP 编译期内置, 激活响应不再下发后端接口基址。
@Data
@Accessors(chain = true)
@Schema(title = "虚拟音箱激活结果")
public class VirtualDeviceActivateResult {

    @Schema(description = "设备号")
    private String deviceNo;

    /// 播报加密密钥(AES-256-GCM 32 字符, 兼作回执签名密钥; 每次激活轮换, 旧密文/旧签名即时作废)
    @Schema(description = "播报密钥(每次激活轮换)")
    private String secret;

    @Schema(description = "MQTT WSS 接入地址")
    private String brokerUrl;

    @Schema(description = "订阅主题(点号形式)")
    private String topic;

    /// MQTT 连接用户名(broker 静态设备账号 app-device, 仅 consume 权限; 面板轮换密码后重新激活取新值)
    @Schema(description = "MQTT 连接用户名")
    private String mqttUsername;

    /// MQTT 连接密码(随用户名由配置下发, 打包进发布包之外的灵活轮换通道)
    @Schema(description = "MQTT 连接密码")
    private String mqttPassword;
}
