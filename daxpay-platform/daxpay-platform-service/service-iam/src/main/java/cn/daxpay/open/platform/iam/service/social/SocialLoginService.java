package cn.daxpay.open.platform.iam.service.social;

import java.util.List;

import cn.daxpay.open.platform.capability.auth.util.SecurityUtil;
import cn.daxpay.open.platform.capability.social.auth.SocialAuthRequestFactory;
import cn.daxpay.open.platform.capability.social.justauth.SocialAuthConfig;
import cn.daxpay.open.platform.capability.social.justauth.SocialSourceEnum;
import cn.daxpay.open.platform.capability.social.justauth.model.AuthCallback;
import cn.daxpay.open.platform.capability.social.justauth.model.AuthUser;
import cn.daxpay.open.platform.capability.social.justauth.request.SocialAuthRequest;
import cn.daxpay.open.platform.core.exception.operation.OperationFailException;
import cn.daxpay.open.platform.iam.entity.social.SocialConfig;
import cn.daxpay.open.platform.iam.enums.SocialClientEnum;
import cn.daxpay.open.platform.iam.result.social.SocialBindResult;
import cn.daxpay.open.platform.iam.result.social.SocialEnabledPlatformResult;
import cn.daxpay.open.platform.iam.result.social.SocialExchangeResult;
import cn.daxpay.open.platform.iam.service.social.cache.RedisSocialStateCache;
import cn.daxpay.open.platform.iam.service.social.cache.SocialAuthContext;
import cn.daxpay.open.platform.iam.service.social.cache.SocialAuthMode;
import cn.daxpay.open.platform.system.entity.config.platform.PlatformUrlConfig;
import cn.daxpay.open.platform.system.service.config.PlatformUrlConfigService;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/// # 第三方社交登录编排服务
///
/// 编排 OAuth2 授权流程: 授权地址生成(render)、授权码兑换(exchange)、
/// 绑定关系查询与解绑. 由 [SocialEndpoint] 作为瘦控制器入口调用,
/// 数据访问与配置管理分别委托 [IamUserSocialBindStore] / [SocialConfigService],
/// Sa-Token 签发委托 [IamSocialLoginHandler].
/// 采用前端回调模式: 第三方平台直接重定向到前端回调页, 前端拿到 code+state 后
/// 调用 [exchangeCode] 完成换 token, 后端不做 302 跳转.
/// state 超时使用系统默认常量.
///
@Slf4j
@Service
@RequiredArgsConstructor
public class SocialLoginService {

    /// state 缓存超时时间(秒), 用户完成第三方授权的合理等待时长
    private static final long STATE_TIMEOUT_SECONDS = 300L;

    private final SocialAuthRequestFactory socialAuthRequestFactory;

    private final RedisSocialStateCache redisSocialStateCache;

    private final IamUserSocialBindStore socialBindStore;

    private final IamSocialLoginHandler socialLoginHandler;

    private final SocialConfigService socialConfigService;

    private final PlatformUrlConfigService platformUrlConfigService;

    /// 查询已启用的第三方登录平台(登录页公开接口)
    /// 仅返回平台编码列表, 不含任何敏感字段, 供登录页动态渲染第三方登录按钮.
    public List<SocialEnabledPlatformResult> enabledList() {
        return socialConfigService.findEnabledList();
    }

    /// 生成授权地址并缓存上下文(前端拿到后跳转)
    /// @param source 平台来源
    /// @param client 终端编码(admin/merchant), 用于解析端点配置中的 baseUrl
    /// @param mode 授权场景(不传则按登录态判断: 已登录=绑定, 未登录=登录)
    /// @param redirect 成功后前端跳转路径(可选)
    public String generateAuthorizeUrl(String source, String client, String mode, String redirect) {
        // 加载平台配置(全局唯一)
        SocialConfig config = this.loadEnabledConfig(source);
        SocialSourceEnum socialSource = SocialSourceEnum.of(source);
        if (socialSource == null) {
            // 社交登录: 不支持的平台
            throw new OperationFailException("error.social.unsupportedSource");
        }
        // 按 client 解析前端 baseUrl(用于 redirectUri 自动生成)
        String baseUrl = this.resolveBaseUrl(client);
        // 回调地址由端点配置的 baseUrl 自动生成, 必须配置 baseUrl
        if (StrUtil.isBlank(baseUrl)) {
            // 社交登录: 端点配置缺失
            throw new OperationFailException("error.social.endpointNotConfigured");
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
            context.setUserId(SecurityUtil.getUserId());
        }
        redisSocialStateCache.cache(state, context, STATE_TIMEOUT_SECONDS);
        // 构建授权请求并生成授权地址
        SocialAuthConfig authConfig = socialConfigService.buildAuthConfig(config, baseUrl);
        SocialAuthRequest request = socialAuthRequestFactory.create(socialSource, authConfig);
        return request.authorize(state);
    }

