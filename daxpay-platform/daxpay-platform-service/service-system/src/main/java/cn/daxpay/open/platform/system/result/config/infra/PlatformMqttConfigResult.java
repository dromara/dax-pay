package cn.daxpay.open.platform.system.result.config.infra;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

/// # 平台 MQTT 接入配置
///
/// 明文回显(含密码), 便于运营与 broker 侧账号直接对照
@Data
@Accessors(chain = true)
@Schema(title = "平台MQTT接入配置")
public class PlatformMqttConfigResult {

    /// 设备端 MQTT 接入地址(含路径的完整 wss 地址)
    @Schema(description = "设备端MQTT接入地址(wss完整地址, 含路径)")
    private String brokerWssUrl;

    /// 接入账号
    @Schema(description = "接入账号")
    private String mqttUsername;

    /// 接入密码(明文回显)
    @Schema(description = "接入密码")
    private String mqttPassword;
}
