package cn.daxpay.open.platform.iam.service.social;

import java.util.ArrayList;
import java.util.List;

import cn.daxpay.open.platform.capability.auth.authentication.UserInfoStatusCheck;
import cn.daxpay.open.platform.capability.social.auth.SocialAuthRequestFactory;
import cn.daxpay.open.platform.capability.social.justauth.SocialAuthConfig;
import cn.daxpay.open.platform.capability.social.justauth.SocialSourceEnum;
import cn.daxpay.open.platform.capability.social.justauth.request.SocialAuthRequest;
import cn.daxpay.open.platform.common.config.properties.PlatformStarterProperties;
import cn.daxpay.open.platform.core.exception.operation.OperationFailException;
import cn.daxpay.open.platform.iam.entity.social.SocialLoginConfig;
import cn.daxpay.open.platform.iam.service.social.other.AlipaySocialAuthRequestFactory;
import cn.daxpay.open.platform.iam.service.user.UserQueryService;
import cn.daxpay.open.platform.system.entity.config.platform.auth.PlatformWechatMpAuthConfig;
import cn.daxpay.open.platform.system.entity.config.platform.infra.PlatformUrlConfig;
import cn.daxpay.open.platform.system.service.config.auth.PlatformWechatMpAuthConfigService;
import cn.daxpay.open.platform.system.service.config.infra.PlatformUrlConfigService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/// # 社交登录回调透传测试(标准 OAuth 编排)
///
/// 覆盖 [SocialLoginService] 跳转型平台(支付宝/公众号)回调改走支付网关域名后的拼装语义:
/// - 跳转型 generateAuthorizeUrl 的 redirectUri 指向网关透传路径(带 client 段), 网关未配置时报错;
/// - 标准型平台沿用各端自身域名, 不受网关配置影响;
/// - [SocialLoginService#resolveTransitTarget] 的白名单校验与目标端回调地址拼装.
/// 扫码绑定/扫码登录的编排测试见 social.scan 子包的 SocialBindScanServiceTest/SocialLoginScanServiceTest.
///
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
    private PlatformStarterProperties platformStarterProperties;

    /// 状态检查链注入空实现(validateSocialLoginUser 遍历该链, 空列表即通过)
    private final List<UserInfoStatusCheck> userInfoStatusChecks = new ArrayList<>();

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

    /// 被测对象: 真实支撑(不 mock 授权链路原语, 保持 redirectUri 拼装断言语义) + 编排 service
    private SocialAuthSupport socialAuthSupport;

    private SocialLoginService socialLoginService;

    @BeforeEach
    void setUp() {
        socialAuthSupport = new SocialAuthSupport(socialAuthRequestFactory, alipaySocialAuthRequestFactory,
                socialLoginConfigService, platformUrlConfigService, platformWechatMpAuthConfigService,
                userQueryService, platformStarterProperties, userInfoStatusChecks);
        socialLoginService = new SocialLoginService(socialAuthSupport, socialBindStore, socialLoginHandler,
                socialLoginConfigService, platformUrlConfigService, userQueryService);
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
