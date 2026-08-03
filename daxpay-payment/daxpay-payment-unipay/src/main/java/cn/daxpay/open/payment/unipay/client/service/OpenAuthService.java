package cn.daxpay.open.payment.unipay.client.service;

import cn.daxpay.open.payment.auth.core.AuthScene;
import cn.daxpay.open.payment.auth.core.AuthSession;
import cn.daxpay.open.payment.auth.core.AuthSessionStore;
import cn.daxpay.open.payment.auth.UnifiedAuthService;
import cn.daxpay.open.payment.common.context.MerchantContextLoader;
import cn.daxpay.open.payment.common.util.PaySignUtil;
import cn.daxpay.open.payment.unipay.aop.PaymentSignService;
import cn.daxpay.open.payment.unipay.param.assist.AuthCodeParam;
import cn.daxpay.open.payment.unipay.param.assist.GenerateAuthUrlParam;
import cn.daxpay.open.payment.unipay.param.open.OpenAuthParam;
import cn.daxpay.open.payment.unipay.result.assist.AuthResult;
import cn.daxpay.open.payment.unipay.result.assist.AuthUrlResult;
import cn.daxpay.open.payment.unipay.result.open.OpenAuthRedirectResult;
import cn.daxpay.open.payment.wx.facade.WxAppFacade;
import cn.daxpay.open.payment.wx.facade.WxAppView;
import cn.daxpay.open.payment.douyin.facade.DouyinAppFacade;
import cn.daxpay.open.payment.douyin.facade.DyAppView;
import cn.daxpay.open.platform.common.config.properties.PlatformConfigProperties;
import cn.daxpay.open.platform.core.code.CommonCode;
import cn.daxpay.open.platform.core.code.CommonErrorCode;
import cn.daxpay.open.platform.core.code.DaxPayErrorCode;
import cn.daxpay.open.platform.core.enums.unipay.ChannelAuthTypeEnum;
import cn.daxpay.open.platform.core.exception.BizInfoException;
import cn.daxpay.open.platform.core.util.ValidationUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.core.util.URLUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/// # 通用认证服务(OPEN 场景)
///
/// 对外开放认证的场景适配层, 供 [OpenAuthController] 使用。核心职责:
///
/// 1. **入口验签**: 验证商户签名后生成 OAuth 重定向链接(委托 [UnifiedAuthService])
/// 2. **回调处理**: OAuth 回调后用 code 换 openId/userId, 构建带签名的重定向 URL 回给对接方
///
/// ## 与 UnifiedAuthService 的关系
/// 复用 [UnifiedAuthService] 的分发能力(source/product 路由), 但在生成授权链接后更新 session
/// 标记 `scene=OPEN`, 以便回调时做重定向式结果返回(而非 JSON)。
///
/// ## 安全约束
/// - 入口要求商户签名, 验签通过后 redirect_url 即可信(商户自己指定的回调地址)
/// - 回调重定向参数附加平台签名, 对接方可用平台公钥验签确认响应来源
@Slf4j
@Service
@RequiredArgsConstructor
public class OpenAuthService {

    private final PaymentSignService paymentSignService;
    private final MerchantContextLoader merchantContextLoader;
    private final UnifiedAuthService unifiedAuthService;
    private final AuthSessionStore authSessionStore;
    private final PlatformConfigProperties platformConfigProperties;
    private final WxAppFacade wxAppFacade;
    private final DouyinAppFacade douyinAppFacade;

    /// 生成 OAuth 重定向链接
    ///
    /// 流程: 参数校验 → 加载商户上下文 → 验签 → 委托 UnifiedAuthService 生成 authUrl →
    /// 更新 session(scene=OPEN, redirect_url) → 返回 authUrl 供 Controller 302 重定向
    public String generateOpenAuthRedirect(OpenAuthParam param) {
        // 参数校验
        ValidationUtil.validateParam(param);
        // 商户身份初始化(含状态校验), 使 mchNo 进入线程上下文供签名校验
        merchantContextLoader.initMch(param.getMchNo());
        // 参数签名校验
        paymentSignService.signVerify(param);

        // 组装认证参数, 委托 UnifiedAuthService 按 authType 分发
        GenerateAuthUrlParam authParam = new GenerateAuthUrlParam();
        authParam.setMchNo(param.getMchNo());
        authParam.setAppId(param.getAppId());
        authParam.setAuthType(param.getAuthType());
        // redirect_url 存入 session.returnPath, 回调时取出构建重定向
        authParam.setReturnPath(param.getRedirectUrl());
        // 通道认证: 入口层 resolve 选定应用, 把档位+主键塞入 param 供策略查密钥
        if (ChannelAuthTypeEnum.WECHAT.getCode().equals(param.getAuthType())) {
            if (StrUtil.isBlank(param.getWxAppId())) {
                // 开放认证: 微信认证必须指定 wxAppId
                throw new BizInfoException(CommonErrorCode.VALIDATE_PARAMETERS_ERROR,
                        "validation.field.wxAppId.notBlank");
            }
            WxAppView app = wxAppFacade.resolveByWxAppId(param.getMchNo(), param.getWxAppId());
            authParam.setAppScope(app.scope().getCode());
            authParam.setAppRefId(app.id());
        }
        else if (ChannelAuthTypeEnum.DOUYIN.getCode().equals(param.getAuthType())) {
            // 抖音 H5 silent_auth 固定使用网站应用(web_app), 开放接口按商户号解析
            DyAppView app = douyinAppFacade.resolveWebAppForH5Auth(param.getMchNo(), null, null);
            authParam.setAppScope(app.scope().getCode());
            authParam.setAppRefId(app.id());
        }
        AuthUrlResult urlResult = unifiedAuthService.generateAuthUrl(authParam);

        // 更新 session: 标记 scene=OPEN(回调时据此做重定向而非 JSON 返回)
        String authToken = urlResult.getAuthToken();
        if (StrUtil.isNotBlank(authToken)) {
            AuthSession session = authSessionStore.loadSession(authToken);
            if (session != null) {
                session.setScene(AuthScene.OPEN.getCode());
                authSessionStore.saveSession(authToken, session);
            }
        }
        return urlResult.getAuthUrl();
    }

