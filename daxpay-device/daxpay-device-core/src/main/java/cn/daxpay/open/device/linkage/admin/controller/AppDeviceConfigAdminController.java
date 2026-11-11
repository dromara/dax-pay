package cn.daxpay.open.device.linkage.admin.controller;

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

/// # 应用设备联动配置(运营端)
///
/// 入口为应用工作台的「收款播报」磁贴, 独立配置页(与商户应用编辑弹窗分离, 独立保存):
/// 播报/打印两个布尔 + 门店严格匹配 + 版式下拉。
/// 权限跟应用管理入口走: 挂商户应用菜单码(merchant:app,
/// 与 [MchAppInfoAdminController] 同权限面)。
@PermCode(menuCode = PermCodes.Merchant.App.MENU)
@Validated
@Tag(name = "应用设备联动配置(运营端)")
@RestController
@RequestMapping("/admin/merchant/app/device-config")
@RequiredArgsConstructor
public class AppDeviceConfigAdminController {

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
