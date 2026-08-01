package cn.daxpay.open.platform.iam.controller.social;

import cn.daxpay.open.platform.iam.param.social.SocialLoginConfigParam;
import cn.daxpay.open.platform.iam.result.social.SocialCallbackUrlResult;
import cn.daxpay.open.platform.iam.result.social.SocialLoginConfigResult;
import cn.daxpay.open.platform.iam.service.social.SocialAutoLoginConfigService;
import cn.daxpay.open.platform.iam.service.social.SocialLoginConfigService;
import cn.daxpay.open.platform.iam.service.social.SocialLoginService;
import cn.daxpay.open.platform.core.annotation.PermCode;
import cn.daxpay.open.platform.core.code.PermCodes;
import cn.daxpay.open.platform.core.rest.Res;
import cn.daxpay.open.platform.core.rest.result.Result;
import cn.daxpay.open.platform.system.param.config.security.PlatformSocialAutoLoginConfigParam;
import cn.daxpay.open.platform.system.result.config.security.PlatformSocialAutoLoginConfigResult;
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

import java.util.List;

/// # 第三方平台登录配置管理
///
@PermCode(menuCode = PermCodes.Iam.Social.MENU)
@Validated
@Tag(name = "社交登录配置管理")
@RestController
@RequestMapping("/social/login-config")
@RequiredArgsConstructor
public class SocialLoginConfigController {

    private final SocialLoginConfigService socialLoginConfigService;

    private final SocialLoginService socialLoginService;

    private final SocialAutoLoginConfigService socialAutoLoginConfigService;

    @PermCode(code = PermCodes.Action.VIEW)
    @Operation(summary = "全量查询平台配置(枚举驱动, 读时初始化缺失平台)")
    @GetMapping("/find-all")
    public Result<List<SocialLoginConfigResult>> findAll() {
        return Res.ok(socialLoginConfigService.findAll());
    }

    /// 列出应在第三方平台登记的 OAuth 回调地址(运营/商户 × 平台 × 登录/绑定)
    @PermCode(code = PermCodes.Action.VIEW)
    @Operation(summary = "查询应登记的社交回调地址清单")
    @GetMapping("/callback-urls")
    public Result<List<SocialCallbackUrlResult>> callbackUrls() {
        return Res.ok(socialLoginService.listCallbackUrls());
    }

    @PermCode(code = PermCodes.Action.VIEW)
    @Operation(summary = "根据平台编码查询(不存在则初始化占位记录)")
    @GetMapping("/get-by-source")
    public Result<SocialLoginConfigResult> findBySource(@NotBlank(message = "{validation.field.source.notBlank}") String source) {
        return Res.ok(socialLoginConfigService.findBySource(source));
    }

    @PermCode(code = PermCodes.Action.MANAGE)
    @Operation(summary = "修改平台配置")
    @PostMapping("/update")
    public Result<Void> update(@RequestBody @Validated SocialLoginConfigParam param) {
        socialLoginConfigService.update(param);
        return Res.ok();
    }

    @PermCode(code = PermCodes.Action.MANAGE)
    @Operation(summary = "切换平台启用状态(仅已配置平台可启停)")
    @PostMapping("/update-enabled")
    public Result<Void> updateEnabled(
        @NotBlank(message = "{validation.field.source.notBlank}") String source,
        Boolean enabled) {
        socialLoginConfigService.updateEnabled(source, enabled);
        return Res.ok();
    }

    /// 应用内自动登录策略(按 admin/merchant 分端)
    @PermCode(code = PermCodes.Action.VIEW)
    @Operation(summary = "获取应用内社交自动登录配置")
    @GetMapping("/auto-login/get")
    public Result<PlatformSocialAutoLoginConfigResult> getAutoLoginConfig() {
        return Res.ok(socialAutoLoginConfigService.findConfig());
    }

    @PermCode(code = PermCodes.Action.MANAGE)
    @Operation(summary = "更新应用内社交自动登录配置")
    @PostMapping("/auto-login/update")
    public Result<Void> updateAutoLoginConfig(@RequestBody @Validated PlatformSocialAutoLoginConfigParam param) {
        socialAutoLoginConfigService.updateConfig(param);
        return Res.ok();
    }
}
