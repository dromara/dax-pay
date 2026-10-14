package cn.daxpay.open.platform.iam.service.social;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import cn.daxpay.open.platform.capability.auth.authentication.UserInfoStatusCheck;
import cn.daxpay.open.platform.capability.auth.util.SecurityUtil;
import cn.daxpay.open.platform.capability.social.auth.SocialAuthRequestFactory;
import cn.daxpay.open.platform.capability.social.justauth.SocialAuthConfig;
import cn.daxpay.open.platform.capability.social.justauth.SocialSourceEnum;
import cn.daxpay.open.platform.capability.social.justauth.model.AuthUser;
import cn.daxpay.open.platform.capability.social.justauth.request.SocialAuthRequest;
import cn.daxpay.open.platform.common.config.properties.PlatformStarterProperties;
import cn.daxpay.open.platform.core.exception.operation.OperationFailException;
import cn.daxpay.open.platform.iam.entity.social.SocialLoginConfig;
import cn.daxpay.open.platform.iam.result.social.SocialBindScanQrResult;
import cn.daxpay.open.platform.iam.result.social.SocialBindScanStatusResult;
import cn.daxpay.open.platform.iam.result.social.SocialExchangeResult;
import cn.daxpay.open.platform.iam.result.social.SocialLoginScanQrResult;
import cn.daxpay.open.platform.iam.result.social.SocialLoginScanStatusResult;
import cn.daxpay.open.platform.iam.result.user.UserInfoResult;
import cn.daxpay.open.platform.iam.service.social.other.AlipaySocialAuthRequestFactory;
import cn.daxpay.open.platform.iam.service.user.UserQueryService;
import cn.daxpay.open.platform.system.entity.config.platform.auth.PlatformWechatMpAuthConfig;
import cn.daxpay.open.platform.system.entity.config.platform.infra.PlatformUrlConfig;
import cn.daxpay.open.platform.system.service.config.auth.PlatformWechatMpAuthConfigService;
import cn.daxpay.open.platform.system.service.config.infra.PlatformUrlConfigService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/// # 社交登录回调透传与扫码绑定/扫码登录测试
///
/// 覆盖 [SocialLoginService] 跳转型平台(支付宝/公众号)回调改走支付网关域名后的拼装语义:
/// - 跳转型 generateAuthorizeUrl 的 redirectUri 指向网关透传路径(带 client 段), 网关未配置时报错;
/// - 标准型平台沿用各端自身域名, 不受网关配置影响;
/// - [SocialLoginService#resolveTransitTarget] 的白名单校验与目标端回调地址拼装;
/// - 公众号扫码绑定(跨设备): 票据生成/授权地址/兑换/状态轮询的编排语义;
/// - 公众号扫码登录(跨设备): 匿名票据生成/未绑定不自动注册/success 一次性签发登录令牌.
@ExtendWith(MockitoExtension.class)
class SocialLoginServiceTest {

    private static final String ADMIN_BASE = "https://admin.example.com";

    private static final String MERCHANT_BASE = "https://mch.example.com";

    private static final String GATEWAY_BASE = "https://gw.example.com";

    @Mock
    private SocialAuthRequestFactory socialAuthRequestFactory;

    @Mock
    private AlipaySocialAuthRequestFactory alipaySocialAuthRequestFactory;

    @Mock
    private IamUserSocialBindStore socialBindStore;

    @Mock
    private SocialBindScanStore socialBindScanStore;

    @Mock
    private SocialLoginScanStore socialLoginScanStore;

    @Mock
    private PlatformStarterProperties platformStarterProperties;

    /// 状态检查链注入空实现(扫码登录兑换会遍历该链, 空列表即通过)
    @Spy
    private List<UserInfoStatusCheck> userInfoStatusChecks = new ArrayList<>();

    @Mock
    private UserQueryService userQueryService;

    @Mock
    private IamSocialLoginHandler socialLoginHandler;

    @Mock
    private SocialLoginConfigService socialLoginConfigService;

    @Mock
    private PlatformUrlConfigService platformUrlConfigService;

    @Mock
    private PlatformWechatMpAuthConfigService platformWechatMpAuthConfigService;

    @Mock
    private SocialAuthRequest socialAuthRequest;

    @InjectMocks
    private SocialLoginService socialLoginService;

