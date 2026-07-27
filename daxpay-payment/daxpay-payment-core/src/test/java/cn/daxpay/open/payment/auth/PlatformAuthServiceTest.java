package cn.daxpay.open.payment.auth;

import cn.daxpay.open.payment.unipay.param.assist.AuthCodeParam;
import cn.daxpay.open.payment.unipay.result.assist.AuthResult;
import cn.daxpay.open.payment.unipay.result.assist.AuthUrlResult;
import cn.daxpay.open.platform.capability.alipay.auth.config.AlipayAuthConfig;
import cn.daxpay.open.platform.capability.alipay.auth.result.AlipayAuthResult;
import cn.daxpay.open.platform.capability.alipay.auth.service.AlipayAuthCapability;
import cn.daxpay.open.platform.capability.douyin.auth.result.DouyinAuthResult;
import cn.daxpay.open.platform.capability.douyin.auth.service.DouyinH5AuthService;
import cn.daxpay.open.platform.capability.wechat.auth.result.WechatAuthResult;
import cn.daxpay.open.platform.capability.wechat.auth.result.WechatAuthUrlResult;
import cn.daxpay.open.platform.capability.wechat.auth.service.WechatMpAuthService;
import cn.daxpay.open.platform.core.enums.unipay.ChannelAuthStatusEnum;
import cn.daxpay.open.platform.core.exception.BizInfoException;
import cn.daxpay.open.platform.system.entity.config.platform.auth.PlatformDouyinH5AuthConfig;
import cn.daxpay.open.platform.system.entity.config.platform.auth.PlatformWechatMpAuthConfig;
import cn.daxpay.open.platform.system.entity.config.platform.infra.PlatformUrlConfig;
import cn.daxpay.open.platform.system.service.config.auth.PlatformAlipayAuthConfigService;
import cn.daxpay.open.platform.system.service.config.auth.PlatformDouyinH5AuthConfigService;
import cn.daxpay.open.platform.system.service.config.auth.PlatformWechatMpAuthConfigService;
import cn.daxpay.open.platform.system.service.config.infra.PlatformUrlConfigService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/// # 平台级认证服务测试
///
/// 锁定 [PlatformAuthService] 三通道(支付宝 / 微信公众号 / 抖音 H5)的 generateUrl / authXxx 行为,
/// 供后续重构(PlatformAuthProvider 拆分)回归验证。
/// 覆盖点:
/// - generateUrl: 配置缺失异常 / gatewayBase 未配置异常 / 正常路径(session.source 正确、authToken 透传 state 一致)
/// - authXxx: 配置缺失异常 / 用户标识为空异常 / 正常路径(openId/userId 映射、fillReturnPath、writeResultByQueryCode)
@ExtendWith(MockitoExtension.class)
class PlatformAuthServiceTest {

    @Mock
    private AuthSessionStore authSessionStore;
    @Mock
    private PlatformAlipayAuthConfigService platformAlipayAuthConfigService;
    @Mock
    private PlatformWechatMpAuthConfigService platformWechatMpAuthConfigService;
    @Mock
    private PlatformDouyinH5AuthConfigService platformDouyinH5AuthConfigService;
    @Mock
    private PlatformUrlConfigService platformUrlConfigService;
    @Mock
    private AlipayAuthCapability alipayAuthCapability;
    @Mock
    private WechatMpAuthService wechatMpAuthService;
    @Mock
    private DouyinH5AuthService douyinH5AuthService;

    @InjectMocks
    private PlatformAuthService platformAuthService;

    private static final String GATEWAY_BASE = "https://gw.example.com";
    private static final String EXPECTED_AUTH_URL = "https://openauth.example.com/oauth?state=xxx";

    // ==================== generateAlipayAuthUrl ====================

