package cn.daxpay.open.device.virtual.client.result;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

/// # 虚拟音箱激活结果(凭证七件套)
///
/// plus 六件套 + serverBaseUrl(开源版新增: 扫码激活协议以服务器地址替代激活码,
/// 下发后端接口基址供 APP 重建 ApiClient; 平台端点配置未填时该字段为 null,
/// APP 走手填兜底)。
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

    /// 后端接口基址(开源版新增: 平台端点配置「后端 API 地址」, APP 据此重建 ApiClient;
    /// 配置未填时为 null, APP 引导手填)
    @Schema(description = "后端接口基址(未配置为空)")
    private String serverBaseUrl;
}
