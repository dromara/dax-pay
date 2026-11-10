package cn.daxpay.open.channel.mock.strategy;

import cn.daxpay.open.channel.mock.client.MockChannelClient;
import cn.daxpay.open.channel.mock.client.credential.MockSdkCredential;
import cn.daxpay.open.channel.mock.client.enums.MockPayBodyType;
import cn.daxpay.open.channel.mock.client.req.MockPayReq;
import cn.daxpay.open.channel.mock.client.resp.MockPayResp;
import cn.daxpay.open.channel.mock.service.MockCredentialSupport;
import cn.daxpay.open.channel.mock.service.MockPayService;
import cn.daxpay.open.channel.mock.service.isv.MockIsvPaymentSwitchService;
import cn.daxpay.open.channel.mock.strategy.pay.MockIsvPayStrategy;
import cn.daxpay.open.payment.auth.core.AppScopeEnum;
import cn.daxpay.open.payment.common.result.DaxResult;
import cn.daxpay.open.payment.strategy.pay.PayStrategyContext;
import cn.daxpay.open.payment.trade.order.entity.NormalPayOrder;
import cn.daxpay.open.payment.trade.order.entity.PayTrade;
import cn.daxpay.open.payment.trade.runtime.bo.PayTradeResultBo;
import cn.daxpay.open.payment.unipay.param.trade.pay.NormalPayParam;
import cn.daxpay.open.payment.wx.facade.WxAppFacade;
import cn.daxpay.open.payment.wx.facade.WxAppView;
import cn.daxpay.open.payment.wx.facade.WxIsvAppPair;
import cn.daxpay.open.platform.core.enums.pay.channel.ProductEnum;
import cn.daxpay.open.platform.system.entity.config.platform.infra.PlatformUrlConfig;
import cn.daxpay.open.platform.system.service.config.infra.PlatformUrlConfigService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.OffsetDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

/// # mock 服务商支付微信双应用下传测试
///
/// 覆盖 [MockIsvPayStrategy] 解析服务商双应用(open 5 参 [WxAppFacade#resolveIsvPair])后的
/// 上下文缓存与透传, 以及 [MockPayService] 将 sp/sub 应用装配进 [MockPayReq]:
/// 1. 微信 JSAPI 族: 回填 channelAppId(sub 优先) + 双应用缓存到上下文 channelConfig;
/// 2. 非 JSAPI 族: 不解析、不缓存, 仅校验服务商支付开关;
/// 3. doPay: 上下文缓存的双应用原样传给服务层;
/// 4. 服务层: 双应用齐全/仅 sp/直连三参入口三种装配口径。
///
/// 全程 Mockito 隔离门面与子应用客户端, 不连库、不起 Spring。
@ExtendWith(MockitoExtension.class)
class MockIsvPayDualAppTest {

    private static final String MCH_NO = "M1789005525459";
    private static final String CHANNEL_MCH_NO = "MOCK-ISV-CM-001";
    private static final String CAPABILITY = "WECHAT_JSAPI";
    private static final String SP_APP_ID = "wx-sp-app";
    private static final String SUB_APP_ID = "wx-sub-app";

    @Mock
    private MockPayService mockPayService;
    @Mock
    private MockIsvPaymentSwitchService mockIsvPaymentSwitchService;
    @Mock
    private WxAppFacade wxAppFacade;
    @Mock
    private MockChannelClient mockChannelClient;
    @Mock
    private PlatformUrlConfigService platformUrlConfigService;
    @Mock
    private MockCredentialSupport mockCredentialSupport;

    /// 服务商策略(MOCK_ISV)
    private MockIsvPayStrategy newStrategy() {
        return new MockIsvPayStrategy(mockPayService, mockIsvPaymentSwitchService, wxAppFacade);
    }

    /// 微信下单参数(方式由用例自定, 未显式指定 channelAppId)
    private NormalPayParam payParam(String method) {
        NormalPayParam payParam = new NormalPayParam();
        payParam.setMchNo(MCH_NO);
        payParam.setChannelMchNo(CHANNEL_MCH_NO);
        payParam.setCapability(CAPABILITY);
        payParam.setMethod(method);
        payParam.setOpenId("o-openid-1");
        payParam.setTitle("服务商单");
        payParam.setAmount(100L);
        return payParam;
    }

    private WxAppView view(AppScopeEnum scope, String wxAppId) {
        return new WxAppView(scope, 1L, wxAppId, "MINI", "secret", "应用");
    }

    /// sp(平台档) + sub(商户档) 双应用对; sub 传 null 表示未绑定商户档应用
    private WxIsvAppPair pair(String subAppId) {
        return new WxIsvAppPair(view(AppScopeEnum.PLATFORM, SP_APP_ID),
                subAppId == null ? null : view(AppScopeEnum.MERCHANT, subAppId));
    }