    @Test
    @DisplayName("generateAlipayAuthUrl: 配置不完整抛 alipayNotConfigured")
    void generateAlipayAuthUrl_notConfigured_shouldThrow() {
        when(platformAlipayAuthConfigService.toCapabilityConfig()).thenReturn(new AlipayAuthConfig());
        when(alipayAuthCapability.isConfigured(any())).thenReturn(false);

        BizInfoException ex = assertThrows(BizInfoException.class,
                () -> platformAuthService.generateAlipayAuthUrl("/return"));

        assertEquals("error.social.alipayNotConfigured", ex.getMessageKey());
        // 已知债: alipay 路径先 saveSession 后 buildAlipayAuthUrl(校验在 build 内),
        // 配置校验失败时 session 已写入会残留至 TTL; wechat/douyin 是先校验后 save。
        // 阶段 2 PlatformAuthProvider 模板方法将统一为「先校验后 save」, 届时此用例可加 never().saveSession 断言。
    }

    @Test
    @DisplayName("generateAlipayAuthUrl: gatewayBase 未配置抛 gatewayUrlNotConfigured")
    void generateAlipayAuthUrl_gatewayBlank_shouldThrow() {
        when(platformAlipayAuthConfigService.toCapabilityConfig()).thenReturn(new AlipayAuthConfig());
        when(alipayAuthCapability.isConfigured(any())).thenReturn(true);
        when(platformUrlConfigService.getUrlConfig()).thenReturn(new PlatformUrlConfig());

        BizInfoException ex = assertThrows(BizInfoException.class,
                () -> platformAuthService.generateAlipayAuthUrl(null));

        assertEquals("error.common.gatewayUrlNotConfigured", ex.getMessageKey());
        // 同上: alipay 路径先 save 后校验, gatewayBase 校验失败时 session 已写入会残留。
    }

    @Test
    @DisplayName("generateAlipayAuthUrl: 正常路径透传 returnPath, session.source=platform_alipay, authToken 一致地写入 session 与 state")
    void generateAlipayAuthUrl_normal_shouldSaveSessionAndReturnUrl() {
        AlipayAuthConfig capabilityConfig = new AlipayAuthConfig();
        when(platformAlipayAuthConfigService.toCapabilityConfig()).thenReturn(capabilityConfig);
        when(alipayAuthCapability.isConfigured(capabilityConfig)).thenReturn(true);
        when(platformUrlConfigService.getUrlConfig())
                .thenReturn(new PlatformUrlConfig().setPaymentGatewayBaseUrl(GATEWAY_BASE + "/"));
        when(alipayAuthCapability.generateAuthUrl(eq(capabilityConfig), anyString(), anyString(), anyString(), eq(false)))
                .thenReturn(EXPECTED_AUTH_URL);

        AuthUrlResult result = platformAuthService.generateAlipayAuthUrl("/cashier/ORD/alipay");

        assertEquals(EXPECTED_AUTH_URL, result.getAuthUrl());
        assertNotNull(result.getQueryCode());

        // 验证 session 内容
        ArgumentCaptor<AuthSession> sessionCaptor = ArgumentCaptor.forClass(AuthSession.class);
        ArgumentCaptor<String> authTokenCaptor = ArgumentCaptor.forClass(String.class);
        verify(authSessionStore).saveSession(authTokenCaptor.capture(), sessionCaptor.capture());
        AuthSession session = sessionCaptor.getValue();
        assertEquals(AuthSession.SOURCE_PLATFORM_ALIPAY, session.getSource());
        assertEquals("/cashier/ORD/alipay", session.getReturnPath());
        assertEquals(result.getQueryCode(), session.getQueryCode());

        // 验证 authToken 透传一致性: saveSession 的 authToken == generateAuthUrl 的 state
        ArgumentCaptor<String> stateCaptor = ArgumentCaptor.forClass(String.class);
        verify(alipayAuthCapability).generateAuthUrl(eq(capabilityConfig), anyString(), anyString(), stateCaptor.capture(), eq(false));
        assertEquals(authTokenCaptor.getValue(), stateCaptor.getValue());

        verify(authSessionStore).saveWaitingResult(result.getQueryCode());
    }

    // ==================== generateWechatMpAuthUrl ====================

