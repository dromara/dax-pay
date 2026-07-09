package cn.daxpay.open.platform.iam.controller.social;

import cn.daxpay.open.platform.iam.param.social.SocialLoginConfigParam;
import cn.daxpay.open.platform.iam.result.social.SocialLoginConfigResult;
import cn.daxpay.open.platform.iam.service.social.SocialLoginConfigService;
import cn.daxpay.open.platform.core.annotation.PermCode;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/// # 第三方平台登录配置管理
///
@PermCode(menuCode = "iam:social:login-config")
@Validated
@Tag(name = "社交登录配置管理")
@RestController
@RequestMapping("/social/login-config")
@RequiredArgsConstructor
public class SocialLoginConfigController {

    private final SocialLoginConfigService socialLoginConfigService;

    @PermCode(code = "view", nameCn = "配置查看", nameEn = "Config View")
    @Operation(summary = "全量查询平台配置(枚举驱动, 读时初始化缺失平台)")
    @GetMapping("/find-all")
    public Result<List<SocialLoginConfigResult>> findAll() {
        return Res.ok(socialLoginConfigService.findAll());
    }

    @PermCode(code = "view", nameCn = "配置查看", nameEn = "Config View")
    @Operation(summary = "根据平台编码查询(不存在则初始化占位记录)")
    @GetMapping("/get-by-source")
    public Result<SocialLoginConfigResult> findBySource(@NotBlank(message = "{validation.field.source.notBlank}") String source) {
        return Res.ok(socialLoginConfigService.findBySource(source));
    }

    @PermCode(code = "manage", nameCn = "配置管理", nameEn = "Config Manage")
    @Operation(summary = "修改平台配置")
    @PostMapping("/update")
    public Result<Void> update(@RequestBody @Validated SocialLoginConfigParam param) {
        socialLoginConfigService.update(param);
        return Res.ok();
    }

    @PermCode(code = "manage", nameCn = "配置管理", nameEn = "Config Manage")
    @Operation(summary = "切换平台启用状态(仅已配置平台可启停)")
    @PostMapping("/update-enabled")
    public Result<Void> updateEnabled(
        @NotBlank(message = "{validation.field.source.notBlank}") String source,
        @RequestParam Boolean enabled) {
        socialLoginConfigService.updateEnabled(source, enabled);
        return Res.ok();
    }
}