    /// 端点配置桩(三地址齐备)
    private PlatformUrlConfig fullUrlConfig() {
        return new PlatformUrlConfig()
                .setAdminBaseUrl(ADMIN_BASE)
                .setMerchantBaseUrl(MERCHANT_BASE)
                .setPaymentGatewayBaseUrl(GATEWAY_BASE);
    }

    /// 公众号平台级凭据桩
    private PlatformWechatMpAuthConfig mpConfig() {
        return new PlatformWechatMpAuthConfig()
                .setAppId("wx-appid")
                .setAppSecret("wx-secret");
    }

    /// stub: 平台已启用 + authConfig 透传 redirectUri, 供参数捕获
    ///
    /// buildAuthConfig 用 lenient: 提前抛错的用例(如网关未配置)不会走到该 stub
    private void stubEnabled(String source) {
        when(socialLoginConfigService.findEnabledBySource(source)).thenReturn(new SocialLoginConfig());
        org.mockito.Mockito.lenient()
                .when(socialLoginConfigService.buildAuthConfig(any(), any()))
                .thenAnswer(invocation -> {
                    SocialAuthConfig config = new SocialAuthConfig();
                    config.setRedirectUri(invocation.getArgument(1));
                    return config;
                });
    }

    @Test
    @DisplayName("跳转型(支付宝)登录授权: redirectUri 指向网关透传路径并带端标识")
    void alipayLoginRedirectToGatewayTransit() {
        stubEnabled("alipay");
        when(platformUrlConfigService.getUrlConfig()).thenReturn(fullUrlConfig());
        when(alipaySocialAuthRequestFactory.create(any())).thenReturn(socialAuthRequest);
        when(socialAuthRequest.authorize(any())).thenReturn("https://openauth.example.com/authorize");

        String url = socialLoginService.generateAuthorizeUrl("alipay", "admin", "LOGIN", null);

        assertEquals("https://openauth.example.com/authorize", url);
        // source 段由 JustAuth 层拼装, 服务层只到 client 段为止
        assertRedirectUri(GATEWAY_BASE + "/auth/oauth-callback/admin");
    }

    @Test
    @DisplayName("跳转型(支付宝)绑定授权: 商户端走绑定透传路径")
    void alipayBindRedirectToGatewayTransit() {
        stubEnabled("alipay");
        when(platformUrlConfigService.getUrlConfig()).thenReturn(fullUrlConfig());
        when(alipaySocialAuthRequestFactory.create(any())).thenReturn(socialAuthRequest);

        socialLoginService.generateAuthorizeUrl("alipay", "merchant", "BIND", null);

        assertRedirectUri(GATEWAY_BASE + "/auth/social-bind-callback/merchant");
    }

    @Test
    @DisplayName("跳转型(公众号)登录授权: 同样走网关透传路径")
    void weChatMpLoginRedirectToGatewayTransit() {
        stubEnabled("weChat");
        when(platformUrlConfigService.getUrlConfig()).thenReturn(fullUrlConfig());
        when(platformWechatMpAuthConfigService.getWechatMpAuthConfig()).thenReturn(mpConfig());
        when(socialAuthRequestFactory.create(any(SocialSourceEnum.class), any(SocialAuthConfig.class))).thenReturn(socialAuthRequest);

        socialLoginService.generateAuthorizeUrl("weChat", "admin", "LOGIN", null);

        assertRedirectUriOnStandardFactory(GATEWAY_BASE + "/auth/oauth-callback/admin");
    }

    @Test
    @DisplayName("网关地址未配置: 跳转型授权生成直接报错")
    void gatewayMissingFailsFast() {
        stubEnabled("alipay");
        when(platformUrlConfigService.getUrlConfig()).thenReturn(new PlatformUrlConfig()
                .setAdminBaseUrl(ADMIN_BASE)
                .setMerchantBaseUrl(MERCHANT_BASE));

        OperationFailException exception = assertThrows(OperationFailException.class,
                () -> socialLoginService.generateAuthorizeUrl("alipay", "admin", "LOGIN", null));
        assertTrue(exception.getMessage().contains("gatewayEndpointNotConfigured"));
    }

