package cn.daxpay.open.platform.system.entity.config.platform.infra;

import lombok.Data;
import lombok.experimental.Accessors;

/// # 平台 MQTT 接入配置
///
/// 设备/终端 MQTT over WebSocket 接入参数(纯 DB 存储, 界面明文回显, 不再读 yml);
/// 设备激活时随凭证下发给终端, 修改后仅新激活/重新激活的设备取到新值。
/// 接入账号在 broker 侧仅授 consume 权限(设备不能发布, 防伪造下发), 内容层另有设备密钥加密
@Data
@Accessors(chain = true)
public class PlatformMqttConfig {

    /// MQTT 接入地址(设备端 wss 完整地址, 含路径, 如 wss://mqtt.example.com/mqtt)
    private String brokerWssUrl;

    /// 接入账号(broker 静态设备账号, 对设备主题仅 consume 权限)
    private String mqttUsername = "app-device";

    /// 接入密码(须与 broker 侧账号一致; 明文存储, 界面明文回显)
    private String mqttPassword;
}
