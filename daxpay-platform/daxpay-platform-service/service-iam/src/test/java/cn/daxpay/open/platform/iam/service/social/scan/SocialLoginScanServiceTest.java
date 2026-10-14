package cn.daxpay.open.platform.iam.service.social.scan;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import cn.daxpay.open.platform.capability.auth.authentication.UserInfoStatusCheck;
import cn.daxpay.open.platform.capability.social.auth.SocialAuthRequestFactory;
import cn.daxpay.open.platform.capability.social.justauth.SocialAuthConfig;
import cn.daxpay.open.platform.capability.social.justauth.SocialSourceEnum;
import cn.daxpay.open.platform.capability.social.justauth.model.AuthUser;
import cn.daxpay.open.platform.capability.social.justauth.request.SocialAuthRequest;
import cn.daxpay.open.platform.common.config.properties.PlatformStarterProperties;
import cn.daxpay.open.platform.core.exception.operation.OperationFailException;
import cn.daxpay.open.platform.iam.entity.social.SocialLoginConfig;
import cn.daxpay.open.platform.iam.result.social.SocialExchangeResult;
import cn.daxpay.open.platform.iam.result.social.SocialLoginScanQrResult;
import cn.daxpay.open.platform.iam.result.social.SocialLoginScanStatusResult;
import cn.daxpay.open.platform.iam.result.user.UserInfoResult;
import cn.daxpay.open.platform.iam.service.social.IamSocialLoginHandler;
import cn.daxpay.open.platform.iam.service.social.IamUserSocialBindStore;
import cn.daxpay.open.platform.iam.service.social.SocialAuthSupport;
import cn.daxpay.open.platform.iam.service.social.SocialLoginConfigService;
import cn.daxpay.open.platform.iam.service.user.UserQueryService;
import cn.daxpay.open.platform.system.entity.config.platform.auth.PlatformWechatMpAuthConfig;
import cn.daxpay.open.platform.system.entity.config.platform.infra.PlatformUrlConfig;
import cn.daxpay.open.platform.system.service.config.auth.PlatformWechatMpAuthConfigService;
import cn.daxpay.open.platform.system.service.config.infra.PlatformUrlConfigService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/// # 公众号扫码登录(跨设备)编排测试
///
/// 覆盖 [SocialLoginScanService] 匿名票据生成/未绑定不自动注册/success 一次性签发登录令牌:
/// - 票据生成: 仅公众号可用, 二维码地址指向网关域名 H5 发起页(不携带用户身份);
/// - 授权地址: 回调走网关独立回调路径, 与扫码绑定回调路径分离;
/// - 兑换: 未绑定标记 not_bound(不自动注册), 已绑定过校验链后标记 success;
/// - 状态轮询: waiting/not_bound/cancelled 透出, success 一次性消费并在轮询上下文签发令牌;
/// - 取消: 仅 waiting 可取消, 其余空操作.
///
@ExtendWith(MockitoExtension.class)
class SocialLoginScanServiceTest {

    private static final String ADMIN_BASE = "https://admin.example.com";

    private static final String MERCHANT_BASE = "https://mch.example.com";

    private static final String GATEWAY_BASE = "https://gw.example.com";

    @Mock
    private SocialAuthRequestFactory socialAuthRequestFactory;

    @Mock
    private SocialLoginConfigService socialLoginConfigService;

    @Mock
    private PlatformUrlConfigService platformUrlConfigService;

    @Mock
    private PlatformWechatMpAuthConfigService platformWechatMpAuthConfigService;

    @Mock
    private PlatformStarterProperties platformStarterProperties;

    /// 状态检查链注入空实现(validateSocialLoginUser 遍历该链, 空列表即通过)
    private final List<UserInfoStatusCheck> userInfoStatusChecks = new ArrayList<>();

    @Mock
    private UserQueryService userQueryService;

    @Mock
    private IamUserSocialBindStore socialBindStore;

    @Mock
    private SocialLoginScanStore socialLoginScanStore;

    @Mock
    private IamSocialLoginHandler socialLoginHandler;

    @Mock
    private SocialAuthRequest socialAuthRequest;

    /// 被测对象: 真实支撑(不 mock 授权链路原语, 保持 redirectUri 拼装断言语义) + 编排 service
    private SocialAuthSupport socialAuthSupport;

    private SocialLoginScanService loginScanService;

    @BeforeEach
    void setUp() {
        socialAuthSupport = new SocialAuthSupport(socialAuthRequestFactory, null,
                socialLoginConfigService, platformUrlConfigService, platformWechatMpAuthConfigService,
                userQueryService, platformStarterProperties, userInfoStatusChecks);
        loginScanService = new SocialLoginScanService(socialAuthSupport, socialLoginScanStore, socialBindStore,
                socialLoginHandler);
    }

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

    /// 公众号扫码登录票据上下文桩(userId null=尚未授权)
    private SocialLoginScanStore.LoginScanContext loginScanContext(Long userId, String status) {
        return new SocialLoginScanStore.LoginScanContext()
                .setUserId(userId)
                .setClientCode("admin")
                .setSource("weChat")
                .setStatus(status);
    }

