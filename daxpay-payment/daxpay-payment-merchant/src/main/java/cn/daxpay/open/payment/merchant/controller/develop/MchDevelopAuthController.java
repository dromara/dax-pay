package cn.daxpay.open.payment.merchant.controller.develop;

import cn.daxpay.open.payment.auth.develop.DevelopAuthService;
import cn.daxpay.open.payment.auth.develop.DevelopChannelAuthParam;
import cn.daxpay.open.payment.auth.develop.MchDevelopChannelAuthParam;
import cn.daxpay.open.payment.common.context.PaymentContext;
import cn.daxpay.open.payment.unipay.result.assist.AuthResult;
import cn.daxpay.open.payment.unipay.result.assist.AuthUrlResult;
import cn.daxpay.open.platform.core.annotation.PermCode;
import cn.daxpay.open.platform.core.code.CommonCode;
import cn.daxpay.open.platform.core.code.PermCodes;
import cn.daxpay.open.platform.core.exception.BizInfoException;
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

import java.util.Objects;

/// 认证调试(商户端)
///
/// 调试入口(均为 OAuth 链接 + queryCode 轮询):
/// - 支付宝H5平台级配置
/// - 微信公众号配置(平台级 OAuth)
/// - 抖音 H5 应用配置(平台级 silent_auth)
/// - 微信支付(直连/服务商, 需商户参数)
/// - 抖音支付(直连/服务商, 需商户参数)
///
/// 小程序获取 openId 已迁至收银台小程序运行时, Web 不再提供小程序调试入口。
@Validated
@PermCode(menuCode = PermCodes.Develop.Auth.MENU)
@Tag(name = "认证调试服务(商户端)")
@RestController
@RequestMapping("/mch/develop/auth")
@RequiredArgsConstructor
public class MchDevelopAuthController {

    private final DevelopAuthService developAuthService;
    private final PaymentContext paymentContext;

    /// 当前登录商户号（上下文必有；缺则视为会话异常）
    private String requireMchNo() {
        String mchNo = paymentContext.getMchNo();
        if (Objects.isNull(mchNo) || mchNo.isBlank()) {
            // 商户上下文缺失
            throw new BizInfoException(CommonCode.FAIL_CODE, "pay.error.assist.mchContextMissing");
        }
        return mchNo;
    }

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
    public Result<AuthUrlResult> generateChannelAuthUrl(@Validated @RequestBody MchDevelopChannelAuthParam param) {
        // 商户号强制取自登录上下文(防越权), 组装为完整参数
        DevelopChannelAuthParam saveParam = new DevelopChannelAuthParam()
                .setMchNo(requireMchNo())
                .setScope(param.getScope())
                .setAppId(param.getAppId());
        return Res.ok(developAuthService.generateChannelAuthUrl(saveParam));
    }

    @PermCode(code = PermCodes.Action.VIEW)
    @Operation(summary = "生成抖音支付授权链接")
    @PostMapping("/generate-douyin-channel-auth-url")
    public Result<AuthUrlResult> generateDouyinChannelAuthUrl(@Validated @RequestBody MchDevelopChannelAuthParam param) {
        // 商户号强制取自登录上下文(防越权), 组装为完整参数
        DevelopChannelAuthParam saveParam = new DevelopChannelAuthParam()
                .setMchNo(requireMchNo())
                .setScope(param.getScope())
                .setAppId(param.getAppId());
        return Res.ok(developAuthService.generateDouyinChannelAuthUrl(saveParam));
    }

    @PermCode(code = PermCodes.Action.VIEW)
    @Operation(summary = "通过查询码获取认证结果")
    @GetMapping("/query-auth-result")
    public Result<AuthResult> queryAuthResult(
            @NotBlank(message = "{validation.field.queryCode.notBlank}") String queryCode) {
        return Res.ok(developAuthService.queryAuthResult(queryCode));
    }
}