    @Test
    @DisplayName("标准型(钉钉)登录授权: 沿用各端域名, 不依赖网关配置")
    void standardSourceKeepsClientDomain() {
        stubEnabled("dingTalk");
        // 网关未配置也不影响标准型
        when(platformUrlConfigService.getUrlConfig()).thenReturn(new PlatformUrlConfig()
                .setAdminBaseUrl(ADMIN_BASE)
                .setMerchantBaseUrl(MERCHANT_BASE));
        when(socialAuthRequestFactory.create(any(SocialSourceEnum.class), any(SocialAuthConfig.class))).thenReturn(socialAuthRequest);

        socialLoginService.generateAuthorizeUrl("dingTalk", "admin", "LOGIN", null);

        assertRedirectUriOnStandardFactory(ADMIN_BASE + "/auth/oauth-callback");
    }

    @Test
    @DisplayName("透传目标: 支付宝运营端登录回跳原回调页")
    void transitTargetAdminLogin() {
        when(platformUrlConfigService.getUrlConfig()).thenReturn(fullUrlConfig());

        String target = socialLoginService.resolveTransitTarget("alipay", "admin", "login");

        assertEquals(ADMIN_BASE + "/auth/oauth-callback/alipay", target);
    }

    @Test
    @DisplayName("透传目标: 支付宝商户端绑定回跳原绑定回调页")
    void transitTargetMerchantBind() {
        when(platformUrlConfigService.getUrlConfig()).thenReturn(fullUrlConfig());

        String target = socialLoginService.resolveTransitTarget("alipay", "merchant", "bind");

        assertEquals(MERCHANT_BASE + "/auth/social-bind-callback/alipay", target);
    }

    @Test
    @DisplayName("透传目标: 标准型平台不在透传范围内")
    void transitTargetRejectsStandardSource() {
        OperationFailException exception = assertThrows(OperationFailException.class,
                () -> socialLoginService.resolveTransitTarget("dingTalk", "admin", "login"));
        assertTrue(exception.getMessage().contains("unsupportedSource"));
    }

    @Test
    @DisplayName("透传目标: 非法端编码被拒绝")
    void transitTargetRejectsUnknownClient() {
        OperationFailException exception = assertThrows(OperationFailException.class,
                () -> socialLoginService.resolveTransitTarget("alipay", "agent", "login"));
        assertTrue(exception.getMessage().contains("unsupportedClient"));
    }

    @Test
    @DisplayName("透传目标: 目标端地址未配置时报对应错误")
    void transitTargetFailsWhenEndpointMissing() {
        when(platformUrlConfigService.getUrlConfig()).thenReturn(new PlatformUrlConfig()
                .setMerchantBaseUrl(MERCHANT_BASE)
                .setPaymentGatewayBaseUrl(GATEWAY_BASE));

        OperationFailException exception = assertThrows(OperationFailException.class,
                () -> socialLoginService.resolveTransitTarget("alipay", "admin", "login"));
        assertTrue(exception.getMessage().contains("adminEndpointNotConfigured"));
    }

    // ==================== 公众号扫码绑定(跨设备) ====================

    /// 公众号扫码票据上下文桩
    private SocialBindScanStore.BindScanContext scanContext(Long userId) {
        return new SocialBindScanStore.BindScanContext()
                .setUserId(userId)
                .setClientCode("admin")
                .setSource("weChat")
                .setStatus(SocialBindScanStore.STATUS_WAITING);
    }

    /// 登录用户信息桩(clientCode=admin)
    private UserInfoResult adminUserInfo(Long userId) {
        return new UserInfoResult().setId(userId).setClientCode("admin");
    }

    @Test
    @DisplayName("扫码绑定: 非公众号平台被拒绝(其余平台 PC 可直接发起)")
    void bindScanRejectsNonWechatPlatform() {
        OperationFailException exception = assertThrows(OperationFailException.class,
                () -> socialLoginService.createBindScanTicket("dingTalk", "admin"));
        assertTrue(exception.getMessage().contains("scanNotSupported"));
    }

    @Test
    @DisplayName("扫码绑定: 跨端发起被拒绝(用户不属于该身份域)")
    void bindScanRejectsClientMismatch() {
        try (MockedStatic<SecurityUtil> mocked = mockStatic(SecurityUtil.class)) {
            mocked.when(SecurityUtil::getUserId).thenReturn(1L);
            when(userQueryService.findById(1L)).thenReturn(new UserInfoResult().setId(1L).setClientCode("merchant"));

            OperationFailException exception = assertThrows(OperationFailException.class,
                    () -> socialLoginService.createBindScanTicket("weChat", "admin"));
            assertTrue(exception.getMessage().contains("clientMismatch"));
        }
    }

