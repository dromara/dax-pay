package cn.daxpay.open.platform.system.controller.config.infra;

import cn.daxpay.open.platform.core.annotation.PermCode;
import cn.daxpay.open.platform.core.code.PermCodes;
import cn.daxpay.open.platform.core.rest.Res;
import cn.daxpay.open.platform.core.rest.result.Result;
import cn.daxpay.open.platform.system.param.config.infra.PlatformSensitiveWordConfigParam;
import cn.daxpay.open.platform.system.result.config.infra.PlatformSensitiveWordConfigResult;
import cn.daxpay.open.platform.system.service.config.infra.PlatformSensitiveWordConfigService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/// # 平台敏感词策略配置
///
@PermCode(menuCode = PermCodes.System.PlatformConfig.MENU)
@Validated
@Tag(name = "平台敏感词策略")
@RestController
@RequestMapping("/platform/config/sensitive-word")
@RequiredArgsConstructor
public class PlatformSensitiveWordConfigController {

    private final PlatformSensitiveWordConfigService platformSensitiveWordConfigService;

    @PermCode(code = PermCodes.Action.VIEW)
    @Operation(summary = "获取敏感词策略")
    @GetMapping("/get")
    public Result<PlatformSensitiveWordConfigResult> get() {
        return Res.ok(platformSensitiveWordConfigService.findConfig());
    }

    @PermCode(code = PermCodes.Action.MANAGE)
    @Operation(summary = "更新敏感词策略")
    @PostMapping("/update")
    public Result<Void> update(@RequestBody @Validated PlatformSensitiveWordConfigParam param) {
        platformSensitiveWordConfigService.updateConfig(param);
        return Res.ok();
    }
}