    /// OAuth 回调处理
    ///
    /// 流程: 恢复 session → 委托 UnifiedAuthService.auth 获取 openId/userId →
    /// 构建带签名的重定向 URL 回给对接方
    ///
    /// @param code 第三方 OAuth 授权码
    /// @param state 认证会话码(=authToken, 由 OAuth state 透传)
    /// @return 完整的重定向 URL(redirect_url?code=0&openid=xxx&sign=xxx)
    public String handleCallback(String code, String state) {
        // 恢复 session
        AuthSession session = authSessionStore.loadSession(state);
        if (session == null) {
            // 会话已失效, 无法恢复 redirect_url, 只能抛异常
            throw new BizInfoException(DaxPayErrorCode.OPERATION_FAIL,
                    "pay.error.assist.authSessionExpired");
        }
        // 场景校验: 仅 OPEN 场景的 session 可走开放重定向回调。
        // 防止公开网关接口(GatewayClientController#generateAuthUrl, 无签名)创建的 PAYMENT 场景 session
        // 被旁路用于把用户 openid 302 到任意 URL(开放重定向 + 用户标识泄漏)
        if (!AuthScene.OPEN.getCode().equals(session.getScene())) {
            log.warn("OPEN 回调场景不匹配, 拒绝重定向: state={}, scene={}", state, session.getScene());
            throw new BizInfoException(DaxPayErrorCode.OPERATION_FAIL,
                    "pay.error.assist.authSceneMismatch");
        }
        // 先保存 redirect_url(UnifiedAuthService.auth 成功后会销毁 session)
        String redirectUrl = session.getReturnPath();

        // 构建 AuthCodeParam 并委托获取 openId/userId
        AuthCodeParam authCodeParam = new AuthCodeParam();
        authCodeParam.setAuthCode(code);
        authCodeParam.setAuthToken(state);
        authCodeParam.setQueryCode(session.getQueryCode());
        try {
            AuthResult authResult = unifiedAuthService.auth(authCodeParam);
            return buildSuccessRedirectUrl(redirectUrl, authResult);
        } catch (RuntimeException e) {
            log.warn("OPEN 认证回调失败, redirectUrl={}, error={}", redirectUrl, e.getMessage());
            return buildErrorRedirectUrl(redirectUrl, e.getMessage());
        }
    }

    /// 构建成功重定向 URL: redirect_url?code=0&msg=success&openid=xxx&sign=xxx
    private String buildSuccessRedirectUrl(String redirectUrl, AuthResult authResult) {
        OpenAuthRedirectResult result = new OpenAuthRedirectResult()
                .setCode(CommonCode.SUCCESS_CODE)
                .setMsg(CommonCode.SUCCESS_MSG)
                .setOpenId(authResult.getOpenId());
        signResult(result);
        return appendQueryParams(redirectUrl, result);
    }

    /// 构建失败重定向 URL: redirect_url?code=1&msg=xxx&sign=xxx
    private String buildErrorRedirectUrl(String redirectUrl, String errorMsg) {
        OpenAuthRedirectResult result = new OpenAuthRedirectResult()
                .setCode(CommonCode.FAIL_CODE)
                .setMsg(StrUtil.sub(errorMsg, 0, 200));
        signResult(result);
        return appendQueryParams(redirectUrl, result);
    }

    /// 使用平台私钥对回调参数签名
    private void signResult(OpenAuthRedirectResult result) {
        String privateKey = platformConfigProperties.getKeyConfig().getPrivateKey();
        result.setSign(PaySignUtil.sign(result, privateKey));
    }

    /// 将回调参数拼接为 query string 追加到 baseUrl
    private String appendQueryParams(String baseUrl, OpenAuthRedirectResult result) {
        // returnPath 为空(异常 session)时回退到平台首页, 避免 baseUrl.contains NPE
        if (StrUtil.isBlank(baseUrl)) {
            baseUrl = "/";
        }
        StringBuilder sb = new StringBuilder(baseUrl);
        sb.append(baseUrl.contains("?") ? "&" : "?");
        sb.append("code=").append(result.getCode());
        sb.append("&msg=").append(URLUtil.encode(result.getMsg()));
        if (StrUtil.isNotBlank(result.getOpenId())) {
            sb.append("&openid=").append(URLUtil.encode(result.getOpenId()));
        }
        sb.append("&sign=").append(URLUtil.encode(result.getSign()));
        return sb.toString();
    }
}