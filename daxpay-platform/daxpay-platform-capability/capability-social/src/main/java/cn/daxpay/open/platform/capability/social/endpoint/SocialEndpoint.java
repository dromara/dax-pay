package cn.daxpay.open.platform.capability.social.endpoint;

import cn.daxpay.open.platform.capability.social.auth.SocialAuthRequestFactory;
import cn.daxpay.open.platform.capability.social.bind.SocialBindStore;
import cn.daxpay.open.platform.capability.social.bind.result.SocialBindResult;
import cn.daxpay.open.platform.capability.social.cache.RedisSocialStateCache;
import cn.daxpay.open.platform.capability.social.cache.SocialAuthContext;
import cn.daxpay.open.platform.capability.social.cache.SocialAuthMode;
import cn.daxpay.open.platform.capability.social.config.entity.SocialConfig;
import cn.daxpay.open.platform.capability.social.config.service.SocialConfigService;
import cn.daxpay.open.platform.capability.social.justauth.SocialAuthConfig;
import cn.daxpay.open.platform.capability.social.justauth.SocialSourceEnum;
import cn.daxpay.open.platform.capability.social.justauth.model.AuthCallback;
import cn.daxpay.open.platform.capability.social.justauth.model.AuthUser;
import cn.daxpay.open.platform.capability.social.justauth.request.SocialAuthRequest;
import cn.daxpay.open.platform.capability.social.login.SocialLoginHandler;
import cn.daxpay.open.platform.core.annotation.IgnoreAuth;
import cn.daxpay.open.platform.core.exception.operation.OperationFailException;
import cn.daxpay.open.platform.core.rest.Res;
import cn.daxpay.open.platform.core.rest.result.Result;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

    /// # 第三方社交登录端点
    ///
    /// 提供 OAuth2 授权(render)、授权码兑换(exchange)、绑定管理(bind list/unbind)等接口.
    /// 采用前端回调模式: 第三方平台直接重定向到前端回调页, 前端拿到 code+state 后
    /// 调用 exchange API 完成换 token, 后端不做 302 跳转.
    /// state 超时使用系统默认常量.
    ///
@Slf4j
@IgnoreAuth
@Tag(name = "第三方社交登录")
@RestController
@RequestMapping("/social")
@RequiredArgsConstructor
public class SocialEndpoint {

    /// state 缓存超时时间(秒), 用户完成第三方授权的合理等待时长
    private static final long STATE_TIMEOUT_SECONDS = 300L;

    private final SocialAuthRequestFactory socialAuthRequestFactory;

    private final RedisSocialStateCache redisSocialStateCache;

    private final SocialBindStore socialBindStore;

    private final SocialLoginHandler socialLoginHandler;

    private final SocialConfigService socialConfigService;

    /// 生成授权地址并缓存上下文(前端拿到后跳转)
    /// @param source 平台来源
    /// @param client 终端编码
    /// @param mode 授权场景(不传则按登录态判断: 已登录=绑定, 未登录=登录)
    /// @param redirect 成功后前端跳转路径(可选)
    @Operation(summary = "生成授权地址")
    @GetMapping("/render/{source}")
    public Result<String> render(
        @PathVariable String source,
        @RequestParam String client,
        @RequestParam(required = false) String mode,
        @RequestParam(required = false) String redirect) {
        // 加载平台配置(全局唯一)
        SocialConfig config = socialConfigService.findEnabledBySource(source);
        if (config == null) {
            // 社交登录: 平台未配置或未启用
            throw new OperationFailException("error.social.configNotExist");
        }
        SocialSourceEnum socialSource = SocialSourceEnum.of(source);
        if (socialSource == null) {
            // 社交登录: 不支持的平台
            throw new OperationFailException("error.social.unsupportedSource");
        }
        SocialAuthMode authMode = this.resolveMode(mode);
        // 构建 state 并缓存上下文(含平台来源, 供 exchange 阶段使用)
        String state = IdUtil.fastSimpleUUID();
        SocialAuthContext context = new SocialAuthContext()
            .setMode(authMode)
            .setClientCode(client)
            .setRedirect(redirect)
            .setSource(source);
        if (authMode == SocialAuthMode.BIND) {
            // 绑定场景必须已登录, 用户ID从登录态获取
            context.setUserId(cn.daxpay.open.platform.capability.auth.util.SecurityUtil.getUserId());
        }
        redisSocialStateCache.cache(state, context, STATE_TIMEOUT_SECONDS);
        // 构建授权请求并生成授权地址
        SocialAuthConfig authConfig = socialConfigService.buildAuthConfig(config);
        SocialAuthRequest request = socialAuthRequestFactory.create(socialSource, authConfig);
        String authorizeUrl = request.authorize(state);
        return Res.ok(authorizeUrl);
    }

