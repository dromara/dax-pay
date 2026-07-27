package cn.daxpay.open.payment.auth;

import cn.daxpay.open.payment.unipay.param.assist.AuthCodeParam;
import cn.daxpay.open.payment.unipay.param.assist.GenerateAuthUrlParam;
import cn.daxpay.open.payment.unipay.result.assist.AuthResult;
import cn.daxpay.open.payment.unipay.result.assist.AuthUrlResult;
import cn.daxpay.open.platform.core.enums.unipay.ChannelAuthTypeEnum;
import cn.daxpay.open.platform.core.exception.BizInfoException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/// # 通道认证服务 Facade 测试
///
/// 锁定 [ChannelAuthService] 的分发行为, 供后续重构(Provider 注册表)回归验证。
/// 覆盖点:
/// - generateAuthUrl: authType=alipay 走平台级支付宝(透传 returnPath); 其余走支付产品策略
/// - auth: session 失效且非 alipay 抛 authSessionExpired; 四个 source 分支(alipay/mp/douyin/产品);
///   无 session 兜底; 成功后 deleteSession 一次失效; returnPath 从 session 回填
@ExtendWith(MockitoExtension.class)
class ChannelAuthServiceTest {

    @Mock
    private AuthSessionStore authSessionStore;

    @Mock
    private PlatformAuthService platformAuthService;

    @Mock
    private ChannelProductAuthService channelProductAuthService;

    @InjectMocks
    private ChannelAuthService channelAuthService;

    // ==================== generateAuthUrl ====================

    @Test
    @DisplayName("generateAuthUrl: authType=alipay 委托平台级支付宝, 透传 returnPath")
    void generateAuthUrl_alipay_shouldDelegateToPlatformWithReturnPath() {
        GenerateAuthUrlParam param = new GenerateAuthUrlParam();
        param.setAuthType(ChannelAuthTypeEnum.ALIPAY.getCode());
        param.setReturnPath("/cashier/ORD123/alipay");
        AuthUrlResult expected = new AuthUrlResult().setAuthUrl("https://openauth.alipay.com/oauth/...");
        when(platformAuthService.generateAlipayAuthUrl("/cashier/ORD123/alipay")).thenReturn(expected);

        AuthUrlResult result = channelAuthService.generateAuthUrl(param);

        assertSame(expected, result);
        verify(channelProductAuthService, never()).generateAuthUrl(param);
    }

    @Test
    @DisplayName("generateAuthUrl: authType=wechat 委托支付产品策略")
    void generateAuthUrl_wechat_shouldDelegateToProductService() {
        GenerateAuthUrlParam param = new GenerateAuthUrlParam();
        param.setAuthType(ChannelAuthTypeEnum.WECHAT.getCode());
        AuthUrlResult expected = new AuthUrlResult().setAuthUrl("https://open.weixin.qq.com/connect/oauth2/authorize?...");
        when(channelProductAuthService.generateAuthUrl(param)).thenReturn(expected);

        AuthUrlResult result = channelAuthService.generateAuthUrl(param);

        assertSame(expected, result);
        verify(platformAuthService, never()).generateAlipayAuthUrl(any());
    }

    // ==================== auth: 失效异常 ====================

    @Test
    @DisplayName("auth: session 失效且 authType 非 alipay 时抛 authSessionExpired, 不调用 deleteSession")
    void auth_sessionExpiredAndNotAlipay_shouldThrow() {
        AuthCodeParam param = newAuthCodeParam("token-expired", ChannelAuthTypeEnum.WECHAT.getCode());
        when(authSessionStore.loadSession("token-expired")).thenReturn(null);

        BizInfoException ex = assertThrows(BizInfoException.class, () -> channelAuthService.auth(param));

        assertEquals("pay.error.assist.authSessionExpired", ex.getMessageKey());
        verify(authSessionStore, never()).deleteSession(any());
        verify(channelProductAuthService, never()).auth(any(), any());
    }

    // ==================== auth: source 分支 ====================

    @Test
    @DisplayName("auth: source=platform_alipay 走平台支付宝分支")
    void auth_platformAlipay_shouldCallAuthAlipay() {
        AuthCodeParam param = newAuthCodeParam("token-1", ChannelAuthTypeEnum.ALIPAY.getCode());
        AuthSession session = new AuthSession().setSource(AuthSession.SOURCE_PLATFORM_ALIPAY);
        AuthResult expected = new AuthResult().setOpenId("uid-1");
        when(authSessionStore.loadSession("token-1")).thenReturn(session);
        when(platformAuthService.authAlipay(param, session)).thenReturn(expected);

        AuthResult result = channelAuthService.auth(param);

        assertSame(expected, result);
        verify(authSessionStore).deleteSession("token-1");
        verify(channelProductAuthService, never()).auth(any(), any());
    }

