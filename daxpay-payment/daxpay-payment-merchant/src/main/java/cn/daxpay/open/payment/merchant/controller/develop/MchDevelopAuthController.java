package cn.daxpay.open.payment.merchant.controller.develop;

import cn.daxpay.open.payment.auth.develop.DevelopAuthService;
import cn.daxpay.open.payment.unipay.param.assist.GenerateAuthUrlParam;
import cn.daxpay.open.payment.unipay.result.assist.AuthResult;
import cn.daxpay.open.payment.unipay.result.assist.AuthUrlResult;
import cn.daxpay.open.platform.core.annotation.PermCode;
import cn.daxpay.open.platform.core.code.PermCodes;
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
import org.springframework.web.bind.annotation.RestController;

/// 认证调试(商户端)
///
/// 调试入口:
/// - 支付宝H5平台级配置(中间页授权链接)
/// - 微信公众号配置(平台级 OAuth)
/// - 抖音 H5 应用配置(平台级 silent_auth 静默授权)
/// - 微信支付(直连/服务商, 需商户参数)
/// - 支付宝小程序(暂未实现)
/// - 微信小程序(商户端/运营端, 暂未实现)
///
/// 已实现项均通过查询码轮询认证结果。
@Validated
@PermCode(menuCode = PermCodes.Develop.Auth.MENU)
@Tag(name = "认证调试服务(商户端)")
@RestController
@RequestMapping("/mch/develop/auth")
@RequiredArgsConstructor
public class MchDevelopAuthController {

    private final DevelopAuthService developAuthService;

    @PermCode(code = PermCodes.Action.VIEW)
    @Operation(summary = "生成支付宝H5授权链接")
    @PostMapping("/generate-alipay-auth-url")
    public Result<AuthUrlResult> generateAlipayAuthUrl() {
        return Res.ok(developAuthService.generateAlipayAuthUrl());
    }

    @PermCode(code = PermCodes.Action.VIEW)
    @Operation(summary = "生成微信公众号配置授权链接")
    @PostMapping("/generate-wechat-mp-auth-url")
    public Result<AuthUrlResult> generateWechatMpAuthUrl() {
        return Res.ok(developAuthService.generateWechatMpAuthUrl());
    }

    @PermCode(code = PermCodes.Action.VIEW)
    @Operation(summary = "生成抖音H5授权链接")
    @PostMapping("/generate-douyin-auth-url")
    public Result<AuthUrlResult> generateDouyinAuthUrl() {
        return Res.ok(developAuthService.generateDouyinAuthUrl());
    }

    @PermCode(code = PermCodes.Action.VIEW)
    @Operation(summary = "生成微信支付授权链接")
    @PostMapping("/generate-channel-auth-url")
    public Result<AuthUrlResult> generateChannelAuthUrl(@RequestBody GenerateAuthUrlParam param) {
        // 不加 @Valid: GenerateAuthUrlParam 继承 PaymentCommonParam.reqTime(@NotNull), 但认证不走签名/防重放, 无需 reqTime;
        // channel/mchNo 由 ProductAuthService 业务层兜底校验, 与 unipay ChannelAuthController 同类接口保持一致
        return Res.ok(developAuthService.generateChannelAuthUrl(param));
    }

    @PermCode(code = PermCodes.Action.VIEW)
    @Operation(summary = "通过查询码获取认证结果")
    @GetMapping("/query-auth-result")
    public Result<AuthResult> queryAuthResult(
            @NotBlank(message = "{validation.field.queryCode.notBlank}") String queryCode) {
        return Res.ok(developAuthService.queryAuthResult(queryCode));
    }
}
