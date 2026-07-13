package cn.daxpay.open.platform.system.controller.config.auth;

import cn.daxpay.open.platform.core.annotation.PermCode;
import cn.daxpay.open.platform.core.code.PermCodes;
import cn.daxpay.open.platform.core.rest.Res;
import cn.daxpay.open.platform.core.rest.result.Result;
import cn.daxpay.open.platform.system.param.config.auth.PlatformWechatMpAuthConfigParam;
import cn.daxpay.open.platform.system.result.config.auth.PlatformWechatMpAuthConfigResult;
import cn.daxpay.open.platform.system.service.config.auth.PlatformWechatMpAuthConfigService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/// # 平台微信公众号 H5 认证配置
///
/// 管理微信公众号网页授权凭据(appId/appSecret), 挂载在「三方平台管理」菜单下(与登录平台配置共享菜单权限)。
///
@PermCode(menuCode = PermCodes.Iam.Social.MENU)
@Validated
@Tag(name = "平台微信公众号 H5 认证配置")
@RestController
@RequestMapping("/platform/config/wechat-mp-auth")
@RequiredArgsConstructor
public class PlatformWechatMpAuthConfigController {

    private final PlatformWechatMpAuthConfigService platformWechatMpAuthConfigService;

    @PermCode(code = PermCodes.Action.VIEW, nameCn = PermCodes.Iam.Social.VIEW_NAME_CN, nameEn = PermCodes.Iam.Social.VIEW_NAME_EN)
    @Operation(summary = "获取微信公众号认证配置")
    @GetMapping("/get")
    public Result<PlatformWechatMpAuthConfigResult> getWechatMpAuthConfig() {
        return Res.ok(platformWechatMpAuthConfigService.findWechatMpAuthConfig());
    }

    @PermCode(code = PermCodes.Action.MANAGE, nameCn = PermCodes.Iam.Social.MANAGE_NAME_CN, nameEn = PermCodes.Iam.Social.MANAGE_NAME_EN)
    @Operation(summary = "更新微信公众号认证配置")
    @PostMapping("/update")
    public Result<Void> updateWechatMpAuthConfig(@RequestBody @Validated PlatformWechatMpAuthConfigParam param) {
        platformWechatMpAuthConfigService.updateWechatMpAuthConfig(param);
        return Res.ok();
    }
}