    /// OAuth 授权码兑换(前端回调模式)
    /// 前端回调页收到第三方平台的 code+state 后调用此接口,
    /// 后端完成 code 换 token 并返回结果 JSON.
    @Operation(summary = "授权码兑换")
    @PostMapping("/exchange")
    public Result<SocialExchangeResult> exchange(
        @RequestParam("code") String code,
        @RequestParam("state") String state,
        HttpServletRequest request,
        HttpServletResponse response) {
        // 校验 state 并恢复上下文
        SocialAuthContext context = redisSocialStateCache.getAndRemove(state);
        if (context == null) {
            // state 已过期或非法
            return Res.ok(new SocialExchangeResult().setError("state_invalid"));
        }
        String source = context.getSource();
        String clientCode = context.getClientCode();
        try {
            // 用授权码换取用户信息
            SocialAuthRequest authRequest = socialAuthRequestFactory.build(source);
            AuthUser authUser = authRequest.login(AuthCallback.of(code, state));
            // 按场景处理
            if (context.getMode() == SocialAuthMode.BIND) {
                socialBindStore.saveBind(context.getUserId(), clientCode, authUser);
                return Res.ok(new SocialExchangeResult().setResult("bind_success"));
            } else {
                // LOGIN 场景: 仅已绑定的账号可直接登录
                Long userId = socialBindStore.findUserIdBySourceAndOpenId(source, authUser.getUuid()).orElse(null);
                if (userId == null) {
                    // 未绑定
                    return Res.ok(new SocialExchangeResult().setError("unbind"));
                }
                String token = socialLoginHandler.login(userId, clientCode, request, response);
                return Res.ok(new SocialExchangeResult().setToken(token));
            }
        } catch (Exception e) {
            log.error("社交登录兑换失败: source={}, msg={}", source, e.getMessage(), e);
            return Res.ok(new SocialExchangeResult().setError("oauth_failed"));
        }
    }

    /// 查询当前登录用户已绑定的第三方账号
    @IgnoreAuth(login = true)
    @Operation(summary = "已绑定的第三方账号列表")
    @GetMapping("/bind/list")
    public Result<List<SocialBindResult>> bindList() {
        Long userId = cn.daxpay.open.platform.capability.auth.util.SecurityUtil.getUserId();
        return Res.ok(socialBindStore.findBindsByUserId(userId));
    }

    /// 解除当前登录用户的指定平台绑定
    @IgnoreAuth(login = true)
    @Operation(summary = "解除第三方账号绑定")
    @PostMapping("/unbind")
    public Result<Void> unbind(@RequestParam String source) {
        Long userId = cn.daxpay.open.platform.capability.auth.util.SecurityUtil.getUserId();
        boolean success = socialBindStore.removeBind(userId, source);
        if (!success) {
            // 社交登录: 未绑定该平台, 无需解绑
            return Res.ok();
        }
        return Res.ok();
    }

    /// 解析授权场景(未传 mode 时按登录态判断)
    private SocialAuthMode resolveMode(String mode) {
        if (StrUtil.isNotBlank(mode)) {
            try {
                return SocialAuthMode.valueOf(mode.toUpperCase());
            } catch (IllegalArgumentException ignored) {
            }
        }
        // 默认: 已登录走绑定, 未登录走登录
        boolean login = cn.daxpay.open.platform.capability.auth.util.SecurityUtil.isLogin();
        return login ? SocialAuthMode.BIND : SocialAuthMode.LOGIN;
    }
}