    @Test
    @DisplayName("扫码绑定: 生成二维码返回票据与 H5 发起页地址")
    void bindScanCreateReturnsQrUrl() {
        stubEnabled("weChat");
        when(platformUrlConfigService.getUrlConfig()).thenReturn(fullUrlConfig());
        when(platformWechatMpAuthConfigService.getWechatMpAuthConfig()).thenReturn(mpConfig());
        try (MockedStatic<SecurityUtil> mocked = mockStatic(SecurityUtil.class)) {
            mocked.when(SecurityUtil::getUserId).thenReturn(1L);
            when(userQueryService.findById(1L)).thenReturn(adminUserInfo(1L));

            SocialBindScanQrResult result = socialLoginService.createBindScanTicket("weChat", "admin");

            assertNotNull(result.getTicket());
            assertEquals(GATEWAY_BASE + "/social/bind-scan?ticket=" + result.getTicket(), result.getQrUrl());
            verify(socialBindScanStore).saveWaiting(result.getTicket(), 1L, "admin", "weChat");
        }
    }

    @Test
    @DisplayName("扫码绑定授权地址: 票据无效报错")
    void bindScanAuthUrlRejectsInvalidTicket() {
        when(socialBindScanStore.get("bad-ticket")).thenReturn(null);

        OperationFailException exception = assertThrows(OperationFailException.class,
                () -> socialLoginService.buildBindScanAuthUrl("bad-ticket"));
        assertTrue(exception.getMessage().contains("scanTicketInvalid"));
    }

    @Test
    @DisplayName("扫码绑定授权地址: 回调走网关独立回调路径并保存 state 映射")
    void bindScanAuthUrlUsesScanCallbackPath() {
        stubEnabled("weChat");
        when(socialBindScanStore.get("ticket-1")).thenReturn(scanContext(1L));
        when(platformUrlConfigService.getUrlConfig()).thenReturn(fullUrlConfig());
        when(platformWechatMpAuthConfigService.getWechatMpAuthConfig()).thenReturn(mpConfig());
        when(socialAuthRequestFactory.create(any(SocialSourceEnum.class), any(SocialAuthConfig.class))).thenReturn(socialAuthRequest);
        when(socialAuthRequest.authorize(anyString())).thenReturn("https://open.weixin.example.com/authorize");

        String url = socialLoginService.buildBindScanAuthUrl("ticket-1");

        assertEquals("https://open.weixin.example.com/authorize", url);
        // state 映射与回调地址: 不经透传页, 直接落网关域名的扫码绑定回调页
        verify(socialBindScanStore).saveState(anyString(), eq("ticket-1"));
        ArgumentCaptor<SocialAuthConfig> captor = ArgumentCaptor.forClass(SocialAuthConfig.class);
        verify(socialAuthRequestFactory).create(any(SocialSourceEnum.class), captor.capture());
        assertEquals(GATEWAY_BASE + "/social/bind-scan-callback", captor.getValue().getRedirectUri());
    }

    @Test
    @DisplayName("扫码绑定兑换: state 映射无效报错")
    void bindScanExchangeRejectsInvalidState() {
        when(socialBindScanStore.consumeTicketByState("bad-state")).thenReturn(null);

        OperationFailException exception = assertThrows(OperationFailException.class,
                () -> socialLoginService.exchangeBindScan("code-1", "bad-state"));
        assertTrue(exception.getMessage().contains("scanTicketInvalid"));
    }