    @Test
    @DisplayName("auth: source=platform_mp 走平台微信公众号分支")
    void auth_platformMp_shouldCallAuthWechatMp() {
        AuthCodeParam param = newAuthCodeParam("token-2", ChannelAuthTypeEnum.WECHAT.getCode());
        AuthSession session = new AuthSession().setSource(AuthSession.SOURCE_PLATFORM_MP);
        AuthResult expected = new AuthResult().setOpenId("openid-2");
        when(authSessionStore.loadSession("token-2")).thenReturn(session);
        when(platformAuthService.authWechatMp(param, session)).thenReturn(expected);

        AuthResult result = channelAuthService.auth(param);

        assertSame(expected, result);
        verify(authSessionStore).deleteSession("token-2");
    }

    @Test
    @DisplayName("auth: source=platform_douyin 走平台抖音分支")
    void auth_platformDouyin_shouldCallAuthDouyin() {
        AuthCodeParam param = newAuthCodeParam("token-3", ChannelAuthTypeEnum.DOUYIN.getCode());
        AuthSession session = new AuthSession().setSource(AuthSession.SOURCE_PLATFORM_DOUYIN);
        AuthResult expected = new AuthResult().setOpenId("openid-3");
        when(authSessionStore.loadSession("token-3")).thenReturn(session);
        when(platformAuthService.authDouyin(param, session)).thenReturn(expected);

        AuthResult result = channelAuthService.auth(param);

        assertSame(expected, result);
        verify(authSessionStore).deleteSession("token-3");
    }

    @Test
    @DisplayName("auth: 无 session 且 authType=alipay 兜底走平台支付宝(authAlipay 传 null session)")
    void auth_noSessionAlipay_shouldFallbackToPlatformAlipay() {
        AuthCodeParam param = newAuthCodeParam("token-4", ChannelAuthTypeEnum.ALIPAY.getCode());
        when(authSessionStore.loadSession("token-4")).thenReturn(null);
        AuthResult expected = new AuthResult().setOpenId("uid-4");
        when(platformAuthService.authAlipay(param, null)).thenReturn(expected);

        AuthResult result = channelAuthService.auth(param);

        assertSame(expected, result);
        verify(authSessionStore).deleteSession("token-4");
        verify(channelProductAuthService, never()).auth(any(), any());
    }

    @Test
    @DisplayName("auth: 产品级 session(无 source 标记)走支付产品策略")
    void auth_productSession_shouldCallChannelProductService() {
        AuthCodeParam param = newAuthCodeParam("token-5", ChannelAuthTypeEnum.WECHAT.getCode());
        AuthSession session = new AuthSession().setProduct("WECHAT_PAY");
        AuthResult expected = new AuthResult().setOpenId("openid-5");
        when(authSessionStore.loadSession("token-5")).thenReturn(session);
        when(channelProductAuthService.auth(param, session)).thenReturn(expected);

        AuthResult result = channelAuthService.auth(param);

        assertSame(expected, result);
        verify(authSessionStore).deleteSession("token-5");
        verify(platformAuthService, never()).authAlipay(any(), any());
    }

    // ==================== auth: returnPath 回填 ====================

    @Test
    @DisplayName("auth: 平台方法未回填 returnPath 时从 session 补齐")
    void auth_shouldFillReturnPathFromSessionWhenBlank() {
        AuthCodeParam param = newAuthCodeParam("token-6", ChannelAuthTypeEnum.WECHAT.getCode());
        AuthSession session = new AuthSession()
                .setSource(AuthSession.SOURCE_PLATFORM_MP)
                .setReturnPath("/aggregate/wechat/ORD");
        AuthResult expected = new AuthResult().setOpenId("openid-6");
        when(authSessionStore.loadSession("token-6")).thenReturn(session);
        when(platformAuthService.authWechatMp(param, session)).thenReturn(expected);

        AuthResult result = channelAuthService.auth(param);

        assertEquals("/aggregate/wechat/ORD", result.getReturnPath());
    }

    @Test
    @DisplayName("auth: 平台方法已回填 returnPath 时不被 session 覆盖")
    void auth_shouldNotOverrideReturnPathWhenAlreadySet() {
        AuthCodeParam param = newAuthCodeParam("token-7", ChannelAuthTypeEnum.ALIPAY.getCode());
        AuthSession session = new AuthSession()
                .setSource(AuthSession.SOURCE_PLATFORM_ALIPAY)
                .setReturnPath("/from-session");
        AuthResult expected = new AuthResult()
                .setOpenId("uid-7")
                .setReturnPath("/from-platform");
        when(authSessionStore.loadSession("token-7")).thenReturn(session);
        when(platformAuthService.authAlipay(param, session)).thenReturn(expected);

        AuthResult result = channelAuthService.auth(param);

        assertEquals("/from-platform", result.getReturnPath());
    }

    // ==================== 辅助 ====================

    /// 构造最小可用的 AuthCodeParam(仅 authToken/authType/authCode)
    private AuthCodeParam newAuthCodeParam(String authToken, String authType) {
        AuthCodeParam param = new AuthCodeParam();
        param.setAuthToken(authToken);
        param.setAuthType(authType);
        param.setAuthCode("code-x");
        return param;
    }
}
