package cn.daxpay.open.payment.unipay.trade.controller;

import cn.daxpay.open.platform.core.annotation.IgnoreAuth;
import cn.daxpay.open.platform.core.rest.Res;
import cn.daxpay.open.platform.core.rest.result.Result;
import cn.daxpay.open.payment.auth.ChannelAuthFacade;
import cn.daxpay.open.payment.unipay.param.assist.AuthCodeParam;
import cn.daxpay.open.payment.unipay.param.assist.GenerateAuthUrlParam;
import cn.daxpay.open.payment.common.result.DaxResult;
import cn.daxpay.open.payment.unipay.result.assist.AuthResult;
import cn.daxpay.open.payment.unipay.result.assist.AuthUrlResult;
import cn.daxpay.open.payment.common.util.DaxRes;
import cn.daxpay.open.payment.unipay.aop.PaymentVerify;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/// # 通道认证服务
///
/// 统一支付网关对外的通道 OAuth 入口(取 openId/userId, **非登录**)。
/// 业务分发委托 [ChannelAuthFacade]:
/// - **生成授权链接**: authType=ALIPAY 走平台级支付宝 OAuth; 其余按支付产品走通道策略
/// - **授权码回调**: 按 session.source 走平台分支, 否则走通道策略
@IgnoreAuth
@Tag(name = "通道认证服务")
@RestController
@RequestMapping("/unipay/assist/channel/auth")
@RequiredArgsConstructor
public class ChannelAuthController {

    private final ChannelAuthFacade channelAuthFacade;

    @PaymentVerify
    @Operation(summary = "获取授权链接")
    @PostMapping("/generate-auth-url")
    public DaxResult<AuthUrlResult> generateAuthUrl(@RequestBody GenerateAuthUrlParam param) {
        return DaxRes.ok(channelAuthFacade.generateAuthUrl(param));
    }

    @Operation(summary = "通过AuthCode获取认证结果")
    @PostMapping("/auth")
    public Result<AuthResult> auth(@RequestBody AuthCodeParam param) {
        return Res.ok(channelAuthFacade.auth(param));
    }

    @Operation(summary = "通过AuthCode获取并设置认证结果")
    @PostMapping("/auth-and-set")
    public Result<Void> authAndSet(@RequestBody AuthCodeParam param) {
        channelAuthFacade.auth(param);
        return Res.ok();
    }
}
