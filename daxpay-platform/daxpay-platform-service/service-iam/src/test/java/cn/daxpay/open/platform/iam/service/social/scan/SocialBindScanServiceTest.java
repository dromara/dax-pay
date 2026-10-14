package cn.daxpay.open.platform.iam.service.social.scan;

import java.util.ArrayList;
import java.util.List;

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
import cn.daxpay.open.platform.iam.result.social.SocialBindScanConfirmResult;
import cn.daxpay.open.platform.iam.result.social.SocialBindScanQrResult;
import cn.daxpay.open.platform.iam.result.social.SocialBindScanStatusResult;
import cn.daxpay.open.platform.iam.result.social.SocialExchangeResult;
import cn.daxpay.open.platform.iam.result.user.UserInfoResult;
import cn.daxpay.open.platform.iam.service.social.IamUserSocialBindStore;
import cn.daxpay.open.platform.iam.service.social.SocialAuthSupport;
import cn.daxpay.open.platform.iam.service.social.SocialLoginConfigService;
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
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/// # 公众号扫码绑定(跨设备)编排测试
///
/// 覆盖 [SocialBindScanService] 票据生成/授权地址/兑换/状态轮询的编排语义:
/// - 票据生成: 仅公众号可用、跨端发起拒绝、二维码地址指向网关域名 H5 发起页;
/// - 授权地址: 回调走网关独立回调路径(不经透传页), state 一次性映射保存;
/// - 兑换: 凭票据中的用户身份落绑定(手机端无登录态), 成功后标记 success;
/// - 状态轮询: 他人票据统一按不存在返回(防探测).
///
@ExtendWith(MockitoExtension.class)
class SocialBindScanServiceTest {

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
    private SocialBindScanStore socialBindScanStore;

    @Mock
    private SocialAuthRequest socialAuthRequest;

    /// 被测对象: 真实支撑(不 mock 授权链路原语, 保持 redirectUri 拼装断言语义) + 编排 service
    private SocialAuthSupport socialAuthSupport;

    private SocialBindScanService bindScanService;

