package cn.daxpay.open.device.linkage.admin.controller;

import cn.daxpay.open.device.linkage.param.AppSimpleConfigParam;
import cn.daxpay.open.device.linkage.result.AppSimpleConfigResult;
import cn.daxpay.open.device.linkage.service.AppDeviceConfigService;
import cn.daxpay.open.platform.core.annotation.PermCode;
import cn.daxpay.open.platform.core.code.PermCodes;
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
/// 入口在「商户应用管理」编辑弹窗的开关区块: 播报/打印两个布尔 + 版式下拉。
/// 开关 UI 在应用管理入口内, 权限跟入口走: 挂商户应用菜单码(merchant:app,
/// 与 [MchAppInfoAdminController] 同权限面; 对齐 plus 原实现挂 Merchant.App 系列的做法)。
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
    @PostMapping("/simple-save")
    public Result<Void> saveSimple(@RequestBody @Validated AppSimpleConfigParam param) {
        appDeviceConfigService.saveSimpleConfig(param);
        return Res.ok();
    }
}
