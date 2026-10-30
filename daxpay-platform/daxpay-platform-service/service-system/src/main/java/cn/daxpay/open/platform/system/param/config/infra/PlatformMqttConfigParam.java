package cn.daxpay.open.platform.system.param.config.infra;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;
import lombok.experimental.Accessors;

/// # 平台 MQTT 接入配置参数
@Data
@Accessors(chain = true)
@Schema(title = "平台MQTT接入配置参数")
public class PlatformMqttConfigParam {

    /// 设备端 MQTT 接入地址(含路径的完整 wss 地址)
    @NotBlank(message = "{validation.field.brokerWssUrl.notBlank}")
    @Pattern(regexp = "^wss?://\\S+$", message = "{validation.field.brokerWssUrl.pattern}")
    @Schema(description = "设备端MQTT接入地址(wss完整地址, 含路径)")
    private String brokerWssUrl;

    /// 接入账号
    @NotBlank(message = "{validation.field.mqttUsername.notBlank}")
    @Schema(description = "接入账号")
    private String mqttUsername;

    /// 接入密码
    @NotBlank(message = "{validation.field.mqttPassword.notBlank}")
    @Schema(description = "接入密码")
    private String mqttPassword;
}