    @BeforeEach
    void setUp() {
        socialAuthSupport = new SocialAuthSupport(socialAuthRequestFactory, null,
                socialLoginConfigService, platformUrlConfigService, platformWechatMpAuthConfigService,
                userQueryService, platformStarterProperties, userInfoStatusChecks);
        bindScanService = new SocialBindScanService(socialAuthSupport, socialBindScanStore, socialBindStore,
                userQueryService);
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
                () -> bindScanService.createBindScanTicket("dingTalk", "admin"));
        assertTrue(exception.getMessage().contains("scanNotSupported"));
    }

    @Test
    @DisplayName("扫码绑定: 跨端发起被拒绝(用户不属于该身份域)")
    void bindScanRejectsClientMismatch() {
        try (MockedStatic<SecurityUtil> mocked = mockStatic(SecurityUtil.class)) {
            mocked.when(SecurityUtil::getUserId).thenReturn(1L);
            when(userQueryService.findById(1L)).thenReturn(new UserInfoResult().setId(1L).setClientCode("merchant"));

            OperationFailException exception = assertThrows(OperationFailException.class,
                    () -> bindScanService.createBindScanTicket("weChat", "admin"));
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

            SocialBindScanQrResult result = bindScanService.createBindScanTicket("weChat", "admin");

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
                () -> bindScanService.buildBindScanAuthUrl("bad-ticket"));
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

        String url = bindScanService.buildBindScanAuthUrl("ticket-1");

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
                () -> bindScanService.exchangeBindScan("code-1", "bad-state"));
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

        SocialExchangeResult result = bindScanService.exchangeBindScan("code-1", "state-1");

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

            SocialBindScanStatusResult result = bindScanService.queryBindScanStatus("ticket-1");

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

            SocialBindScanStatusResult result = bindScanService.queryBindScanStatus("ticket-1");

            assertEquals(SocialBindScanStore.STATUS_SUCCESS, result.getStatus());
        }
    }

    @Test
    @DisplayName("扫码绑定确认信息: 返回脱敏的目标账号名(手机号按通行规则脱敏)")
    void bindScanConfirmInfoReturnsMaskedAccount() {
        when(socialBindScanStore.get("ticket-1")).thenReturn(scanContext(1L));
        when(userQueryService.findById(1L)).thenReturn(adminUserInfo(1L).setAccount("13812345678"));

        SocialBindScanConfirmResult result = bindScanService.queryBindScanConfirm("ticket-1");

        assertEquals("138****5678", result.getMaskedAccount());
    }

    @Test
    @DisplayName("扫码绑定确认信息: 打开确认页顺带把 waiting 标记为 scanned(已扫码中间态)")
    void bindScanConfirmInfoMarksScanned() {
        when(socialBindScanStore.get("ticket-1")).thenReturn(scanContext(1L));
        when(userQueryService.findById(1L)).thenReturn(adminUserInfo(1L).setAccount("13812345678"));

        bindScanService.queryBindScanConfirm("ticket-1");

        verify(socialBindScanStore).markScanned(eq("ticket-1"), any(SocialBindScanStore.BindScanContext.class));
    }

    @Test
    @DisplayName("扫码绑定确认信息: 非 waiting 状态不重复标记(终态不回退为 scanned)")
    void bindScanConfirmInfoNoRescanOnNonWaiting() {
        when(socialBindScanStore.get("ticket-1")).thenReturn(scanContext(1L).setStatus(SocialBindScanStore.STATUS_SUCCESS));
        when(userQueryService.findById(1L)).thenReturn(adminUserInfo(1L).setAccount("13812345678"));

        bindScanService.queryBindScanConfirm("ticket-1");

        verify(socialBindScanStore, never()).markScanned(anyString(), any(SocialBindScanStore.BindScanContext.class));
    }

    @Test
    @DisplayName("扫码绑定确认信息: 票据无效报错")
    void bindScanConfirmInfoRejectsInvalidTicket() {
        when(socialBindScanStore.get("bad-ticket")).thenReturn(null);

        OperationFailException exception = assertThrows(OperationFailException.class,
                () -> bindScanService.queryBindScanConfirm("bad-ticket"));
        assertTrue(exception.getMessage().contains("scanTicketInvalid"));
    }

    @Test
    @DisplayName("取消扫码绑定: waiting 状态标记 cancelled")
    void bindScanCancelMarksCancelled() {
        when(socialBindScanStore.get("ticket-1")).thenReturn(scanContext(1L));

        bindScanService.cancelBindScan("ticket-1");

        verify(socialBindScanStore).markCancelled(eq("ticket-1"), any(SocialBindScanStore.BindScanContext.class));
    }

    @Test
    @DisplayName("取消扫码绑定: scanned 中间态同样可取消(确认页在扫码后展示)")
    void bindScanCancelFromScanned() {
        when(socialBindScanStore.get("ticket-1")).thenReturn(scanContext(1L).setStatus(SocialBindScanStore.STATUS_SCANNED));

        bindScanService.cancelBindScan("ticket-1");

        verify(socialBindScanStore).markCancelled(eq("ticket-1"), any(SocialBindScanStore.BindScanContext.class));
    }

    @Test
    @DisplayName("取消扫码绑定: 票据不存在或非 waiting 时空操作(不覆盖已完成状态)")
    void bindScanCancelNoOpOnMissingOrNonWaiting() {
        bindScanService.cancelBindScan("missing-ticket");
        bindScanService.cancelBindScan(null);
        when(socialBindScanStore.get("ticket-1")).thenReturn(scanContext(1L).setStatus(SocialBindScanStore.STATUS_SUCCESS));

        bindScanService.cancelBindScan("ticket-1");

        verify(socialBindScanStore, never()).markCancelled(anyString(), any(SocialBindScanStore.BindScanContext.class));
    }

    @Test
    @DisplayName("扫码绑定状态: cancelled 透出(手机端确认页已取消)")
    void bindScanStatusPassthroughCancelled() {
        SocialBindScanStore.BindScanContext context = scanContext(1L).setStatus(SocialBindScanStore.STATUS_CANCELLED);
        when(socialBindScanStore.get("ticket-1")).thenReturn(context);
        try (MockedStatic<SecurityUtil> mocked = mockStatic(SecurityUtil.class)) {
            mocked.when(SecurityUtil::getUserId).thenReturn(1L);

            SocialBindScanStatusResult result = bindScanService.queryBindScanStatus("ticket-1");

            assertEquals(SocialBindScanStatusResult.CANCELLED, result.getStatus());
        }
    }

    @Test
    @DisplayName("扫码绑定状态: scanned 中间态透出(PC 端展示扫码成功待操作)")
    void bindScanStatusPassthroughScanned() {
        SocialBindScanStore.BindScanContext context = scanContext(1L).setStatus(SocialBindScanStore.STATUS_SCANNED);
        when(socialBindScanStore.get("ticket-1")).thenReturn(context);
        try (MockedStatic<SecurityUtil> mocked = mockStatic(SecurityUtil.class)) {
            mocked.when(SecurityUtil::getUserId).thenReturn(1L);

            SocialBindScanStatusResult result = bindScanService.queryBindScanStatus("ticket-1");

            assertEquals(SocialBindScanStatusResult.SCANNED, result.getStatus());
        }
    }
}