    /// OAuth 授权码兑换(前端回调模式)
    /// 前端回调页收到第三方平台的 code+state 后调用此方法,
    /// 后端完成 code 换 token 并返回结果 JSON.
    public SocialExchangeResult exchangeCode(String code, String state,
                                             HttpServletRequest request, HttpServletResponse response) {
        // 校验 state 并恢复上下文
        SocialAuthContext context = redisSocialStateCache.getAndRemove(state);
        if (context == null) {
            // state 已过期或非法
            return new SocialExchangeResult().setError("state_invalid");
        }
        String source = context.getSource();
        String clientCode = context.getClientCode();
        try {
            // 加载平台配置 + 创建对应 Request
            SocialConfig config = socialConfigService.findEnabledBySource(source);
            if (config == null) {
                return new SocialExchangeResult().setError("oauth_failed");
            }
            // exchange 阶段的 redirect_uri 必须与 authorize 阶段一致, 从端点配置按 client 解析 baseUrl
            String baseUrl = this.resolveBaseUrl(clientCode);
            SocialAuthConfig authConfig = socialConfigService.buildAuthConfig(config, baseUrl);
            SocialSourceEnum socialSource = SocialSourceEnum.of(source);
            SocialAuthRequest authRequest = socialAuthRequestFactory.create(socialSource, authConfig);
            AuthUser authUser = authRequest.login(AuthCallback.of(code, state));
            // 按场景处理
            if (context.getMode() == SocialAuthMode.BIND) {
                socialBindStore.saveBind(context.getUserId(), clientCode, authUser);
                return new SocialExchangeResult().setResult("bind_success");
            } else {
                // LOGIN 场景: 仅已绑定的账号可直接登录
                Long userId = socialBindStore.findUserIdBySourceAndOpenId(source, authUser.getUuid()).orElse(null);
                if (userId == null) {
                    // 未绑定
                    return new SocialExchangeResult().setError("unbind");
                }
                String token = socialLoginHandler.login(userId, clientCode, request, response);
                return new SocialExchangeResult().setToken(token);
            }
        } catch (Exception e) {
            log.error("社交登录兑换失败: source={}, msg={}", source, e.getMessage(), e);
            return new SocialExchangeResult().setError("oauth_failed");
        }
    }

    /// 查询指定用户已绑定的所有第三方账号
    public List<SocialBindResult> bindList(Long userId) {
        return socialBindStore.findBindsByUserId(userId);
    }

    /// 解除指定用户的某个平台绑定(幂等: 未绑定则无操作)
    public void unbind(Long userId, String source) {
        socialBindStore.removeBind(userId, source);
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
        boolean login = SecurityUtil.isLogin();
        return login ? SocialAuthMode.BIND : SocialAuthMode.LOGIN;
    }

    /// 按 client 解析前端 baseUrl(用于 redirectUri 自动生成)
    private String resolveBaseUrl(String clientCode) {
        PlatformUrlConfig urlConfig = platformUrlConfigService.getUrlConfig();
        return SocialClientEnum.of(clientCode).resolveBaseUrl(urlConfig);
    }

    /// 加载已启用的平台配置(不存在则抛业务异常)
    private SocialConfig loadEnabledConfig(String source) {
        SocialConfig config = socialConfigService.findEnabledBySource(source);
        if (config == null) {
            // 社交登录: 平台未配置或未启用
            throw new OperationFailException("error.social.configNotExist");
        }
        return config;
    }
}
