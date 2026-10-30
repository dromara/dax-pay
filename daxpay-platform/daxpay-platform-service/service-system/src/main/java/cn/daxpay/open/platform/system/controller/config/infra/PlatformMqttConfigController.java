package cn.daxpay.open.platform.system.controller.config.infra;

import cn.daxpay.open.platform.core.annotation.PermCode;
import cn.daxpay.open.platform.core.code.PermCodes;
import cn.daxpay.open.platform.core.rest.Res;
import cn.daxpay.open.platform.core.rest.result.Result;
import cn.daxpay.open.platform.system.param.config.infra.PlatformMqttConfigParam;
import cn.daxpay.open.platform.system.result.config.infra.PlatformMqttConfigResult;
import cn.daxpay.open.platform.system.service.config.infra.PlatformMqttConfigService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/// # 平台 MQTT 接入配置
///
/// 管理设备/终端 MQTT 接入地址与账号(虚拟设备激活时随凭证下发), 挂平台配置菜单
/// (配置类型 `device_mqtt`, 参照平台端点配置同模式)。
@PermCode(menuCode = PermCodes.System.PlatformConfig.MENU)
@Validated
@Tag(name = "平台MQTT接入配置")
@RestController
@RequestMapping("/platform/config/mqtt")
@RequiredArgsConstructor
public class PlatformMqttConfigController {
    private final PlatformMqttConfigService platformMqttConfigService;

    @PermCode(code = PermCodes.Action.VIEW)
    @Operation(summary = "获取MQTT接入配置")
    @GetMapping("/get")
    public Result<PlatformMqttConfigResult> getMqttConfig() {
        return Res.ok(platformMqttConfigService.findMqttConfig());
    }

    @PermCode(code = PermCodes.Action.MANAGE)
    @Operation(summary = "更新MQTT接入配置")
    @PostMapping("/update")
    public Result<Void> updateMqttConfig(@RequestBody @Validated PlatformMqttConfigParam param) {
        platformMqttConfigService.updateMqttConfig(param);
        return Res.ok();
    }
}