    /// 登录用户信息桩(clientCode=admin)
    private UserInfoResult adminUserInfo(Long userId) {
        return new UserInfoResult().setId(userId).setClientCode("admin");
    }

    @Test
    @DisplayName("扫码登录: 非公众号平台被拒绝(其余平台 PC 可直接跳转授权)")
    void loginScanRejectsNonWechatPlatform() {
        OperationFailException exception = assertThrows(OperationFailException.class,
                () -> loginScanService.createLoginScanTicket("dingTalk", "admin"));
        assertTrue(exception.getMessage().contains("scanNotSupported"));
    }

    @Test
    @DisplayName("扫码登录: 匿名生成二维码返回票据与 H5 发起页地址(不携带用户身份)")
    void loginScanCreateReturnsQrUrl() {
        stubEnabled("weChat");
        when(platformUrlConfigService.getUrlConfig()).thenReturn(fullUrlConfig());
        when(platformWechatMpAuthConfigService.getWechatMpAuthConfig()).thenReturn(mpConfig());

        SocialLoginScanQrResult result = loginScanService.createLoginScanTicket("weChat", "admin");

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

        String url = loginScanService.buildLoginScanAuthUrl("ticket-1");

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
                () -> loginScanService.exchangeLoginScan("code-1", "bad-state", null, null));
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

        SocialExchangeResult result = loginScanService.exchangeLoginScan("code-1", "state-1", null, null);

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

        SocialExchangeResult result = loginScanService.exchangeLoginScan("code-1", "state-1", null, null);

        assertEquals("login_ready", result.getResult());
        verify(socialLoginScanStore).markSuccess(eq("ticket-1"), any(SocialLoginScanStore.LoginScanContext.class));
    }

    @Test
    @DisplayName("扫码登录状态: waiting/not_bound 直接透出")
    void loginScanStatusPassthroughNonTerminal() {
        when(socialLoginScanStore.get("ticket-1")).thenReturn(loginScanContext(null, SocialLoginScanStore.STATUS_WAITING));
        assertEquals(SocialLoginScanStore.STATUS_WAITING,
                loginScanService.queryLoginScanStatus("ticket-1", null, null).getStatus());

        when(socialLoginScanStore.get("ticket-2")).thenReturn(loginScanContext(null, SocialLoginScanStore.STATUS_NOT_BOUND));
        assertEquals(SocialLoginScanStore.STATUS_NOT_BOUND,
                loginScanService.queryLoginScanStatus("ticket-2", null, null).getStatus());
    }

    @Test
    @DisplayName("扫码登录状态: cancelled 透出(手机端确认页已取消)")
    void loginScanStatusPassthroughCancelled() {
        when(socialLoginScanStore.get("ticket-1")).thenReturn(loginScanContext(null, SocialLoginScanStore.STATUS_CANCELLED));

        assertEquals(SocialLoginScanStatusResult.CANCELLED,
                loginScanService.queryLoginScanStatus("ticket-1", null, null).getStatus());
    }

    @Test
    @DisplayName("取消扫码登录: waiting 状态标记 cancelled")
    void loginScanCancelMarksCancelled() {
        when(socialLoginScanStore.get("ticket-1")).thenReturn(loginScanContext(null, SocialLoginScanStore.STATUS_WAITING));

        loginScanService.cancelLoginScan("ticket-1");

        verify(socialLoginScanStore).markCancelled(eq("ticket-1"), any(SocialLoginScanStore.LoginScanContext.class));
    }

    @Test
    @DisplayName("取消扫码登录: 票据不存在或非 waiting 时空操作(不覆盖已完成状态)")
    void loginScanCancelNoOpOnMissingOrNonWaiting() {
        loginScanService.cancelLoginScan("missing-ticket");
        loginScanService.cancelLoginScan(null);
        when(socialLoginScanStore.get("ticket-1")).thenReturn(loginScanContext(1L, SocialLoginScanStore.STATUS_SUCCESS));

        loginScanService.cancelLoginScan("ticket-1");

        verify(socialLoginScanStore, never()).markCancelled(anyString(), any(SocialLoginScanStore.LoginScanContext.class));
    }

    @Test
    @DisplayName("扫码登录状态: success 一次性消费票据并在轮询上下文签发登录令牌")
    void loginScanStatusIssuesTokenOnce() {
        SocialLoginScanStore.LoginScanContext context = loginScanContext(1L, SocialLoginScanStore.STATUS_SUCCESS);
        when(socialLoginScanStore.get("ticket-1")).thenReturn(context);
        when(socialLoginScanStore.consume("ticket-1")).thenReturn(context);
        when(socialLoginHandler.login(eq(1L), eq("admin"), eq("weChat"), any(), any())).thenReturn("token-9");

        SocialLoginScanStatusResult result = loginScanService.queryLoginScanStatus("ticket-1",
                mock(HttpServletRequest.class), mock(HttpServletResponse.class));

        assertEquals(SocialLoginScanStore.STATUS_SUCCESS, result.getStatus());
        assertEquals("token-9", result.getToken());
    }
}
