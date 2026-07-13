package cn.daxpay.open.platform.system.controller.config.auth;

import cn.daxpay.open.platform.core.annotation.PermCode;
import cn.daxpay.open.platform.core.code.PermCodes;
import cn.daxpay.open.platform.core.rest.Res;
import cn.daxpay.open.platform.core.rest.result.Result;
import cn.daxpay.open.platform.system.param.config.auth.PlatformAlipayAuthConfigParam;
import cn.daxpay.open.platform.system.result.config.auth.PlatformAlipayAuthConfigResult;
import cn.daxpay.open.platform.system.service.config.auth.PlatformAlipayAuthConfigService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/// # 平台支付宝开放平台认证配置
///
/// 管理支付宝 OAuth 凭据(appId/私钥/证书), 挂载在「三方平台管理」菜单下(与登录平台配置共享菜单权限)。
/// 凭据同时服务于: 三方登录的支付宝授权登录(iam 模块)、支付场景的通道认证(payment 模块)。
///
@PermCode(menuCode = PermCodes.Iam.Social.MENU)
@Validated
@Tag(name = "平台支付宝开放平台认证配置")
@RestController
@RequestMapping("/platform/config/alipay-auth")
@RequiredArgsConstructor
public class PlatformAlipayAuthConfigController {

    private final PlatformAlipayAuthConfigService platformAlipayAuthConfigService;

    @PermCode(code = PermCodes.Action.VIEW, nameCn = PermCodes.Iam.Social.VIEW_NAME_CN, nameEn = PermCodes.Iam.Social.VIEW_NAME_EN)
    @Operation(summary = "获取支付宝认证配置")
    @GetMapping("/get")
    public Result<PlatformAlipayAuthConfigResult> getAlipayAuthConfig() {
        return Res.ok(platformAlipayAuthConfigService.findAlipayAuthConfig());
    }

    @PermCode(code = PermCodes.Action.MANAGE, nameCn = PermCodes.Iam.Social.MANAGE_NAME_CN, nameEn = PermCodes.Iam.Social.MANAGE_NAME_EN)
    @Operation(summary = "更新支付宝认证配置")
    @PostMapping("/update")
    public Result<Void> updateAlipayAuthConfig(@RequestBody @Validated PlatformAlipayAuthConfigParam param) {
        platformAlipayAuthConfigService.updateAlipayAuthConfig(param);
        return Res.ok();
    }
}
