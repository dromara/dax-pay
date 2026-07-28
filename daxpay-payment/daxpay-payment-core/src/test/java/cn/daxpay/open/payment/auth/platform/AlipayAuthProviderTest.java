package cn.daxpay.open.payment.auth.platform;

import cn.daxpay.open.payment.auth.core.AuthSession;
import cn.daxpay.open.payment.auth.core.AuthSourceEnum;
import cn.daxpay.open.payment.auth.core.AuthSessionStore;
import cn.daxpay.open.payment.unipay.param.assist.AuthCodeParam;
import cn.daxpay.open.payment.unipay.result.assist.AuthResult;
import cn.daxpay.open.payment.unipay.result.assist.AuthUrlResult;
import cn.daxpay.open.platform.capability.alipay.auth.config.AlipayAuthConfig;
import cn.daxpay.open.platform.capability.alipay.auth.result.AlipayAuthResult;
import cn.daxpay.open.platform.capability.alipay.auth.service.AlipayAuthCapability;
import cn.daxpay.open.platform.core.enums.unipay.ChannelAuthStatusEnum;
import cn.daxpay.open.platform.core.exception.BizInfoException;
import cn.daxpay.open.platform.system.service.config.auth.PlatformAlipayAuthConfigService;
import cn.daxpay.open.platform.system.service.config.infra.PlatformUrlConfigService;
import cn.daxpay.open.platform.system.entity.config.platform.infra.PlatformUrlConfig;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/// # 支付宝平台级认证 Provider 测试
///
/// 覆盖 [AlipayAuthProvider] 的 generateAuthUrl / auth 内部逻辑。
/// 重点验证「先校验后 save」改进: 配置缺失时不写入 session。
@ExtendWith(MockitoExtension.class)
class AlipayAuthProviderTest {

    @Mock private AuthSessionStore authSessionStore;
    @Mock private PlatformAlipayAuthConfigService platformAlipayAuthConfigService;
    @Mock private PlatformUrlConfigService platformUrlConfigService;
    @Mock private AlipayAuthCapability alipayAuthCapability;

    @InjectMocks private AlipayAuthProvider alipayAuthProvider;

    private static final String GATEWAY_BASE = "https://gw.example.com";
    private static final String EXPECTED_AUTH_URL = "https://openauth.example.com/oauth?state=xxx";

    @Test
    @DisplayName("sourceCode 返回 SOURCE_PLATFORM_ALIPAY")
    void sourceCode_isPlatformAlipay() {
        assertEquals(AuthSourceEnum.PLATFORM_ALIPAY, alipayAuthProvider.sourceCode());
    }

    @Test
    @DisplayName("generateAuthUrl: 配置不完整抛 alipayNotConfigured, 且不写入 session(先校验后 save)")
    void generateAuthUrl_notConfigured_shouldThrowWithoutSavingSession() {
        when(platformAlipayAuthConfigService.toCapabilityConfig()).thenReturn(new AlipayAuthConfig());
        when(alipayAuthCapability.isConfigured(any())).thenReturn(false);

        BizInfoException ex = assertThrows(BizInfoException.class,
                () -> alipayAuthProvider.generateAuthUrl("/return"));

        assertEquals("error.social.alipayNotConfigured", ex.getMessageKey());
        verify(authSessionStore, never()).saveSession(anyString(), any());
    }

    @Test
    @DisplayName("generateAuthUrl: gatewayBase 未配置抛 gatewayUrlNotConfigured")
    void generateAuthUrl_gatewayBlank_shouldThrow() {
        when(platformAlipayAuthConfigService.toCapabilityConfig()).thenReturn(new AlipayAuthConfig());
        when(alipayAuthCapability.isConfigured(any())).thenReturn(true);
        when(platformUrlConfigService.getUrlConfig()).thenReturn(new PlatformUrlConfig());

        BizInfoException ex = assertThrows(BizInfoException.class,
                () -> alipayAuthProvider.generateAuthUrl(null));

        assertEquals("error.common.gatewayUrlNotConfigured", ex.getMessageKey());
    }

