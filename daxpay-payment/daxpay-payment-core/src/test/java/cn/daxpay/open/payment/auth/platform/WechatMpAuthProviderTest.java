package cn.daxpay.open.payment.auth.platform;

import cn.daxpay.open.payment.auth.core.AuthScene;
import cn.daxpay.open.payment.auth.core.AuthSession;
import cn.daxpay.open.payment.auth.core.AuthSessionStore;
import cn.daxpay.open.payment.unipay.param.assist.AuthCodeParam;
import cn.daxpay.open.payment.unipay.result.assist.AuthResult;
import cn.daxpay.open.payment.unipay.result.assist.AuthUrlResult;
import cn.daxpay.open.platform.capability.wechat.auth.result.WechatAuthResult;
import cn.daxpay.open.platform.capability.wechat.auth.result.WechatAuthUrlResult;
import cn.daxpay.open.platform.capability.wechat.auth.service.WechatMpAuthService;
import cn.daxpay.open.platform.core.enums.unipay.ChannelAuthStatusEnum;
import cn.daxpay.open.platform.core.exception.BizInfoException;
import cn.daxpay.open.platform.system.entity.config.platform.auth.PlatformWechatMpAuthConfig;
import cn.daxpay.open.platform.system.service.config.auth.PlatformWechatMpAuthConfigService;
import cn.daxpay.open.platform.system.entity.config.platform.infra.PlatformUrlConfig;
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

/// # 微信公众号平台级认证 Provider 测试
///
/// 覆盖 [WechatMpAuthProvider] 的 generateAuthUrl / auth 内部逻辑。
/// 配置来源为三方平台管理的 [PlatformWechatMpAuthConfigService], 与 iam 社交登录共用。
@ExtendWith(MockitoExtension.class)
class WechatMpAuthProviderTest {

    @Mock private AuthSessionStore authSessionStore;
    @Mock private PlatformWechatMpAuthConfigService platformWechatMpAuthConfigService;
    @Mock private PlatformUrlConfigService platformUrlConfigService;
    @Mock private WechatMpAuthService wechatMpAuthService;

    @InjectMocks private WechatMpAuthProvider wechatMpAuthProvider;

    private static final String GATEWAY_BASE = "https://gw.example.com";

    /// 构建微信公众号认证配置(appId + appSecret)
    private PlatformWechatMpAuthConfig buildMpConfig(String appId, String appSecret) {
        return new PlatformWechatMpAuthConfig().setAppId(appId).setAppSecret(appSecret);
    }

    @Test
    @DisplayName("sourceCode 返回 SOURCE_PLATFORM_MP")
    void sourceCode_isPlatformMp() {
        assertEquals(AuthSession.SOURCE_PLATFORM_MP, wechatMpAuthProvider.sourceCode());
    }

    @Test
    @DisplayName("generateAuthUrl: 配置未填写抛 mpAuthNotConfigured, 不写 session")
    void generateAuthUrl_configMissing_shouldThrow() {
        when(platformWechatMpAuthConfigService.getWechatMpAuthConfig())
                .thenReturn(buildMpConfig("", ""));

        BizInfoException ex = assertThrows(BizInfoException.class,
                () -> wechatMpAuthProvider.generateAuthUrl(null));

        assertEquals("error.payment.wx.mpAuthNotConfigured", ex.getMessageKey());
        verify(authSessionStore, never()).saveSession(anyString(), any());
    }

    @Test
    @DisplayName("generateAuthUrl: 正常路径 session.source=platform_mp, scene=platform")
    void generateAuthUrl_normal_shouldSaveSession() {
        when(platformWechatMpAuthConfigService.getWechatMpAuthConfig())
                .thenReturn(buildMpConfig("wxid", "secret"));
        when(platformUrlConfigService.getUrlConfig())
                .thenReturn(new PlatformUrlConfig().setPaymentGatewayBaseUrl(GATEWAY_BASE));
        when(wechatMpAuthService.generateAuthUrl(anyString(), eq("wxid"), eq("secret"), anyString()))
                .thenReturn(new WechatAuthUrlResult().setAuthUrl("https://open.weixin.qq.com/..."));

        AuthUrlResult result = wechatMpAuthProvider.generateAuthUrl(null);

        assertNotNull(result.getQueryCode());
        assertNotNull(result.getAuthToken());
        ArgumentCaptor<AuthSession> captor = ArgumentCaptor.forClass(AuthSession.class);
        verify(authSessionStore).saveSession(anyString(), captor.capture());
        AuthSession session = captor.getValue();
        assertEquals(AuthSession.SOURCE_PLATFORM_MP, session.getSource());
        assertEquals(AuthScene.PLATFORM.getCode(), session.getScene());
    }

    @Test
    @DisplayName("auth: openId 为空抛 wechat authFailed")
    void auth_openIdBlank_shouldThrow() {
        when(platformWechatMpAuthConfigService.getWechatMpAuthConfig())
                .thenReturn(buildMpConfig("x", "y"));
        when(wechatMpAuthService.getTokenAndOpenId(eq("code-x"), eq("x"), eq("y")))
                .thenReturn(new WechatAuthResult());

        AuthCodeParam param = newAuthCodeParam("code-x");
        BizInfoException ex = assertThrows(BizInfoException.class, () -> wechatMpAuthProvider.auth(param, null));

        assertEquals("error.channel.wechat.authFailed", ex.getMessageKey());
        verify(authSessionStore, never()).writeResultByQueryCode(anyString(), any(), any());
    }

    @Test
    @DisplayName("auth: 正常路径返回 openId 并回填 returnPath")
    void auth_normal_shouldReturnOpenId() {
        when(platformWechatMpAuthConfigService.getWechatMpAuthConfig())
                .thenReturn(buildMpConfig("x", "y"));
        when(wechatMpAuthService.getTokenAndOpenId(eq("code-x"), eq("x"), eq("y")))
                .thenReturn(new WechatAuthResult().setOpenId("wx-oid").setAccessToken("atk"));

        AuthCodeParam param = newAuthCodeParam("code-x");
        AuthSession session = new AuthSession().setReturnPath("/aggregate/wechat/ORD");
        AuthResult result = wechatMpAuthProvider.auth(param, session);

        assertEquals("wx-oid", result.getOpenId());
        assertEquals("atk", result.getAccessToken());
        assertEquals("/aggregate/wechat/ORD", result.getReturnPath());
        assertEquals(ChannelAuthStatusEnum.SUCCESS.getCode(), result.getStatus());
    }

    private AuthCodeParam newAuthCodeParam(String authCode) {
        AuthCodeParam param = new AuthCodeParam();
        param.setAuthCode(authCode);
        param.setQueryCode("qc-fixed");
        return param;
    }
}