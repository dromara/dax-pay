package cn.daxpay.open.platform.iam.endpoint;

import java.util.List;

import cn.daxpay.open.platform.capability.auth.util.SecurityUtil;
import cn.daxpay.open.platform.core.annotation.IgnoreAuth;
import cn.daxpay.open.platform.core.rest.Res;
import cn.daxpay.open.platform.core.rest.result.Result;
import cn.daxpay.open.platform.iam.result.social.SocialBindResult;
import cn.daxpay.open.platform.iam.result.social.SocialEnabledPlatformResult;
import cn.daxpay.open.platform.iam.result.social.SocialExchangeResult;
import cn.daxpay.open.platform.iam.service.social.SocialLoginService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/// # 第三方社交登录端点
///
/// 瘦控制器, 仅负责 HTTP 参数解包与结果包装, 业务编排全部委托 [SocialLoginService].
/// 采用前端回调模式: 第三方平台直接重定向到前端回调页, 前端拿到 code+state 后
/// 调用 exchange 接口完成换 token, 后端不做 302 跳转.
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
    /// @param redirect 成功后前端跳转路径(可选)
    @Operation(summary = "生成授权地址")
    @GetMapping("/render/{source}")
    public Result<String> render(@PathVariable String source,
                                 @RequestParam String client,
                                 @RequestParam(required = false) String mode,
                                 @RequestParam(required = false) String redirect) {
        return Res.ok(socialLoginService.generateAuthorizeUrl(source, client, mode, redirect));
    }

    /// OAuth 授权码兑换(前端回调模式)
    /// 前端回调页收到第三方平台的 code+state 后调用此接口,
    /// 后端完成 code 换 token 并返回结果 JSON.
    @Operation(summary = "授权码兑换")
    @PostMapping("/exchange")
    public Result<SocialExchangeResult> exchange(@RequestParam("code") String code,
                                                 @RequestParam("state") String state,
                                                 HttpServletRequest request,
                                                 HttpServletResponse response) {
        return Res.ok(socialLoginService.exchangeCode(code, state, request, response));
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
    public Result<Void> unbind(@RequestParam String source) {
        socialLoginService.unbind(SecurityUtil.getUserId(), source);
        return Res.ok();
    }
}
