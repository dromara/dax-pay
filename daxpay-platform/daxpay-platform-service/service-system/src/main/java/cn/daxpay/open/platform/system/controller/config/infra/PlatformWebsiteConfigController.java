package cn.daxpay.open.platform.system.controller.config.infra;

import cn.daxpay.open.platform.core.annotation.IgnoreAuth;
import cn.daxpay.open.platform.core.annotation.PermCode;
import cn.daxpay.open.platform.core.code.PermCodes;
import cn.daxpay.open.platform.core.rest.Res;
import cn.daxpay.open.platform.core.rest.result.Result;
import cn.daxpay.open.platform.system.param.config.infra.PlatformWebsiteConfigParam;
import cn.daxpay.open.platform.system.result.config.infra.PlatformWebsiteConfigResult;
import cn.daxpay.open.platform.system.service.config.infra.PlatformWebsiteConfigService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/// # 平台站点配置
///
/// 管理系统名称、Logo、备案与版权等展示配置
@PermCode(menuCode = PermCodes.System.PlatformConfig.MENU)
@Validated
@Tag(name = "平台站点配置")
@RestController
@RequestMapping("/platform/config/website")
@RequiredArgsConstructor
public class PlatformWebsiteConfigController {

    private final PlatformWebsiteConfigService platformWebsiteConfigService;

    /// 获取站点配置(免登录, 供登录页品牌展示)
    @IgnoreAuth
    @Operation(summary = "获取站点配置")
    @GetMapping("/get")
    public Result<PlatformWebsiteConfigResult> getWebsiteConfig() {
        return Res.ok(platformWebsiteConfigService.findWebsiteConfig());
    }

    @PermCode(code = PermCodes.Action.MANAGE)
    @Operation(summary = "更新站点配置")
    @PostMapping("/update")
    public Result<Void> updateWebsiteConfig(@RequestBody @Validated PlatformWebsiteConfigParam param) {
        platformWebsiteConfigService.updateWebsiteConfig(param);
        return Res.ok();
    }
}
