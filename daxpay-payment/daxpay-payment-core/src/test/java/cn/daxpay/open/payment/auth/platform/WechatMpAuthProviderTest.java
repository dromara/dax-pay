package cn.daxpay.open.payment.auth.platform;

import cn.daxpay.open.payment.auth.core.AuthScene;
import cn.daxpay.open.payment.auth.core.AuthSession;
import cn.daxpay.open.payment.auth.core.AuthSessionStore;
import cn.daxpay.open.payment.unipay.param.assist.AuthCodeParam;
import cn.daxpay.open.payment.unipay.result.assist.AuthResult;
import cn.daxpay.open.payment.unipay.result.assist.AuthUrlResult;
import cn.daxpay.open.payment.wx.enums.WxAppScopeEnum;
import cn.daxpay.open.payment.wx.facade.WxAppFacade;
import cn.daxpay.open.payment.wx.facade.WxAppView;
import cn.daxpay.open.platform.capability.wechat.auth.result.WechatAuthResult;
import cn.daxpay.open.platform.capability.wechat.auth.result.WechatAuthUrlResult;
import cn.daxpay.open.platform.capability.wechat.auth.service.WechatMpAuthService;
import cn.daxpay.open.platform.core.enums.pay.channel.PayCapabilityEnum;
import cn.daxpay.open.platform.core.enums.pay.channel.ProductEnum;
import cn.daxpay.open.platform.core.enums.unipay.ChannelAuthStatusEnum;
import cn.daxpay.open.platform.core.exception.BizInfoException;
import cn.daxpay.open.platform.core.exception.operation.OperationFailException;
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
import static org.mockito.ArgumentMatchers.isNull;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/// # 微信公众号平台级认证 Provider 测试
///
/// 覆盖 [WechatMpAuthProvider] 的 generateAuthUrl / auth 内部逻辑。
/// Phase 3 迁移后配置来源从 PlatformWechatMpAuthConfig 改为 WxAppFacade 主数据。
@ExtendWith(MockitoExtension.class)
class WechatMpAuthProviderTest {

    @Mock private AuthSessionStore authSessionStore;
    @Mock private WxAppFacade wxAppFacade;
    @Mock private PlatformUrlConfigService platformUrlConfigService;
    @Mock private WechatMpAuthService wechatMpAuthService;

    @InjectMocks private WechatMpAuthProvider wechatMpAuthProvider;

    private static final String GATEWAY_BASE = "https://gw.example.com";

    /// 构建一个公众号类型的平台应用视图
    private WxAppView buildAppView(String wxAppId, String appSecret) {
        return new WxAppView(WxAppScopeEnum.PLATFORM, 1L, wxAppId,
                "official_account", appSecret, "测试公众号");
    }

    @Test
    @DisplayName("sourceCode 返回 SOURCE_PLATFORM_MP")
    void sourceCode_isPlatformMp() {
        assertEquals(AuthSession.SOURCE_PLATFORM_MP, wechatMpAuthProvider.sourceCode());
    }

    @Test
    @DisplayName("generateAuthUrl: 平台应用不存在时 facade 抛异常, 不写 session")
    void generateAuthUrl_appNotFound_shouldThrow() {
        when(wxAppFacade.resolve(isNull(), isNull(), eq(PayCapabilityEnum.WECHAT_JSAPI.getCode()),
                isNull(), eq(ProductEnum.WECHAT_PAY.getCode())))
                .thenThrow(new OperationFailException("error.payment.wx.appNotConfigured", "official_account"));

        assertThrows(OperationFailException.class,
                () -> wechatMpAuthProvider.generateAuthUrl(null));

        verify(authSessionStore, never()).saveSession(anyString(), any());
    }

    @Test
    @DisplayName("generateAuthUrl: appSecret 未配置抛 appNotConfigured, 不写 session")
    void generateAuthUrl_secretMissing_shouldThrow() {
        when(wxAppFacade.resolve(isNull(), isNull(), eq(PayCapabilityEnum.WECHAT_JSAPI.getCode()),
                isNull(), eq(ProductEnum.WECHAT_PAY.getCode())))
                .thenReturn(buildAppView("wxid", ""));

        BizInfoException ex = assertThrows(BizInfoException.class,
                () -> wechatMpAuthProvider.generateAuthUrl(null));

        assertEquals("error.payment.wx.appNotConfigured", ex.getMessageKey());
        verify(authSessionStore, never()).saveSession(anyString(), any());
    }

    @Test
    @DisplayName("generateAuthUrl: 正常路径 session.source=platform_mp, scene=platform")
    void generateAuthUrl_normal_shouldSaveSession() {
        when(wxAppFacade.resolve(isNull(), isNull(), eq(PayCapabilityEnum.WECHAT_JSAPI.getCode()),
                isNull(), eq(ProductEnum.WECHAT_PAY.getCode())))
                .thenReturn(buildAppView("wxid", "secret"));
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
        when(wxAppFacade.resolve(isNull(), isNull(), eq(PayCapabilityEnum.WECHAT_JSAPI.getCode()),
                isNull(), eq(ProductEnum.WECHAT_PAY.getCode())))
                .thenReturn(buildAppView("x", "y"));
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
        when(wxAppFacade.resolve(isNull(), isNull(), eq(PayCapabilityEnum.WECHAT_JSAPI.getCode()),
                isNull(), eq(ProductEnum.WECHAT_PAY.getCode())))
                .thenReturn(buildAppView("x", "y"));
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