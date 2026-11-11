package cn.daxpay.open.device.app.admin.controller;

import cn.daxpay.open.device.linkage.param.AppSimpleConfigParam;
import cn.daxpay.open.device.linkage.result.AppSimpleConfigResult;
import cn.daxpay.open.device.linkage.service.AppDeviceConfigService;
import cn.daxpay.open.platform.core.annotation.OperateLog;
import cn.daxpay.open.platform.core.annotation.PermCode;
import cn.daxpay.open.platform.core.code.PermCodes;
import cn.daxpay.open.platform.core.enums.common.OperateLogType;
import cn.daxpay.open.platform.core.rest.Res;
import cn.daxpay.open.platform.core.rest.result.Result;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/// # 应用设备联动配置(运营小程序端镜像)
///
/// 对应 Web 运营端 [AppDeviceConfigAdminController], 镜像应用工作台「收款播报」
/// 独立配置页的读写两端点, 复用同一 [AppDeviceConfigService] 与权限码,
/// 仅 URL 前缀不同(/app-admin)。
@PermCode(menuCode = PermCodes.Merchant.App.MENU)
@Validated
@Tag(name = "小程序管理端-应用设备联动配置")
@RestController
@RequestMapping("/app-admin/merchant/app/device-config")
@RequiredArgsConstructor
public class AppAdminDeviceConfigController {

    private final AppDeviceConfigService appDeviceConfigService;

    @PermCode(code = PermCodes.Action.VIEW)
    @Operation(summary = "应用设备联动配置(行不存在=默认全开派生)")
    @GetMapping("/simple-get")
    public Result<AppSimpleConfigResult> getSimpleConfig(@NotBlank(message = "{validation.field.appId.notBlank}") String appId) {
        return Res.ok(appDeviceConfigService.getSimpleConfig(appId));
    }

    @PermCode(code = PermCodes.Action.MANAGE)
    @Operation(summary = "保存应用设备联动配置(播报/打印开关 + 版式代码)")
    @OperateLog(title = "保存应用收款播报配置", businessType = OperateLogType.UPDATE, saveParam = true)
    @PostMapping("/simple-save")
    public Result<Void> saveSimple(@RequestBody @Validated AppSimpleConfigParam param) {
        appDeviceConfigService.saveSimpleConfig(param);
        return Res.ok();
    }
}
