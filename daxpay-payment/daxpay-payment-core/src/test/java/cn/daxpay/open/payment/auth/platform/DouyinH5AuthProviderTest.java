package cn.daxpay.open.payment.auth.platform;

import cn.daxpay.open.payment.auth.core.AuthSession;
import cn.daxpay.open.payment.auth.core.AuthSourceEnum;
import cn.daxpay.open.payment.auth.core.AuthSessionStore;
import cn.daxpay.open.payment.unipay.param.assist.AuthCodeParam;
import cn.daxpay.open.payment.unipay.result.assist.AuthResult;
import cn.daxpay.open.payment.unipay.result.assist.AuthUrlResult;
import cn.daxpay.open.platform.capability.douyin.auth.result.DouyinAuthResult;
import cn.daxpay.open.platform.capability.douyin.auth.service.DouyinH5AuthService;
import cn.daxpay.open.platform.core.enums.unipay.ChannelAuthStatusEnum;
import cn.daxpay.open.platform.core.exception.BizInfoException;
import cn.daxpay.open.platform.system.entity.config.platform.auth.PlatformDouyinH5AuthConfig;
import cn.daxpay.open.platform.system.entity.config.platform.infra.PlatformUrlConfig;
import cn.daxpay.open.platform.system.service.config.auth.PlatformDouyinH5AuthConfigService;
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

/// # 抖音 H5 平台级认证 Provider 测试
///
/// 覆盖 [DouyinH5AuthProvider] 的 generateAuthUrl / auth 内部逻辑。
@ExtendWith(MockitoExtension.class)
class DouyinH5AuthProviderTest {

    @Mock private AuthSessionStore authSessionStore;
    @Mock private PlatformDouyinH5AuthConfigService platformDouyinH5AuthConfigService;
    @Mock private PlatformUrlConfigService platformUrlConfigService;
    @Mock private DouyinH5AuthService douyinH5AuthService;

    @InjectMocks private DouyinH5AuthProvider douyinH5AuthProvider;

    private static final String GATEWAY_BASE = "https://gw.example.com";

    @Test
    @DisplayName("sourceCode 返回 SOURCE_PLATFORM_DOUYIN")
    void sourceCode_isPlatformDouyin() {
        assertEquals(AuthSourceEnum.PLATFORM_DOUYIN, douyinH5AuthProvider.sourceCode());
    }

    @Test
    @DisplayName("generateAuthUrl: clientKey/clientSecret 未配置抛 douyinH5NotConfigured, 不写 session")
    void generateAuthUrl_notConfigured_shouldThrow() {
        when(platformDouyinH5AuthConfigService.getDouyinH5AuthConfig())
                .thenReturn(new PlatformDouyinH5AuthConfig());

        BizInfoException ex = assertThrows(BizInfoException.class,
                () -> douyinH5AuthProvider.generateAuthUrl(null));

        assertEquals("error.social.douyinH5NotConfigured", ex.getMessageKey());
        verify(authSessionStore, never()).saveSession(anyString(), any());
    }

    @Test
    @DisplayName("generateAuthUrl: 正常路径 session.source=platform_douyin")
    void generateAuthUrl_normal_shouldSaveSession() {
        PlatformDouyinH5AuthConfig cfg = new PlatformDouyinH5AuthConfig().setClientKey("ck").setClientSecret("cs");
        when(platformDouyinH5AuthConfigService.getDouyinH5AuthConfig()).thenReturn(cfg);
        when(platformUrlConfigService.getUrlConfig())
                .thenReturn(new PlatformUrlConfig().setPaymentGatewayBaseUrl(GATEWAY_BASE));
        when(douyinH5AuthService.buildSilentAuthUrl(eq("ck"), anyString(), anyString()))
                .thenReturn("https://aweme.snssdk.com/...");

        AuthUrlResult result = douyinH5AuthProvider.generateAuthUrl(null);

        assertNotNull(result.getQueryCode());
        ArgumentCaptor<AuthSession> captor = ArgumentCaptor.forClass(AuthSession.class);
        verify(authSessionStore).saveSession(anyString(), captor.capture());
        assertEquals(AuthSourceEnum.PLATFORM_DOUYIN.getCode(), captor.getValue().getSource());
    }

    @Test
    @DisplayName("auth: openId 为空抛 douyin authFailed")
    void auth_openIdBlank_shouldThrow() {
        when(platformDouyinH5AuthConfigService.getDouyinH5AuthConfig())
                .thenReturn(new PlatformDouyinH5AuthConfig().setClientKey("k").setClientSecret("s"));
        when(douyinH5AuthService.getOpenIdByCode(eq("k"), eq("s"), eq("code-x")))
                .thenReturn(new DouyinAuthResult());

        AuthCodeParam param = newAuthCodeParam("code-x");
        BizInfoException ex = assertThrows(BizInfoException.class, () -> douyinH5AuthProvider.auth(param, null));

        assertEquals("error.douyin.authFailed", ex.getMessageKey());
        verify(authSessionStore, never()).writeResultByQueryCode(anyString(), any(), any());
    }

    @Test
    @DisplayName("auth: 正常路径返回 openId")
    void auth_normal_shouldReturnOpenId() {
        when(platformDouyinH5AuthConfigService.getDouyinH5AuthConfig())
                .thenReturn(new PlatformDouyinH5AuthConfig().setClientKey("k").setClientSecret("s"));
        when(douyinH5AuthService.getOpenIdByCode(eq("k"), eq("s"), eq("code-x")))
                .thenReturn(new DouyinAuthResult().setOpenId("dy-oid").setAccessToken("atk"));

        AuthCodeParam param = newAuthCodeParam("code-x");
        AuthResult result = douyinH5AuthProvider.auth(param, null);

        assertEquals("dy-oid", result.getOpenId());
        assertEquals("atk", result.getAccessToken());
        assertEquals(ChannelAuthStatusEnum.SUCCESS.getCode(), result.getStatus());
    }

    private AuthCodeParam newAuthCodeParam(String authCode) {
        AuthCodeParam param = new AuthCodeParam();
        param.setAuthCode(authCode);
        param.setQueryCode("qc-fixed");
        return param;
    }
}
