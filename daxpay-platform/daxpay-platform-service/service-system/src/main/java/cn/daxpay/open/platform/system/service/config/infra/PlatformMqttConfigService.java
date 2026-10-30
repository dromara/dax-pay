package cn.daxpay.open.platform.system.service.config.infra;

import cn.daxpay.open.platform.system.convert.config.infra.PlatformMqttConfigConvert;
import cn.daxpay.open.platform.system.entity.config.platform.infra.PlatformMqttConfig;
import cn.daxpay.open.platform.system.enums.PlatformConfigTypeEnum;
import cn.daxpay.open.platform.system.param.config.infra.PlatformMqttConfigParam;
import cn.daxpay.open.platform.system.result.config.infra.PlatformMqttConfigResult;
import cn.daxpay.open.platform.system.service.config.SystemPlatformConfigService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/// # 平台 MQTT 接入配置服务
///
/// 管理设备/终端 MQTT 接入地址与账号(纯 DB 存储、明文回显, 不读 yml);
/// 设备(虚拟设备收款播报 APP)激活时经 [#getMqttConfig] 取当前配置随凭证下发,
/// 修改即时生效(仅影响新激活设备)。
/// 数据源正典: 配置类型 [PlatformConfigTypeEnum#DEVICE_MQTT](`device_mqtt`)。
@Slf4j
@Service
@RequiredArgsConstructor
public class PlatformMqttConfigService {

    private final SystemPlatformConfigService systemConfigService;

    /// 获取 MQTT 接入配置(不存在时创建默认配置)
    public PlatformMqttConfig getMqttConfig() {
        return systemConfigService.getOrCreateConfig(PlatformConfigTypeEnum.DEVICE_MQTT,
                PlatformMqttConfig.class,
                new PlatformMqttConfig());
    }

    /// 获取 MQTT 接入配置
    public PlatformMqttConfigResult findMqttConfig() {
        return PlatformMqttConfigConvert.CONVERT.toMqttResult(this.getMqttConfig());
    }

    /// 更新 MQTT 接入配置
    public void updateMqttConfig(PlatformMqttConfigParam param) {
        PlatformMqttConfig data = this.getMqttConfig();
        PlatformMqttConfigConvert.CONVERT.copy(param, data);
        systemConfigService.updateConfig(PlatformConfigTypeEnum.DEVICE_MQTT, data);
    }
}
