package cn.daxpay.open.platform.system.controller.config;

import cn.daxpay.open.platform.core.annotation.PermCode;
import cn.daxpay.open.platform.core.rest.Res;
import cn.daxpay.open.platform.core.rest.result.Result;
import cn.daxpay.open.platform.system.param.config.PlatformOssConfigParam;
import cn.daxpay.open.platform.system.result.config.platform.PlatformOssConfigResult;
import cn.daxpay.open.platform.system.service.config.PlatformOssConfigService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/// # 平台OSS配置
///
/// 管理对象存储配置
@PermCode(menuCode = "system:platform:config")
@Validated
@Tag(name = "平台OSS配置")
@RestController
@RequestMapping("/platform/config/oss")
@RequiredArgsConstructor
public class PlatformOssConfigController {
    private final PlatformOssConfigService platformOssConfigService;

    @PermCode(code = "platformConfig:view", nameCn = "平台配置查看", nameEn = "Platform Config View")
    @Operation(summary = "获取OSS配置")
    @GetMapping("/get")
    public Result<PlatformOssConfigResult> getOssConfig() {
        return Res.ok(platformOssConfigService.findOssConfig());
    }

    @PermCode(code = "platformConfig:manage", nameCn = "平台配置管理", nameEn = "Platform Config Manage")
    @Operation(summary = "更新OSS配置")
    @PostMapping("/update")
    public Result<Void> updateOssConfig(@RequestBody @Validated PlatformOssConfigParam param) {
        platformOssConfigService.updateOssConfig(param);
        return Res.ok();
    }
}