    @Test
    @DisplayName("generateWechatMpAuthUrl: appId/appSecret 未配置抛 wechatMpNotConfigured")
    void generateWechatMpAuthUrl_notConfigured_shouldThrow() {
        when(platformWechatMpAuthConfigService.getWechatMpAuthConfig())
                .thenReturn(new PlatformWechatMpAuthConfig());

        BizInfoException ex = assertThrows(BizInfoException.class,
                () -> platformAuthService.generateWechatMpAuthUrl(null));

        assertEquals("error.social.wechatMpNotConfigured", ex.getMessageKey());
        verify(authSessionStore, never()).saveSession(anyString(), any());
    }

    @Test
    @DisplayName("generateWechatMpAuthUrl: 正常路径 session.source=platform_mp, authUrl 来自 wechatMpAuthService")
    void generateWechatMpAuthUrl_normal_shouldReturnUrl() {
        PlatformWechatMpAuthConfig cfg = new PlatformWechatMpAuthConfig()
                .setAppId("wx-app-id").setAppSecret("wx-secret");
        when(platformWechatMpAuthConfigService.getWechatMpAuthConfig()).thenReturn(cfg);
        when(platformUrlConfigService.getUrlConfig())
                .thenReturn(new PlatformUrlConfig().setPaymentGatewayBaseUrl(GATEWAY_BASE));
        when(wechatMpAuthService.generateAuthUrl(anyString(), eq("wx-app-id"), eq("wx-secret"), anyString()))
                .thenReturn(new WechatAuthUrlResult().setAuthUrl(EXPECTED_AUTH_URL));

        AuthUrlResult result = platformAuthService.generateWechatMpAuthUrl(null);

        assertEquals(EXPECTED_AUTH_URL, result.getAuthUrl());
        assertNotNull(result.getQueryCode());

        ArgumentCaptor<AuthSession> sessionCaptor = ArgumentCaptor.forClass(AuthSession.class);
        verify(authSessionStore).saveSession(anyString(), sessionCaptor.capture());
        assertEquals(AuthSession.SOURCE_PLATFORM_MP, sessionCaptor.getValue().getSource());
        verify(authSessionStore).saveWaitingResult(result.getQueryCode());
    }

    // ==================== generateDouyinAuthUrl ====================

    @Test
    @DisplayName("generateDouyinAuthUrl: clientKey/clientSecret 未配置抛 douyinH5NotConfigured")
    void generateDouyinAuthUrl_notConfigured_shouldThrow() {
        when(platformDouyinH5AuthConfigService.getDouyinH5AuthConfig())
                .thenReturn(new PlatformDouyinH5AuthConfig());

        BizInfoException ex = assertThrows(BizInfoException.class,
                () -> platformAuthService.generateDouyinAuthUrl(null));

        assertEquals("error.social.douyinH5NotConfigured", ex.getMessageKey());
        verify(authSessionStore, never()).saveSession(anyString(), any());
    }

    @Test
    @DisplayName("generateDouyinAuthUrl: 正常路径 session.source=platform_douyin, authUrl 来自 douyinH5AuthService")
    void generateDouyinAuthUrl_normal_shouldReturnUrl() {
        PlatformDouyinH5AuthConfig cfg = new PlatformDouyinH5AuthConfig()
                .setClientKey("dy-key").setClientSecret("dy-secret");
        when(platformDouyinH5AuthConfigService.getDouyinH5AuthConfig()).thenReturn(cfg);
        when(platformUrlConfigService.getUrlConfig())
                .thenReturn(new PlatformUrlConfig().setPaymentGatewayBaseUrl(GATEWAY_BASE));
        when(douyinH5AuthService.buildSilentAuthUrl(eq("dy-key"), anyString(), anyString()))
                .thenReturn(EXPECTED_AUTH_URL);

        AuthUrlResult result = platformAuthService.generateDouyinAuthUrl(null);

        assertEquals(EXPECTED_AUTH_URL, result.getAuthUrl());
        assertNotNull(result.getQueryCode());

        ArgumentCaptor<AuthSession> sessionCaptor = ArgumentCaptor.forClass(AuthSession.class);
        verify(authSessionStore).saveSession(anyString(), sessionCaptor.capture());
        assertEquals(AuthSession.SOURCE_PLATFORM_DOUYIN, sessionCaptor.getValue().getSource());
        verify(authSessionStore).saveWaitingResult(result.getQueryCode());
    }

