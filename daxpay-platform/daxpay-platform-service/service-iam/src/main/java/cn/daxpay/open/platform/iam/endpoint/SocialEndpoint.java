package cn.daxpay.open.platform.iam.endpoint;

import java.util.List;

import cn.daxpay.open.platform.capability.auth.util.SecurityUtil;
import cn.daxpay.open.platform.core.annotation.IgnoreAuth;
import cn.daxpay.open.platform.core.rest.Res;
import cn.daxpay.open.platform.core.rest.result.Result;
import cn.daxpay.open.platform.iam.param.social.SocialAppletBindParam;
import cn.daxpay.open.platform.iam.param.social.SocialAppletLoginParam;
import cn.daxpay.open.platform.iam.result.social.SocialBindResult;
import cn.daxpay.open.platform.iam.result.social.SocialEnabledPlatformResult;
import cn.daxpay.open.platform.iam.result.social.SocialExchangeResult;
import cn.daxpay.open.platform.iam.service.social.SocialLoginService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/// # 第三方社交登录端点
///
/// 瘦控制器, 仅负责 HTTP 参数解包与结果包装, 业务编排全部委托 [SocialLoginService].
/// 采用前端回调模式: 第三方平台直接重定向到前端回调页, 前端拿到 code+state 后
/// 调用 exchange-login 或 exchange-bind 接口完成换 token, 后端不做 302 跳转.
///
@IgnoreAuth
@Tag(name = "第三方社交登录")
@RestController
@RequestMapping("/social")
@RequiredArgsConstructor
public class SocialEndpoint {

    private final SocialLoginService socialLoginService;

    /// 查询已启用的第三方登录平台(登录页公开接口)
    /// 仅返回平台编码列表, 不含任何敏感字段, 供登录页动态渲染第三方登录按钮.
    @Operation(summary = "查询已启用的第三方登录平台")
    @GetMapping("/enabled-list")
    public Result<List<SocialEnabledPlatformResult>> enabledList() {
        return Res.ok(socialLoginService.enabledList());
    }

    /// 生成授权地址
    /// @param source 平台来源
    /// @param client 终端编码(admin/merchant), 用于解析端点配置中的 baseUrl
    /// @param mode 授权场景(不传则按登录态判断: 已登录=绑定, 未登录=登录)
    @Operation(summary = "生成授权地址")
    @GetMapping("/render/{source}")
    public Result<String> render(@PathVariable String source,
                                 String client,
                                 @RequestParam(required = false) String mode,
                                 @RequestParam(required = false) Boolean silent) {
        return Res.ok(socialLoginService.generateAuthorizeUrl(source, client, mode, silent));
    }

    /// OAuth 授权码兑换 - 登录(公开, 无需认证)
    /// 前端登录回调页(/auth/oauth-callback/{source})收到第三方平台的 code+state 后调用,
    /// 后端完成 code 换 token 并返回登录结果.
    @Operation(summary = "授权码兑换-登录")
    @PostMapping("/exchange-login")
    public Result<SocialExchangeResult> exchangeLogin(@RequestParam("code") String code,
                                                       @RequestParam("state") String state,
                                                       @RequestParam("source") String source,
                                                       @RequestParam("client") String client,
                                                       HttpServletRequest request,
                                                       HttpServletResponse response) {
        return Res.ok(socialLoginService.exchangeForLogin(code, state, source, client, request, response));
    }

    /// 小程序快捷登录(公开, 无需认证)
    /// 小程序端通过 uni.login / my.getAuthCode / tt.login 获取平台 login code 后直传,
    /// 后端换取 openId 并按绑定关系登录; 未绑定时返回明确引导错误(不自动注册)。
    /// code 具有平台侧一次性语义, 不加 @NonceVerification 防重放。
    @Operation(summary = "小程序快捷登录")
    @PostMapping("/applet-login")
    public Result<SocialExchangeResult> appletLogin(@RequestBody @Validated SocialAppletLoginParam param,
                                                     HttpServletRequest request,
                                                     HttpServletResponse response) {
        return Res.ok(socialLoginService.appletLogin(param, request, response));
    }

    /// 小程序快捷绑定(需登录)
    /// 已登录用户在小程序内发起三方绑定, code 直传换取 openId 后保存绑定关系,
    /// 终端与用户身份取自登录态。
    @IgnoreAuth(login = true)
    @Operation(summary = "小程序快捷绑定")
    @PostMapping("/applet-bind")
    public Result<SocialExchangeResult> appletBind(@RequestBody @Validated SocialAppletBindParam param) {
        return Res.ok(socialLoginService.appletBind(param));
    }

    /// OAuth 授权码兑换 - 绑定(需登录)
    /// 前端绑定回调页(/auth/social-bind-callback/{source})收到第三方平台的 code+state 后调用,
    /// 后端完成 code 换 token 并保存绑定关系到当前登录用户.
    @IgnoreAuth(login = true)
    @Operation(summary = "授权码兑换-绑定")
    @PostMapping("/exchange-bind")
    public Result<SocialExchangeResult> exchangeBind(@RequestParam("code") String code,
                                                     @RequestParam("state") String state,
                                                     @RequestParam("source") String source,
                                                     @RequestParam("client") String client) {
        return Res.ok(socialLoginService.exchangeForBind(code, state, source, client));
    }

    /// 查询当前登录用户已绑定的第三方账号
    @IgnoreAuth(login = true)
    @Operation(summary = "已绑定的第三方账号列表")
    @GetMapping("/bind/list")
    public Result<List<SocialBindResult>> bindList() {
        return Res.ok(socialLoginService.bindList(SecurityUtil.getUserId()));
    }

    /// 解除当前登录用户的指定平台绑定
    @IgnoreAuth(login = true)
    @Operation(summary = "解除第三方账号绑定")
    @PostMapping("/unbind")
    public Result<Void> unbind(String source) {
        socialLoginService.unbind(SecurityUtil.getUserId(), source);
        return Res.ok();
    }
}