    @Test
    @DisplayName("微信 JSAPI: 回填 channelAppId(sub 优先) 并把双应用缓存到上下文")
    void wxJsapi_backfillsSubAndCachesPair() {
        NormalPayParam payParam = payParam("wechat_jsapi");
        WxIsvAppPair pair = pair(SUB_APP_ID);
        when(wxAppFacade.resolveIsvPair(MCH_NO, CHANNEL_MCH_NO, CAPABILITY, null,
                ProductEnum.MOCK_ISV.getCode())).thenReturn(pair);
        PayStrategyContext ctx = new PayStrategyContext().setPayParam(payParam);

        newStrategy().doBeforePay(ctx);

        verify(mockIsvPaymentSwitchService).assertPaymentEnabled();
        assertEquals(SUB_APP_ID, payParam.getChannelAppId(), "回填值应优先取 sub_appid");
        assertSame(pair, ctx.getChannelConfig(WxIsvAppPair.class), "双应用应缓存到上下文供 doPay 透传");
    }

    @Test
    @DisplayName("非 JSAPI 方式: 只校验服务商支付开关, 不解析也不缓存双应用")
    void nonJsapi_skipsResolveAndCache() {
        NormalPayParam payParam = payParam("wechat_qr");
        PayStrategyContext ctx = new PayStrategyContext().setPayParam(payParam);

        newStrategy().doBeforePay(ctx);

        verify(mockIsvPaymentSwitchService).assertPaymentEnabled();
        verifyNoInteractions(wxAppFacade);
        assertNull(payParam.getChannelAppId());
        assertNull(ctx.getChannelConfig(WxIsvAppPair.class));
    }

    @Test
    @DisplayName("doPay: 上下文缓存的双应用原样传给服务层")
    void doPay_passesCachedPair() {
        PayTrade order = mock(PayTrade.class);
        NormalPayOrder container = mock(NormalPayOrder.class);
        NormalPayParam payParam = payParam("wechat_jsapi");
        WxIsvAppPair pair = pair(SUB_APP_ID);
        PayStrategyContext ctx = new PayStrategyContext().setPayParam(payParam)
                .setTrade(order).setNormalOrder(container).setChannelConfig(pair);
        when(mockPayService.pay(order, payParam, container, pair)).thenReturn(new PayTradeResultBo());

        newStrategy().doPay(ctx);

        verify(mockPayService).pay(order, payParam, container, pair);
    }

    @Test
    @DisplayName("服务层装配: 双应用齐全下传 sp+sub, 仅 sp 时 sub 为空, 三参直连入口均不传")
    void service_assemblesSpAndSubAppIds() {
        PayTrade order = mock(PayTrade.class);
        when(order.getTradeNo()).thenReturn("T20261108001");
        when(order.getMchNo()).thenReturn(MCH_NO);
        when(mockCredentialSupport.build(CHANNEL_MCH_NO)).thenReturn(new MockSdkCredential());
        PlatformUrlConfig urlConfig = new PlatformUrlConfig();
        urlConfig.setBackendBaseUrl("http://127.0.0.1:9999");
        when(platformUrlConfigService.getUrlConfig()).thenReturn(urlConfig);
        MockPayResp resp = new MockPayResp();
        resp.setTradeNo("MOCK-T-1");
        resp.setComplete(Boolean.TRUE);
        resp.setPayBodyType(MockPayBodyType.URL);
        resp.setPayBody("http://127.0.0.1:10888/confirm");
        resp.setAmount(100L);
        resp.setFinishTime(OffsetDateTime.parse("2026-11-08T02:00:00Z"));
        ArgumentCaptor<MockPayReq> captor = ArgumentCaptor.forClass(MockPayReq.class);
        when(mockChannelClient.pay(captor.capture())).thenReturn(new DaxResult<>(0, resp, "ok"));
        MockPayService service = new MockPayService(mockChannelClient, platformUrlConfigService, mockCredentialSupport);

        // 双应用齐全: appId 为策略层回填的 sub, sp/sub 分别下传
        NormalPayParam fullParam = payParam("wechat_jsapi");
        fullParam.setChannelAppId(SUB_APP_ID);
        service.pay(order, fullParam, null, pair(SUB_APP_ID));
        MockPayReq fullReq = captor.getValue();
        assertEquals(SUB_APP_ID, fullReq.getAppId());
        assertEquals(SP_APP_ID, fullReq.getSpAppId());
        assertEquals(SUB_APP_ID, fullReq.getSubAppId());

        // 仅 sp(未绑定商户档应用): sub 不传
        NormalPayParam spOnlyParam = payParam("wechat_mini");
        spOnlyParam.setChannelAppId(SP_APP_ID);
        service.pay(order, spOnlyParam, null, pair(null));
        MockPayReq spOnlyReq = captor.getValue();
        assertEquals(SP_APP_ID, spOnlyReq.getSpAppId());
        assertNull(spOnlyReq.getSubAppId());

        // 三参入口(直连及其余方式): 不携带双应用
        NormalPayParam directParam = payParam("wechat_jsapi");
        directParam.setChannelAppId(SP_APP_ID);
        service.pay(order, directParam, null);
        MockPayReq directReq = captor.getValue();
        assertNull(directReq.getSpAppId());
        assertNull(directReq.getSubAppId());
    }
}
