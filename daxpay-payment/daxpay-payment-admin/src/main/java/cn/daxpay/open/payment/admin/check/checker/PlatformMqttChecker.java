package cn.daxpay.open.payment.admin.check.checker;

import cn.daxpay.open.payment.common.check.checker.AdminConfigChecker;
import cn.daxpay.open.payment.common.check.enums.ConfigCheckCategoryEnum;
import cn.daxpay.open.payment.common.check.model.ConfigCheckItem;
import cn.daxpay.open.platform.system.entity.config.platform.infra.PlatformMqttConfig;
import cn.daxpay.open.platform.system.service.config.infra.PlatformMqttConfigService;
import cn.hutool.core.util.StrUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.Map;

/// # 平台 MQTT 接入配置检查器
///
/// 检测 MQTT 接入地址与密码是否已配置。配置记录恒存在(缺省创建),
/// 只存在字段为空态, 判空必须查字段。MQTT 未配置时设备无法接收播报。
@Component
@Order(6)
@RequiredArgsConstructor
public class PlatformMqttChecker implements AdminConfigChecker {

    private final PlatformMqttConfigService platformMqttConfigService;

    @Override
    public ConfigCheckCategoryEnum getCategory() {
        return ConfigCheckCategoryEnum.PLATFORM_MQTT;
    }

    @Override
    public ConfigCheckItem check() {
        // 配置记录恒存在, 判断关键字段是否为空 => 告警
        PlatformMqttConfig config = platformMqttConfigService.getMqttConfig();
        boolean unconfigured = StrUtil.isBlank(config.getBrokerWssUrl())
                || StrUtil.isBlank(config.getMqttPassword());
        if (unconfigured) {
            return ConfigCheckItem.of(
                    ConfigCheckCategoryEnum.PLATFORM_MQTT,
                    ConfigCheckCategoryEnum.PLATFORM_MQTT.getCode(),
                    "configCheck.platformMqtt.title",
                    "configCheck.platformMqtt.description",
                    "/system/config/platform",
                    Map.of("tab", "mqtt")
            );
        }
        return null;
    }
}
