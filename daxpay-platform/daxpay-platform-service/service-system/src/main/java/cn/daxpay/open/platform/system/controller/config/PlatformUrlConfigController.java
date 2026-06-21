package cn.daxpay.open.platform.system.controller.config;

import cn.daxpay.open.platform.core.annotation.PermCode;
import cn.daxpay.open.platform.core.rest.Res;
import cn.daxpay.open.platform.core.rest.result.Result;
import cn.daxpay.open.platform.system.param.config.PlatformUrlConfigParam;
import cn.daxpay.open.platform.system.result.config.platform.PlatformUrlConfigResult;
import cn.daxpay.open.platform.system.service.config.PlatformUrlConfigService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/// # 平台端点配置
///
/// 管理系统访问地址等端点配置
@PermCode(menuCode = "system:platform:config")
@Validated
@Tag(name = "平台端点配置")
@RestController
@RequestMapping("/platform/config/url")
@RequiredArgsConstructor
public class PlatformUrlConfigController {
    private final PlatformUrlConfigService platformUrlConfigService;

    @PermCode(code = "platformConfig:view", nameCn = "平台配置查看", nameEn = "Platform Config View")
    @Operation(summary = "获取端点配置")
    @GetMapping("/get")
    public Result<PlatformUrlConfigResult> getUrlConfig() {
        return Res.ok(platformUrlConfigService.findUrlConfig());
    }

    @PermCode(code = "platformConfig:manage", nameCn = "平台配置管理", nameEn = "Platform Config Manage")
    @Operation(summary = "更新端点配置")
    @PostMapping("/update")
    public Result<Void> updateUrlConfig(@RequestBody @Validated PlatformUrlConfigParam param) {
        platformUrlConfigService.updateUrlConfig(param);
        return Res.ok();
    }
}