    @Test
    @DisplayName("扫码绑定兑换: 凭票据绑定对应用户并标记成功")
    void bindScanExchangeBindsTicketUser() {
        stubEnabled("weChat");
        when(socialBindScanStore.consumeTicketByState("state-1")).thenReturn("ticket-1");
        when(socialBindScanStore.get("ticket-1")).thenReturn(scanContext(1L));
        when(platformUrlConfigService.getUrlConfig()).thenReturn(fullUrlConfig());
        when(platformWechatMpAuthConfigService.getWechatMpAuthConfig()).thenReturn(mpConfig());
        when(socialAuthRequestFactory.create(any(SocialSourceEnum.class), any(SocialAuthConfig.class))).thenReturn(socialAuthRequest);
        AuthUser authUser = new AuthUser().setUuid("openid-1").setSource("weChat");
        when(socialAuthRequest.login(any())).thenReturn(authUser);

        SocialExchangeResult result = socialLoginService.exchangeBindScan("code-1", "state-1");

        assertEquals("bind_success", result.getResult());
        // 手机端无登录态, 绑定用户由票据决定
        verify(socialBindStore).saveBind(1L, "admin", authUser);
        verify(socialBindScanStore).markSuccess(eq("ticket-1"), any(SocialBindScanStore.BindScanContext.class));
    }

    @Test
    @DisplayName("扫码绑定状态: 他人票据按不存在返回(防探测)")
    void bindScanStatusHidesForeignTicket() {
        when(socialBindScanStore.get("ticket-1")).thenReturn(scanContext(2L));
        try (MockedStatic<SecurityUtil> mocked = mockStatic(SecurityUtil.class)) {
            mocked.when(SecurityUtil::getUserId).thenReturn(1L);

            SocialBindScanStatusResult result = socialLoginService.queryBindScanStatus("ticket-1");

            assertEquals(SocialBindScanStatusResult.NOT_EXIST, result.getStatus());
        }
    }

    @Test
    @DisplayName("扫码绑定状态: 本人票据返回绑定进度")
    void bindScanStatusReturnsProgress() {
        SocialBindScanStore.BindScanContext context = scanContext(1L).setStatus(SocialBindScanStore.STATUS_SUCCESS);
        when(socialBindScanStore.get("ticket-1")).thenReturn(context);
        try (MockedStatic<SecurityUtil> mocked = mockStatic(SecurityUtil.class)) {
            mocked.when(SecurityUtil::getUserId).thenReturn(1L);

            SocialBindScanStatusResult result = socialLoginService.queryBindScanStatus("ticket-1");

            assertEquals(SocialBindScanStore.STATUS_SUCCESS, result.getStatus());
        }
    }

    // ==================== 公众号扫码登录(跨设备) ====================

    /// 公众号扫码登录票据上下文桩(userId null=尚未授权)
    private SocialLoginScanStore.LoginScanContext loginScanContext(Long userId, String status) {
        return new SocialLoginScanStore.LoginScanContext()
                .setUserId(userId)
                .setClientCode("admin")
                .setSource("weChat")
                .setStatus(status);
    }

    @Test
    @DisplayName("扫码登录: 非公众号平台被拒绝(其余平台 PC 可直接跳转授权)")
    void loginScanRejectsNonWechatPlatform() {
        OperationFailException exception = assertThrows(OperationFailException.class,
                () -> socialLoginService.createLoginScanTicket("dingTalk", "admin"));
        assertTrue(exception.getMessage().contains("scanNotSupported"));
    }

    @Test
    @DisplayName("扫码登录: 匿名生成二维码返回票据与 H5 发起页地址(不携带用户身份)")
    void loginScanCreateReturnsQrUrl() {
        stubEnabled("weChat");
        when(platformUrlConfigService.getUrlConfig()).thenReturn(fullUrlConfig());
        when(platformWechatMpAuthConfigService.getWechatMpAuthConfig()).thenReturn(mpConfig());

        SocialLoginScanQrResult result = socialLoginService.createLoginScanTicket("weChat", "admin");

        assertNotNull(result.getTicket());
        assertEquals(GATEWAY_BASE + "/social/login-scan?ticket=" + result.getTicket(), result.getQrUrl());
        verify(socialLoginScanStore).saveWaiting(result.getTicket(), "admin", "weChat");
    }

