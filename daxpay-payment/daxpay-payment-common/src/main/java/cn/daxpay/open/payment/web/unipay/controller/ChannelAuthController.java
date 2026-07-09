package cn.daxpay.open.payment.web.unipay.controller;

import cn.daxpay.open.platform.core.annotation.IgnoreAuth;
import cn.daxpay.open.platform.core.rest.Res;
import cn.daxpay.open.platform.core.rest.result.Result;
import cn.daxpay.open.payment.core.assist.AuthSession;
import cn.daxpay.open.payment.core.assist.AuthSessionStore;
import cn.daxpay.open.payment.core.assist.ChannelAuthService;
import cn.daxpay.open.payment.core.assist.PlatformAuthService;
import cn.daxpay.open.payment.unipay.param.assist.AuthCodeParam;
import cn.daxpay.open.payment.unipay.param.assist.GenerateAuthUrlParam;
import cn.daxpay.open.payment.common.result.DaxResult;
import cn.daxpay.open.payment.unipay.result.assist.AuthResult;
import cn.daxpay.open.payment.unipay.result.assist.AuthUrlResult;
import cn.daxpay.open.payment.common.util.DaxRes;
import cn.daxpay.open.payment.common.aop.PaymentVerify;
import cn.daxpay.open.platform.core.enums.unipay.ChannelAuthTypeEnum;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Objects;

/// # 通道认证服务
///
/// 认证来源薄分发入口:
/// - **生成授权链接**: authType=ALIPAY 走平台级支付宝中间页([PlatformAuthService]); 其余按支付产品走通道策略([ChannelAuthService])
/// - **换票回调**: 支付宝平台级 / 微信系统公众号(session.source=platform_mp) 走 [PlatformAuthService]; 其余走通道策略
@IgnoreAuth
@Tag(name = "通道认证服务")
@RestController
@RequestMapping("/unipay/assist/channel/auth")
@RequiredArgsConstructor
public class ChannelAuthController {

    private final AuthSessionStore authSessionStore;
    private final PlatformAuthService platformAuthService;
    private final ChannelAuthService channelAuthService;

    @PaymentVerify
    @Operation(summary = "获取授权链接")
    @PostMapping("/generate-auth-url")
    public DaxResult<AuthUrlResult> generateAuthUrl(@RequestBody GenerateAuthUrlParam param) {
        // 支付宝: 平台级 H5 中间页(不依赖商户上下文)
        if (isAlipayAuth(param.getAuthType())) {
            return DaxRes.ok(platformAuthService.generateAlipayAuthUrl());
        }
        return DaxRes.ok(channelAuthService.generateAuthUrl(param));
    }

    @Operation(summary = "通过AuthCode获取认证结果")
    @PostMapping("/auth")
    public Result<AuthResult> auth(@RequestBody AuthCodeParam param) {
        return Res.ok(doAuth(param));
    }

    @Operation(summary = "通过AuthCode获取并设置认证结果")
    @PostMapping("/auth-and-set")
    public Result<Void> authAndSet(@RequestBody AuthCodeParam param) {
        doAuth(param);
        return Res.ok();
    }

    /// 认证换票薄分发
    ///
    /// 优先级: 支付宝平台级(authType=ALIPAY) > 微信系统公众号(session.source=platform_mp) > 通道策略。
    /// 微信等 OAuth 重定向通道回调仅含 authToken, 故先从会话恢复上下文再判断来源。
    private AuthResult doAuth(AuthCodeParam param) {
        if (isAlipayAuth(param.getAuthType())) {
            return platformAuthService.authAlipay(param);
        }
        AuthSession session = authSessionStore.loadSession(param.getAuthToken());
        if (isPlatformMp(session)) {
            return platformAuthService.authWechatMp(param, session);
        }
        return channelAuthService.auth(param, session);
    }

    /// 是否支付宝认证类型(平台级支付宝走 H5 中间页)
    private boolean isAlipayAuth(String authType) {
        return Objects.equals(authType, ChannelAuthTypeEnum.ALIPAY.getCode());
    }

    /// 是否微信系统公众号配置来源(平台级)
    private boolean isPlatformMp(AuthSession session) {
        return session != null && AuthSession.SOURCE_PLATFORM_MP.equals(session.getSource());
    }
}
