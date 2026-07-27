package cn.daxpay.open.payment.unipay.client.controller;

import cn.daxpay.open.payment.unipay.client.service.OpenAuthService;
import cn.daxpay.open.payment.unipay.param.open.OpenAuthParam;
import cn.daxpay.open.platform.core.annotation.IgnoreAuth;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.view.RedirectView;

/// # 通用认证接口(对外开放认证 OPEN 场景)
///
/// 对外提供获取用户标识(openId/userId)的重定向接口, 参考 hkrt getOpenid 模式:
/// 对接方构建签名 URL 引导用户浏览器访问 → 系统验签后 302 到第三方 OAuth →
/// OAuth 回调后系统换 openId → 302 重定向到对接方 redirect_url 带用户标识和签名。
///
/// ## 三通道支持
/// - **wechat**: 走商户通道绑定的微信应用(WxAppFacade 解析), 公众号 OAuth 取 openId
/// - **alipay**: 走平台级支付宝配置, auth_base 静默授权取 userId
/// - **douyin**: 走商户通道绑定的抖音应用, H5 silent_auth 取 openId
///
/// ## 安全
/// 入口要求商户签名(与支付接口一致), 验签通过后 redirect_url 即可信。
/// 回调重定向参数附加平台签名, 对接方可验签。
@IgnoreAuth
@Validated
@Tag(name = "通用认证服务(对外开放)")
@RestController
@RequestMapping("/unipay/open/auth")
@RequiredArgsConstructor
public class OpenAuthController {

    private final OpenAuthService openAuthService;

    /// 获取用户标识(重定向入口)
    ///
    /// 验签通过后, 生成 OAuth 授权链接并 302 重定向。
    /// 授权完成后第三方回调到 `/unipay/open/auth/callback`。
    @Operation(summary = "获取用户标识(重定向)")
    @GetMapping("/get-openid")
    public RedirectView getOpenId(@Valid OpenAuthParam param) {
        return new RedirectView(openAuthService.generateOpenAuthRedirect(param));
    }

    /// OAuth 回调处理
    ///
    /// 第三方 OAuth 授权完成后回调到此接口, 系统用 code 换取 openId/userId,
    /// 然后 302 重定向到对接方的 redirect_url 带用户标识和签名。
    @Operation(summary = "OAuth 认证回调")
    @GetMapping("/callback")
    public RedirectView callback(String code, String state) {
        String redirectUrl = openAuthService.handleCallback(code, state);
        return new RedirectView(redirectUrl);
    }
}