    @Test
    @DisplayName("扫码登录授权地址: 回调走网关独立回调路径并保存 state 映射")
    void loginScanAuthUrlUsesScanCallbackPath() {
        stubEnabled("weChat");
        when(socialLoginScanStore.get("ticket-1")).thenReturn(loginScanContext(null, SocialLoginScanStore.STATUS_WAITING));
        when(platformUrlConfigService.getUrlConfig()).thenReturn(fullUrlConfig());
        when(platformWechatMpAuthConfigService.getWechatMpAuthConfig()).thenReturn(mpConfig());
        when(socialAuthRequestFactory.create(any(SocialSourceEnum.class), any(SocialAuthConfig.class))).thenReturn(socialAuthRequest);
        when(socialAuthRequest.authorize(anyString())).thenReturn("https://open.weixin.example.com/authorize");

        String url = socialLoginService.buildLoginScanAuthUrl("ticket-1");

        assertEquals("https://open.weixin.example.com/authorize", url);
        // 与扫码绑定回调路径分离, 两类票据互不干扰
        verify(socialLoginScanStore).saveState(anyString(), eq("ticket-1"));
        ArgumentCaptor<SocialAuthConfig> captor = ArgumentCaptor.forClass(SocialAuthConfig.class);
        verify(socialAuthRequestFactory).create(any(SocialSourceEnum.class), captor.capture());
        assertEquals(GATEWAY_BASE + "/social/login-scan-callback", captor.getValue().getRedirectUri());
    }

    @Test
    @DisplayName("扫码登录兑换: state 映射无效报错")
    void loginScanExchangeRejectsInvalidState() {
        when(socialLoginScanStore.consumeTicketByState("bad-state")).thenReturn(null);

        OperationFailException exception = assertThrows(OperationFailException.class,
                () -> socialLoginService.exchangeLoginScan("code-1", "bad-state", null, null));
        assertTrue(exception.getMessage().contains("scanTicketInvalid"));
    }

    @Test
    @DisplayName("扫码登录兑换: 授权微信未绑定账号标记 not_bound(不自动注册)")
    void loginScanExchangeMarksNotBound() {
        stubEnabled("weChat");
        when(socialLoginScanStore.consumeTicketByState("state-1")).thenReturn("ticket-1");
        when(socialLoginScanStore.get("ticket-1")).thenReturn(loginScanContext(null, SocialLoginScanStore.STATUS_WAITING));
        when(platformUrlConfigService.getUrlConfig()).thenReturn(fullUrlConfig());
        when(platformWechatMpAuthConfigService.getWechatMpAuthConfig()).thenReturn(mpConfig());
        when(socialAuthRequestFactory.create(any(SocialSourceEnum.class), any(SocialAuthConfig.class))).thenReturn(socialAuthRequest);
        when(socialAuthRequest.login(any())).thenReturn(new AuthUser().setUuid("openid-x").setSource("weChat"));
        when(socialBindStore.findUserIdBySourceAndOpenId("weChat", "openid-x")).thenReturn(Optional.empty());

        SocialExchangeResult result = socialLoginService.exchangeLoginScan("code-1", "state-1", null, null);

        assertEquals("unbind", result.getError());
        verify(socialLoginScanStore).markNotBound(eq("ticket-1"), any(SocialLoginScanStore.LoginScanContext.class));
    }

    @Test
    @DisplayName("扫码登录兑换: 已绑定用户通过校验后标记 success 待 PC 轮询签发")
    void loginScanExchangeMarksSuccess() {
        stubEnabled("weChat");
        when(socialLoginScanStore.consumeTicketByState("state-1")).thenReturn("ticket-1");
        when(socialLoginScanStore.get("ticket-1")).thenReturn(loginScanContext(null, SocialLoginScanStore.STATUS_WAITING));
        when(platformUrlConfigService.getUrlConfig()).thenReturn(fullUrlConfig());
        when(platformWechatMpAuthConfigService.getWechatMpAuthConfig()).thenReturn(mpConfig());
        when(socialAuthRequestFactory.create(any(SocialSourceEnum.class), any(SocialAuthConfig.class))).thenReturn(socialAuthRequest);
        when(socialAuthRequest.login(any())).thenReturn(new AuthUser().setUuid("openid-1").setSource("weChat"));
        when(socialBindStore.findUserIdBySourceAndOpenId("weChat", "openid-1")).thenReturn(Optional.of(1L));
        // 用户校验链(身份域归属): 归属 admin 通过, 状态检查链为空
        when(userQueryService.findById(1L)).thenReturn(adminUserInfo(1L));

        SocialExchangeResult result = socialLoginService.exchangeLoginScan("code-1", "state-1", null, null);

        assertEquals("login_ready", result.getResult());
        verify(socialLoginScanStore).markSuccess(eq("ticket-1"), any(SocialLoginScanStore.LoginScanContext.class));
    }

