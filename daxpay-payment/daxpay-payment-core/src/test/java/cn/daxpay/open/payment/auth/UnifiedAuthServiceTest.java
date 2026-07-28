package cn.daxpay.open.payment.auth;

import cn.daxpay.open.payment.auth.channel.MerchantChannelAuthService;
import cn.daxpay.open.payment.auth.core.AuthSession;
import cn.daxpay.open.payment.auth.core.AuthSourceEnum;
import cn.daxpay.open.payment.auth.core.AuthSessionStore;
import cn.daxpay.open.payment.auth.platform.AlipayAuthProvider;
import cn.daxpay.open.payment.auth.platform.DouyinH5AuthProvider;
import cn.daxpay.open.payment.auth.platform.WechatMpAuthProvider;
import cn.daxpay.open.payment.unipay.param.assist.AuthCodeParam;
import cn.daxpay.open.payment.unipay.param.assist.GenerateAuthUrlParam;
import cn.daxpay.open.payment.unipay.result.assist.AuthResult;
import cn.daxpay.open.payment.unipay.result.assist.AuthUrlResult;
import cn.daxpay.open.platform.core.enums.unipay.ChannelAuthTypeEnum;
import cn.daxpay.open.platform.core.exception.BizInfoException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/// # 统一认证服务 Facade 测试(Provider 注册表版本)
///
/// 锁定 [UnifiedAuthService] 的 Provider 注册表分发行为。覆盖:
/// - generateAuthUrl: authType=alipay 走平台级支付宝 Provider; 其余走商户级服务
/// - auth: session.source 命中 Provider; 无 session 兜底(alipay); 商户级 session 走策略; 失效异常
/// - returnPath 回填 + deleteSession 一次失效
@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class UnifiedAuthServiceTest {

    @Mock private AuthSessionStore authSessionStore;
    @Mock private MerchantChannelAuthService merchantChannelAuthService;
    @Mock private AlipayAuthProvider alipayAuthProvider;
    @Mock private WechatMpAuthProvider wechatMpAuthProvider;
    @Mock private DouyinH5AuthProvider douyinH5AuthProvider;

    private UnifiedAuthService unifiedAuthService;

    @BeforeEach
    void setUp() {
        // Provider 注册表索引: 构造时调 sourceCode() 建 Map
        when(alipayAuthProvider.sourceCode()).thenReturn(AuthSourceEnum.PLATFORM_ALIPAY);
        when(wechatMpAuthProvider.sourceCode()).thenReturn(AuthSourceEnum.PLATFORM_MP);
        when(douyinH5AuthProvider.sourceCode()).thenReturn(AuthSourceEnum.PLATFORM_DOUYIN);
        unifiedAuthService = new UnifiedAuthService(authSessionStore, merchantChannelAuthService,
                List.of(alipayAuthProvider, wechatMpAuthProvider, douyinH5AuthProvider));
    }

    // ==================== generateAuthUrl ====================

    @Test
    @DisplayName("generateAuthUrl: authType=alipay 委托平台级支付宝 Provider, 透传 returnPath")
    void generateAuthUrl_alipay_shouldDelegateToAlipayProvider() {
        GenerateAuthUrlParam param = new GenerateAuthUrlParam();
        param.setAuthType(ChannelAuthTypeEnum.ALIPAY.getCode());
        param.setReturnPath("/cashier/ORD/alipay");
        AuthUrlResult expected = new AuthUrlResult().setAuthUrl("https://openauth.alipay.com/...");
        when(alipayAuthProvider.generateAuthUrl("/cashier/ORD/alipay")).thenReturn(expected);

        AuthUrlResult result = unifiedAuthService.generateAuthUrl(param);

        assertSame(expected, result);
        verify(merchantChannelAuthService, never()).generateAuthUrl(any());
    }

    @Test
    @DisplayName("generateAuthUrl: authType=wechat 委托商户级通道策略")
    void generateAuthUrl_wechat_shouldDelegateToMerchantService() {
        GenerateAuthUrlParam param = new GenerateAuthUrlParam();
        param.setAuthType(ChannelAuthTypeEnum.WECHAT.getCode());
        AuthUrlResult expected = new AuthUrlResult().setAuthUrl("https://open.weixin.qq.com/...");
        when(merchantChannelAuthService.generateAuthUrl(eq(param))).thenReturn(expected);

        AuthUrlResult result = unifiedAuthService.generateAuthUrl(param);

        assertSame(expected, result);
        verify(alipayAuthProvider, never()).generateAuthUrl(any());
    }

    // ==================== auth: 失效异常 ====================

    @Test
    @DisplayName("auth: session 失效且 authType 非 alipay 抛 authSessionExpired")
    void auth_sessionExpiredAndNotAlipay_shouldThrow() {
        AuthCodeParam param = newAuthCodeParam("tok-x", ChannelAuthTypeEnum.WECHAT.getCode());
        when(authSessionStore.loadSession("tok-x")).thenReturn(null);

        BizInfoException ex = assertThrows(BizInfoException.class, () -> unifiedAuthService.auth(param));

        assertEquals("pay.error.assist.authSessionExpired", ex.getMessageKey());
        verify(authSessionStore, never()).deleteSession(any());
        verify(merchantChannelAuthService, never()).auth(any(), any());
    }

    // ==================== auth: source 分支(Provider 注册表查找) ====================

    @Test
    @DisplayName("auth: source=platform_alipay 命中 AlipayProvider")
    void auth_sourceAlipay_shouldCallAlipayProvider() {
        AuthCodeParam param = newAuthCodeParam("tok-1", ChannelAuthTypeEnum.ALIPAY.getCode());
        AuthSession session = new AuthSession().setSource(AuthSourceEnum.PLATFORM_ALIPAY.getCode());
        AuthResult expected = new AuthResult().setOpenId("uid-1");
        when(authSessionStore.loadSession("tok-1")).thenReturn(session);
        when(alipayAuthProvider.auth(param, session)).thenReturn(expected);

        AuthResult result = unifiedAuthService.auth(param);

        assertSame(expected, result);
        verify(authSessionStore).deleteSession("tok-1");
        verify(merchantChannelAuthService, never()).auth(any(), any());
    }

    @Test
    @DisplayName("auth: source=platform_mp 命中 WechatMpProvider")
    void auth_sourceMp_shouldCallWechatMpProvider() {
        AuthCodeParam param = newAuthCodeParam("tok-2", ChannelAuthTypeEnum.WECHAT.getCode());
        AuthSession session = new AuthSession().setSource(AuthSourceEnum.PLATFORM_MP.getCode());
        AuthResult expected = new AuthResult().setOpenId("openid-2");
        when(authSessionStore.loadSession("tok-2")).thenReturn(session);
        when(wechatMpAuthProvider.auth(param, session)).thenReturn(expected);

        AuthResult result = unifiedAuthService.auth(param);

        assertSame(expected, result);
        verify(authSessionStore).deleteSession("tok-2");
    }

    @Test
    @DisplayName("auth: source=platform_douyin 命中 DouyinH5Provider")
    void auth_sourceDouyin_shouldCallDouyinProvider() {
        AuthCodeParam param = newAuthCodeParam("tok-3", ChannelAuthTypeEnum.DOUYIN.getCode());
        AuthSession session = new AuthSession().setSource(AuthSourceEnum.PLATFORM_DOUYIN.getCode());
        AuthResult expected = new AuthResult().setOpenId("openid-3");
        when(authSessionStore.loadSession("tok-3")).thenReturn(session);
        when(douyinH5AuthProvider.auth(param, session)).thenReturn(expected);

        AuthResult result = unifiedAuthService.auth(param);

        assertSame(expected, result);
        verify(authSessionStore).deleteSession("tok-3");
    }

    @Test
    @DisplayName("auth: 无 session 且 authType=alipay 兜底走 AlipayProvider(auth 传 null session)")
    void auth_noSessionAlipay_shouldFallbackToAlipayProvider() {
        AuthCodeParam param = newAuthCodeParam("tok-4", ChannelAuthTypeEnum.ALIPAY.getCode());
        when(authSessionStore.loadSession("tok-4")).thenReturn(null);
        AuthResult expected = new AuthResult().setOpenId("uid-4");
        when(alipayAuthProvider.auth(eq(param), eq(null))).thenReturn(expected);

        AuthResult result = unifiedAuthService.auth(param);

        assertSame(expected, result);
        verify(authSessionStore).deleteSession("tok-4");
        verify(merchantChannelAuthService, never()).auth(any(), any());
    }

    @Test
    @DisplayName("auth: 商户级 session(无 source 标记)走商户级服务")
    void auth_merchantSession_shouldCallMerchantService() {
        AuthCodeParam param = newAuthCodeParam("tok-5", ChannelAuthTypeEnum.WECHAT.getCode());
        AuthSession session = new AuthSession().setAuthType(ChannelAuthTypeEnum.WECHAT.getCode());
        AuthResult expected = new AuthResult().setOpenId("openid-5");
        when(authSessionStore.loadSession("tok-5")).thenReturn(session);
        when(merchantChannelAuthService.auth(param, session)).thenReturn(expected);

        AuthResult result = unifiedAuthService.auth(param);

        assertSame(expected, result);
        verify(authSessionStore).deleteSession("tok-5");
        verify(alipayAuthProvider, never()).auth(any(), any());
    }

    // ==================== auth: returnPath 回填 ====================

    @Test
    @DisplayName("auth: Provider 未回填 returnPath 时从 session 补齐")
    void auth_shouldFillReturnPathFromSessionWhenBlank() {
        AuthCodeParam param = newAuthCodeParam("tok-6", ChannelAuthTypeEnum.WECHAT.getCode());
        AuthSession session = new AuthSession()
                .setSource(AuthSourceEnum.PLATFORM_MP.getCode())
                .setReturnPath("/aggregate/wechat/ORD");
        AuthResult expected = new AuthResult().setOpenId("openid-6");
        when(authSessionStore.loadSession("tok-6")).thenReturn(session);
        when(wechatMpAuthProvider.auth(param, session)).thenReturn(expected);

        AuthResult result = unifiedAuthService.auth(param);

        assertEquals("/aggregate/wechat/ORD", result.getReturnPath());
    }

    @Test
    @DisplayName("auth: Provider 已回填 returnPath 时不被 session 覆盖")
    void auth_shouldNotOverrideReturnPathWhenAlreadySet() {
        AuthCodeParam param = newAuthCodeParam("tok-7", ChannelAuthTypeEnum.ALIPAY.getCode());
        AuthSession session = new AuthSession()
                .setSource(AuthSourceEnum.PLATFORM_ALIPAY.getCode())
                .setReturnPath("/from-session");
        AuthResult expected = new AuthResult()
                .setOpenId("uid-7")
                .setReturnPath("/from-provider");
        when(authSessionStore.loadSession("tok-7")).thenReturn(session);
        when(alipayAuthProvider.auth(param, session)).thenReturn(expected);

        AuthResult result = unifiedAuthService.auth(param);

        assertEquals("/from-provider", result.getReturnPath());
    }

    // ==================== 辅助 ====================

    private AuthCodeParam newAuthCodeParam(String authToken, String authType) {
        AuthCodeParam param = new AuthCodeParam();
        param.setAuthToken(authToken);
        param.setAuthType(authType);
        param.setAuthCode("code-x");
        return param;
    }
}
