package cn.daxpay.open.payment.unipay.trade.controller;

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
import cn.daxpay.open.payment.unipay.aop.PaymentVerify;
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
/// 按来源把认证请求分流到平台或通道:
/// - **生成授权链接**: authType=ALIPAY 走平台级支付宝 OAuth([PlatformAuthService]); 其余按支付产品走通道策略([ChannelAuthService])
/// - **授权码回调**: 优先按 session.source 走平台级分支(alipay/mp/douyin), 否则走通道策略;
///   authType=ALIPAY 且无 session 时走平台级(小程序等直连兜底)
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
        // 支付宝: 平台级 OAuth(不依赖商户上下文)
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

    /// 按会话来源把授权码回调分到平台或通道处理
    ///
    /// 优先级: 会话来源 platform_alipay / platform_mp / platform_douyin >
    /// authType=ALIPAY 且无会话(小程序直连兜底) > 通道策略。
    /// OAuth 重定向回调通常只有 authToken, 先从会话恢复上下文再判断来源。
    private AuthResult doAuth(AuthCodeParam param) {
        AuthSession session = authSessionStore.loadSession(param.getAuthToken());
        if (isPlatformAlipay(session)) {
            return platformAuthService.authAlipay(param, session);
        }
        if (isPlatformMp(session)) {
            return platformAuthService.authWechatMp(param, session);
        }
        if (isPlatformDouyin(session)) {
            return platformAuthService.authDouyin(param, session);
        }
        // 无会话且 authType=alipay: 小程序等直连场景兜底
        if (isAlipayAuth(param.getAuthType()) && session == null) {
            return platformAuthService.authAlipay(param, null);
        }
        return channelAuthService.auth(param, session);
    }

    /// 是否支付宝认证类型(平台级支付宝走 OAuth)
    private boolean isAlipayAuth(String authType) {
        return Objects.equals(authType, ChannelAuthTypeEnum.ALIPAY.getCode());
    }

    /// 是否支付宝平台级配置来源
    private boolean isPlatformAlipay(AuthSession session) {
        return session != null && AuthSession.SOURCE_PLATFORM_ALIPAY.equals(session.getSource());
    }

    /// 是否微信系统公众号配置来源(平台级)
    private boolean isPlatformMp(AuthSession session) {
        return session != null && AuthSession.SOURCE_PLATFORM_MP.equals(session.getSource());
    }

    /// 是否抖音 H5 应用配置来源(平台级)
    private boolean isPlatformDouyin(AuthSession session) {
        return session != null && AuthSession.SOURCE_PLATFORM_DOUYIN.equals(session.getSource());
    }
}