    @Test
    @DisplayName("扫码登录状态: waiting/not_bound 直接透出")
    void loginScanStatusPassthroughNonTerminal() {
        when(socialLoginScanStore.get("ticket-1")).thenReturn(loginScanContext(null, SocialLoginScanStore.STATUS_WAITING));
        assertEquals(SocialLoginScanStore.STATUS_WAITING,
                socialLoginService.queryLoginScanStatus("ticket-1", null, null).getStatus());

        when(socialLoginScanStore.get("ticket-2")).thenReturn(loginScanContext(null, SocialLoginScanStore.STATUS_NOT_BOUND));
        assertEquals(SocialLoginScanStore.STATUS_NOT_BOUND,
                socialLoginService.queryLoginScanStatus("ticket-2", null, null).getStatus());
    }

    @Test
    @DisplayName("扫码登录状态: cancelled 透出(手机端确认页已取消)")
    void loginScanStatusPassthroughCancelled() {
        when(socialLoginScanStore.get("ticket-1")).thenReturn(loginScanContext(null, SocialLoginScanStore.STATUS_CANCELLED));

        assertEquals(SocialLoginScanStatusResult.CANCELLED,
                socialLoginService.queryLoginScanStatus("ticket-1", null, null).getStatus());
    }

    @Test
    @DisplayName("取消扫码登录: waiting 状态标记 cancelled")
    void loginScanCancelMarksCancelled() {
        when(socialLoginScanStore.get("ticket-1")).thenReturn(loginScanContext(null, SocialLoginScanStore.STATUS_WAITING));

        socialLoginService.cancelLoginScan("ticket-1");

        verify(socialLoginScanStore).markCancelled(eq("ticket-1"), any(SocialLoginScanStore.LoginScanContext.class));
    }

    @Test
    @DisplayName("取消扫码登录: 票据不存在或非 waiting 时空操作(不覆盖已完成状态)")
    void loginScanCancelNoOpOnMissingOrNonWaiting() {
        socialLoginService.cancelLoginScan("missing-ticket");
        socialLoginService.cancelLoginScan(null);
        when(socialLoginScanStore.get("ticket-1")).thenReturn(loginScanContext(1L, SocialLoginScanStore.STATUS_SUCCESS));

        socialLoginService.cancelLoginScan("ticket-1");

        verify(socialLoginScanStore, never()).markCancelled(anyString(), any(SocialLoginScanStore.LoginScanContext.class));
    }

    @Test
    @DisplayName("扫码登录状态: success 一次性消费票据并在轮询上下文签发登录令牌")
    void loginScanStatusIssuesTokenOnce() {
        SocialLoginScanStore.LoginScanContext context = loginScanContext(1L, SocialLoginScanStore.STATUS_SUCCESS);
        when(socialLoginScanStore.get("ticket-1")).thenReturn(context);
        when(socialLoginScanStore.consume("ticket-1")).thenReturn(context);
        when(socialLoginHandler.login(eq(1L), eq("admin"), eq("weChat"), any(), any())).thenReturn("token-9");

        SocialLoginScanStatusResult result = socialLoginService.queryLoginScanStatus("ticket-1",
                mock(HttpServletRequest.class), mock(HttpServletResponse.class));

        assertEquals(SocialLoginScanStore.STATUS_SUCCESS, result.getStatus());
        assertEquals("token-9", result.getToken());
    }

    /// 从支付宝授权请求工厂捕获 SocialAuthConfig 并断言 redirectUri
    private void assertRedirectUri(String expected) {
        ArgumentCaptor<SocialAuthConfig> captor = ArgumentCaptor.forClass(SocialAuthConfig.class);
        verify(alipaySocialAuthRequestFactory).create(captor.capture());
        assertEquals(expected, captor.getValue().getRedirectUri());
    }

    /// 从标准 JustAuth 工厂捕获 SocialAuthConfig 并断言 redirectUri
    private void assertRedirectUriOnStandardFactory(String expected) {
        ArgumentCaptor<SocialAuthConfig> captor = ArgumentCaptor.forClass(SocialAuthConfig.class);
        verify(socialAuthRequestFactory).create(any(SocialSourceEnum.class), captor.capture());
        assertEquals(expected, captor.getValue().getRedirectUri());
    }
}