    // ==================== authAlipay ====================

    @Test
    @DisplayName("authAlipay: 配置不完整抛 alipayNotConfigured")
    void authAlipay_notConfigured_shouldThrow() {
        when(platformAlipayAuthConfigService.toCapabilityConfig()).thenReturn(new AlipayAuthConfig());
        when(alipayAuthCapability.isConfigured(any())).thenReturn(false);

        BizInfoException ex = assertThrows(BizInfoException.class,
                () -> platformAuthService.authAlipay(new AuthCodeParam(), null));

        assertEquals("error.social.alipayNotConfigured", ex.getMessageKey());
        verify(authSessionStore, never()).writeResultByQueryCode(anyString(), any(), any());
    }

    @Test
    @DisplayName("authAlipay: userId 与 openId 均空抛 authFailed")
    void authAlipay_identifiersBlank_shouldThrow() {
        when(platformAlipayAuthConfigService.toCapabilityConfig()).thenReturn(new AlipayAuthConfig());
        when(alipayAuthCapability.isConfigured(any())).thenReturn(true);
        when(alipayAuthCapability.getUserId(any(), eq("code-x")))
                .thenReturn(new AlipayAuthResult());

        AuthCodeParam param = newAuthCodeParam("code-x");
        BizInfoException ex = assertThrows(BizInfoException.class,
                () -> platformAuthService.authAlipay(param, null));

        assertEquals("error.alipay.authFailed", ex.getMessageKey());
        verify(authSessionStore, never()).writeResultByQueryCode(anyString(), any(), any());
    }

    @Test
    @DisplayName("authAlipay: userId 为空时回退使用 openId")
    void authAlipay_openIdFallback_shouldUseOpenId() {
        when(platformAlipayAuthConfigService.toCapabilityConfig()).thenReturn(new AlipayAuthConfig());
        when(alipayAuthCapability.isConfigured(any())).thenReturn(true);
        when(alipayAuthCapability.getUserId(any(), eq("code-x")))
                .thenReturn(new AlipayAuthResult().setOpenId("oid-fallback"));

        AuthCodeParam param = newAuthCodeParam("code-x");
        AuthResult result = platformAuthService.authAlipay(param, null);

        assertEquals("oid-fallback", result.getOpenId());
        assertEquals("oid-fallback", result.getUserId());
        assertEquals(ChannelAuthStatusEnum.SUCCESS.getCode(), result.getStatus());
        verify(authSessionStore).writeResultByQueryCode(eq(param.getQueryCode()), eq(null), any());
    }

    @Test
    @DisplayName("authAlipay: 正常路径 userId 优先, 回填 session.returnPath")
    void authAlipay_normal_shouldReturnUserIdAndFillReturnPath() {
        when(platformAlipayAuthConfigService.toCapabilityConfig()).thenReturn(new AlipayAuthConfig());
        when(alipayAuthCapability.isConfigured(any())).thenReturn(true);
        when(alipayAuthCapability.getUserId(any(), eq("code-x")))
                .thenReturn(new AlipayAuthResult().setUserId("uid-1").setAccessToken("atk"));

        AuthCodeParam param = newAuthCodeParam("code-x");
        AuthSession session = new AuthSession().setReturnPath("/cashier/ORD/alipay");
        AuthResult result = platformAuthService.authAlipay(param, session);

        assertEquals("uid-1", result.getUserId());
        assertEquals("uid-1", result.getOpenId());
        assertEquals("atk", result.getAccessToken());
        assertEquals("/cashier/ORD/alipay", result.getReturnPath());
        verify(authSessionStore).writeResultByQueryCode(eq(param.getQueryCode()), eq(session), any());
    }

    // ==================== authWechatMp ====================

