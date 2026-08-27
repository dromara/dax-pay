package cn.daxpay.open.platform.system.controller.config.infra;

import cn.daxpay.open.platform.core.annotation.PermCode;
import cn.daxpay.open.platform.core.code.PermCodes;
import cn.daxpay.open.platform.core.rest.Res;
import cn.daxpay.open.platform.core.rest.result.Result;
import cn.daxpay.open.platform.system.param.config.infra.PlatformMailConfigParam;
import cn.daxpay.open.platform.system.result.config.infra.PlatformMailConfigResult;
import cn.daxpay.open.platform.system.service.config.infra.PlatformMailConfigService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/// # 平台邮件发件箱配置
///
/// 管理 SMTP 发件服务器配置, 挂平台配置菜单
@PermCode(menuCode = PermCodes.System.PlatformConfig.MENU)
@Validated
@Tag(name = "平台邮件发件箱配置")
@RestController
@RequestMapping("/platform/config/mail")
@RequiredArgsConstructor
public class PlatformMailConfigController {
    private final PlatformMailConfigService platformMailConfigService;

    @PermCode(code = PermCodes.Action.VIEW)
    @Operation(summary = "获取邮件发件箱配置")
    @GetMapping("/get")
    public Result<PlatformMailConfigResult> getMailConfig() {
        return Res.ok(platformMailConfigService.findMailConfig());
    }

    @PermCode(code = PermCodes.Action.MANAGE)
    @Operation(summary = "更新邮件发件箱配置")
    @PostMapping("/update")
    public Result<Void> updateMailConfig(@RequestBody @Validated PlatformMailConfigParam param) {
        platformMailConfigService.updateMailConfig(param);
        return Res.ok();
    }
}
