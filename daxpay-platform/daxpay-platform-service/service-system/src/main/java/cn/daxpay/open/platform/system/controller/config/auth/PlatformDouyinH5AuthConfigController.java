package cn.daxpay.open.platform.system.controller.config.auth;


import cn.daxpay.open.platform.core.code.PermCodes;
import cn.daxpay.open.platform.core.annotation.PermCode;
import cn.daxpay.open.platform.core.rest.Res;
import cn.daxpay.open.platform.core.rest.result.Result;
import cn.daxpay.open.platform.system.param.config.auth.PlatformDouyinH5AuthConfigParam;
import cn.daxpay.open.platform.system.result.config.auth.PlatformDouyinH5AuthConfigResult;
import cn.daxpay.open.platform.system.service.config.auth.PlatformDouyinH5AuthConfigService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/// # 平台抖音开放平台 H5 应用认证配置
///
/// 管理抖音开放平台 H5 应用凭据(clientKey/clientSecret), 挂载在「三方平台管理」菜单下(与登录平台配置共享菜单权限)。
/// 本配置独立于「三方平台登录配置」中的抖音 OAuth 登录凭据。
///
@PermCode(menuCode = PermCodes.Iam.Social.MENU)
@Validated
@Tag(name = "平台抖音开放平台 H5 应用认证配置")
@RestController
@RequestMapping("/platform/config/douyin-h5-auth")
@RequiredArgsConstructor
public class PlatformDouyinH5AuthConfigController {

    private final PlatformDouyinH5AuthConfigService platformDouyinH5AuthConfigService;

    @PermCode(code = PermCodes.Action.VIEW, nameCn = "社交登录配置查看", nameEn = "Social Login Config View")
    @Operation(summary = "获取抖音 H5 应用认证配置")
    @GetMapping("/get")
    public Result<PlatformDouyinH5AuthConfigResult> getDouyinH5AuthConfig() {
        return Res.ok(platformDouyinH5AuthConfigService.findDouyinH5AuthConfig());
    }

    @PermCode(code = PermCodes.Action.MANAGE, nameCn = "社交登录配置管理", nameEn = "Social Login Config Manage")
    @Operation(summary = "更新抖音 H5 应用认证配置")
    @PostMapping("/update")
    public Result<Void> updateDouyinH5AuthConfig(@RequestBody @Validated PlatformDouyinH5AuthConfigParam param) {
        platformDouyinH5AuthConfigService.updateDouyinH5AuthConfig(param);
        return Res.ok();
    }
}