    @Test
    @DisplayName("authWechatMp: openId 为空抛 wechat authFailed")
    void authWechatMp_openIdBlank_shouldThrow() {
        when(platformWechatMpAuthConfigService.getWechatMpAuthConfig())
                .thenReturn(new PlatformWechatMpAuthConfig().setAppId("x").setAppSecret("y"));
        when(wechatMpAuthService.getTokenAndOpenId(eq("code-x"), eq("x"), eq("y")))
                .thenReturn(new WechatAuthResult());

        AuthCodeParam param = newAuthCodeParam("code-x");
        BizInfoException ex = assertThrows(BizInfoException.class,
                () -> platformAuthService.authWechatMp(param, null));

        assertEquals("error.channel.wechat.authFailed", ex.getMessageKey());
        verify(authSessionStore, never()).writeResultByQueryCode(anyString(), any(), any());
    }

    @Test
    @DisplayName("authWechatMp: 正常路径返回 openId 并回填 returnPath")
    void authWechatMp_normal_shouldReturnOpenId() {
        when(platformWechatMpAuthConfigService.getWechatMpAuthConfig())
                .thenReturn(new PlatformWechatMpAuthConfig().setAppId("x").setAppSecret("y"));
        when(wechatMpAuthService.getTokenAndOpenId(eq("code-x"), eq("x"), eq("y")))
                .thenReturn(new WechatAuthResult().setOpenId("wx-oid").setAccessToken("atk"));

        AuthCodeParam param = newAuthCodeParam("code-x");
        AuthSession session = new AuthSession().setReturnPath("/aggregate/wechat/ORD");
        AuthResult result = platformAuthService.authWechatMp(param, session);

        assertEquals("wx-oid", result.getOpenId());
        assertEquals("atk", result.getAccessToken());
        assertEquals("/aggregate/wechat/ORD", result.getReturnPath());
        verify(authSessionStore).writeResultByQueryCode(eq(param.getQueryCode()), eq(session), any());
    }

    // ==================== authDouyin ====================

    @Test
    @DisplayName("authDouyin: openId 为空抛 douyin authFailed")
    void authDouyin_openIdBlank_shouldThrow() {
        when(platformDouyinH5AuthConfigService.getDouyinH5AuthConfig())
                .thenReturn(new PlatformDouyinH5AuthConfig().setClientKey("k").setClientSecret("s"));
        when(douyinH5AuthService.getOpenIdByCode(eq("k"), eq("s"), eq("code-x")))
                .thenReturn(new DouyinAuthResult());

        AuthCodeParam param = newAuthCodeParam("code-x");
        BizInfoException ex = assertThrows(BizInfoException.class,
                () -> platformAuthService.authDouyin(param, null));

        assertEquals("error.douyin.authFailed", ex.getMessageKey());
        verify(authSessionStore, never()).writeResultByQueryCode(anyString(), any(), any());
    }

    @Test
    @DisplayName("authDouyin: 正常路径返回 openId")
    void authDouyin_normal_shouldReturnOpenId() {
        when(platformDouyinH5AuthConfigService.getDouyinH5AuthConfig())
                .thenReturn(new PlatformDouyinH5AuthConfig().setClientKey("k").setClientSecret("s"));
        when(douyinH5AuthService.getOpenIdByCode(eq("k"), eq("s"), eq("code-x")))
                .thenReturn(new DouyinAuthResult().setOpenId("dy-oid").setAccessToken("atk"));

        AuthCodeParam param = newAuthCodeParam("code-x");
        AuthResult result = platformAuthService.authDouyin(param, null);

        assertEquals("dy-oid", result.getOpenId());
        assertEquals("atk", result.getAccessToken());
        assertEquals(ChannelAuthStatusEnum.SUCCESS.getCode(), result.getStatus());
        verify(authSessionStore).writeResultByQueryCode(eq(param.getQueryCode()), eq(null), any());
    }

    // ==================== 辅助 ====================

    /// 构造带固定 authCode 与 queryCode 的 AuthCodeParam
    private AuthCodeParam newAuthCodeParam(String authCode) {
        AuthCodeParam param = new AuthCodeParam();
        param.setAuthCode(authCode);
        param.setQueryCode("qc-fixed");
        return param;
    }
}