    @Test
    @DisplayName("generateAuthUrl: 正常路径 session.source=platform_alipay, 透传 returnPath")
    void generateAuthUrl_normal_shouldSaveSessionAndReturnUrl() {
        AlipayAuthConfig capabilityConfig = new AlipayAuthConfig();
        when(platformAlipayAuthConfigService.toCapabilityConfig()).thenReturn(capabilityConfig);
        when(alipayAuthCapability.isConfigured(capabilityConfig)).thenReturn(true);
        when(platformUrlConfigService.getUrlConfig())
                .thenReturn(new PlatformUrlConfig().setPaymentGatewayBaseUrl(GATEWAY_BASE));
        when(alipayAuthCapability.generateAuthUrl(eq(capabilityConfig), anyString(), anyString(), anyString(), eq(false)))
                .thenReturn(EXPECTED_AUTH_URL);

        AuthUrlResult result = alipayAuthProvider.generateAuthUrl("/cashier/ORD/alipay");

        assertEquals(EXPECTED_AUTH_URL, result.getAuthUrl());
        assertNotNull(result.getQueryCode());
        ArgumentCaptor<AuthSession> sessionCaptor = ArgumentCaptor.forClass(AuthSession.class);
        verify(authSessionStore).saveSession(anyString(), sessionCaptor.capture());
        assertEquals(AuthSourceEnum.PLATFORM_ALIPAY.getCode(), sessionCaptor.getValue().getSource());
        assertEquals("/cashier/ORD/alipay", sessionCaptor.getValue().getReturnPath());
        verify(authSessionStore).saveWaitingResult(result.getQueryCode());
    }

    @Test
    @DisplayName("auth: 配置不完整抛 alipayNotConfigured")
    void auth_notConfigured_shouldThrow() {
        when(platformAlipayAuthConfigService.toCapabilityConfig()).thenReturn(new AlipayAuthConfig());
        when(alipayAuthCapability.isConfigured(any())).thenReturn(false);

        BizInfoException ex = assertThrows(BizInfoException.class,
                () -> alipayAuthProvider.auth(new AuthCodeParam(), null));

        assertEquals("error.social.alipayNotConfigured", ex.getMessageKey());
        verify(authSessionStore, never()).writeResultByQueryCode(anyString(), any(), any());
    }

    @Test
    @DisplayName("auth: userId 与 openId 均空抛 authFailed")
    void auth_identifiersBlank_shouldThrow() {
        when(platformAlipayAuthConfigService.toCapabilityConfig()).thenReturn(new AlipayAuthConfig());
        when(alipayAuthCapability.isConfigured(any())).thenReturn(true);
        when(alipayAuthCapability.getUserId(any(), eq("code-x"))).thenReturn(new AlipayAuthResult());

        AuthCodeParam param = newAuthCodeParam("code-x");
        BizInfoException ex = assertThrows(BizInfoException.class, () -> alipayAuthProvider.auth(param, null));

        assertEquals("error.alipay.authFailed", ex.getMessageKey());
        verify(authSessionStore, never()).writeResultByQueryCode(anyString(), any(), any());
    }

    @Test
    @DisplayName("auth: userId 为空时回退使用 openId")
    void auth_openIdFallback_shouldUseOpenId() {
        when(platformAlipayAuthConfigService.toCapabilityConfig()).thenReturn(new AlipayAuthConfig());
        when(alipayAuthCapability.isConfigured(any())).thenReturn(true);
        when(alipayAuthCapability.getUserId(any(), eq("code-x")))
                .thenReturn(new AlipayAuthResult().setOpenId("oid-fallback"));

        AuthResult result = alipayAuthProvider.auth(newAuthCodeParam("code-x"), null);

        assertEquals("oid-fallback", result.getOpenId());
        assertEquals("oid-fallback", result.getUserId());
        assertEquals(ChannelAuthStatusEnum.SUCCESS.getCode(), result.getStatus());
    }

    @Test
    @DisplayName("auth: 正常路径 userId 优先, 回填 session.returnPath")
    void auth_normal_shouldReturnUserIdAndFillReturnPath() {
        when(platformAlipayAuthConfigService.toCapabilityConfig()).thenReturn(new AlipayAuthConfig());
        when(alipayAuthCapability.isConfigured(any())).thenReturn(true);
        when(alipayAuthCapability.getUserId(any(), eq("code-x")))
                .thenReturn(new AlipayAuthResult().setUserId("uid-1").setAccessToken("atk"));

        AuthCodeParam param = newAuthCodeParam("code-x");
        AuthSession session = new AuthSession().setReturnPath("/cashier/ORD/alipay");
        AuthResult result = alipayAuthProvider.auth(param, session);

        assertEquals("uid-1", result.getUserId());
        assertEquals("atk", result.getAccessToken());
        assertEquals("/cashier/ORD/alipay", result.getReturnPath());
        verify(authSessionStore).writeResultByQueryCode(eq(param.getQueryCode()), eq(session), any());
    }

    private AuthCodeParam newAuthCodeParam(String authCode) {
        AuthCodeParam param = new AuthCodeParam();
        param.setAuthCode(authCode);
        param.setQueryCode("qc-fixed");
        return param;
    }
}
