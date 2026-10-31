package cn.daxpay.open.channel.mock.strategy;

import cn.daxpay.open.channel.mock.client.MockChannelClient;
import cn.daxpay.open.channel.mock.client.credential.MockSdkCredential;
import cn.daxpay.open.channel.mock.client.enums.MockPayBodyType;
import cn.daxpay.open.channel.mock.client.req.MockPayReq;
import cn.daxpay.open.channel.mock.client.resp.MockPayResp;
import cn.daxpay.open.channel.mock.service.MockCredentialSupport;
import cn.daxpay.open.channel.mock.service.MockPayService;
import cn.daxpay.open.channel.mock.strategy.pay.MockPayStrategy;
import cn.daxpay.open.payment.auth.core.AppScopeEnum;
import cn.daxpay.open.payment.common.result.DaxResult;
import cn.daxpay.open.payment.strategy.pay.PayStrategyContext;
import cn.daxpay.open.payment.trade.order.entity.NormalPayOrder;
import cn.daxpay.open.payment.trade.order.entity.PayTrade;
import cn.daxpay.open.payment.trade.runtime.bo.PayTradeResultBo;
import cn.daxpay.open.payment.unipay.param.trade.pay.NormalPayParam;
import cn.daxpay.open.payment.wx.facade.WxAppFacade;
import cn.daxpay.open.payment.wx.facade.WxAppView;
import cn.daxpay.open.platform.core.code.CommonErrorCode;
import cn.daxpay.open.platform.core.enums.pay.channel.ProductEnum;
import cn.daxpay.open.platform.core.enums.unipay.PayBodyTypeEnum;
import cn.daxpay.open.platform.core.exception.BizInfoException;
import cn.daxpay.open.platform.system.entity.config.platform.infra.PlatformUrlConfig;
import cn.daxpay.open.platform.system.service.config.infra.PlatformUrlConfigService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

/// # mock 通道支付微信应用解析测试
///
/// 覆盖 [MockPayStrategy] doBeforePay 的解析回填与 [MockPayService] 的请求装配(open 5 参
/// [WxAppFacade#resolve], 无服务商形态):
/// 1. 直连(MOCK_PAY) wx_jsapi 解析成功回填 channelAppId(显式优先 + 产品码 MOCK_PAY);
/// 2. 参数未携带商户号/通道商户号时回落上下文(交易凭证)取值, 不假设入参恒有值;
/// 3. 解析失败异常原样上抛, 整单失败不兜底回落;
/// 4. 非 JSAPI 方式(扫码/付款码/H5/APP/支付宝系等)完全不触发解析, 零行为变化;
/// 5. 服务层请求装配: 过期时间按东八区格式化、way/provider 映射、appId 回落容器快照。
///
/// 全程 Mockito 隔离 [WxAppFacade], 不连库、不起 Spring。
@ExtendWith(MockitoExtension.class)
class MockPayWxAppResolveTest {

    private static final String MCH_NO = "M1789005525459";
    private static final String CHANNEL_MCH_NO = "MOCK-CM-001";
    private static final String CAPABILITY = "WECHAT_JSAPI";
    private static final String DIRECT_APP_ID = "wx-direct-app";

    @Mock
    private MockPayService mockPayService;
    @Mock
    private WxAppFacade wxAppFacade;
    @Mock
    private MockChannelClient mockChannelClient;
    @Mock
    private PlatformUrlConfigService platformUrlConfigService;
    @Mock
    private MockCredentialSupport mockCredentialSupport;

    /// 直连策略(MOCK_PAY)
    private MockPayStrategy newStrategy() {
        return new MockPayStrategy(mockPayService, wxAppFacade);
    }

    /// 微信 JSAPI 下单参数(显式 channelAppId 由用例自定)
    private NormalPayParam wxJsapiParam(String channelAppId, String method) {
        NormalPayParam payParam = new NormalPayParam();
        payParam.setMchNo(MCH_NO);
        payParam.setChannelMchNo(CHANNEL_MCH_NO);
        payParam.setCapability(CAPABILITY);
        payParam.setMethod(method);
        payParam.setChannelAppId(channelAppId);
        payParam.setOpenId("o-openid-1");
        return payParam;
    }

    private PayStrategyContext context(NormalPayParam payParam) {
        return new PayStrategyContext().setPayParam(payParam);
    }

    private WxAppView platformView(String wxAppId) {
        return new WxAppView(AppScopeEnum.PLATFORM, 1L, wxAppId, "MINI", "secret", "平台应用");
    }

    @Test
    @DisplayName("直连 wx_jsapi: 解析成功回填 channelAppId, 显式值优先透传且产品码为 MOCK_PAY")
    void directPay_wxJsapi_resolvesAndBackfillsChannelAppId() {
        // 未显式指定 channelAppId: 走能力绑/产品默认应用解析(open 5 参签名, 无 plus 的第 3 个 null 槽位)
        NormalPayParam blankParam = wxJsapiParam(null, "wechat_jsapi");
        when(wxAppFacade.resolve(MCH_NO, CHANNEL_MCH_NO, CAPABILITY, null,
                ProductEnum.MOCK_PAY.getCode())).thenReturn(platformView(DIRECT_APP_ID));
        newStrategy().doBeforePay(context(blankParam));
        assertEquals(DIRECT_APP_ID, blankParam.getChannelAppId(), "解析结果应回填 channelAppId 供订单落库");

        // 显式指定 channelAppId: 原样透传给 resolve(显式优先且必须已登记)
        NormalPayParam explicitParam = wxJsapiParam(DIRECT_APP_ID, "wechat_jsapi");
        when(wxAppFacade.resolve(MCH_NO, CHANNEL_MCH_NO, CAPABILITY, DIRECT_APP_ID,
                ProductEnum.MOCK_PAY.getCode())).thenReturn(platformView(DIRECT_APP_ID));
        newStrategy().doBeforePay(context(explicitParam));
        assertEquals(DIRECT_APP_ID, explicitParam.getChannelAppId());
    }

    @Test
    @DisplayName("参数无商户号/通道商户号: 回落支付单快照, 不假设 NormalPayParam 恒有值")
    void directPay_missingMchNoFallsBackToTrade() {
        NormalPayParam payParam = wxJsapiParam(null, "wechat_mini");
        payParam.setMchNo(null);
        payParam.setChannelMchNo(null);
        PayTrade trade = mock(PayTrade.class);
        when(trade.getMchNo()).thenReturn(MCH_NO);
        when(trade.getChannelMchNo()).thenReturn(CHANNEL_MCH_NO);
        when(wxAppFacade.resolve(MCH_NO, CHANNEL_MCH_NO, CAPABILITY, null,
                ProductEnum.MOCK_PAY.getCode())).thenReturn(platformView(DIRECT_APP_ID));

        newStrategy().doBeforePay(context(payParam).setTrade(trade));

        assertEquals(DIRECT_APP_ID, payParam.getChannelAppId());
    }

    @Test
    @DisplayName("解析失败: 异常原样上抛整单失败, 不兜底回落")
    void resolveFailure_propagatesAndFailsOrder() {
        NormalPayParam payParam = wxJsapiParam(null, "wechat_jsapi");
        when(wxAppFacade.resolve(MCH_NO, CHANNEL_MCH_NO, CAPABILITY, null,
                ProductEnum.MOCK_PAY.getCode()))
                .thenThrow(new BizInfoException(CommonErrorCode.UN_SUPPORTED_OPERATE,
                        "未配置 {0} 对应的应用，请前往通道商户应用配置页绑定", CAPABILITY));
        assertThrows(BizInfoException.class, () -> newStrategy().doBeforePay(context(payParam)),
                "解析失败应整单失败, 不应回落商户系统应用号");
        assertNull(payParam.getChannelAppId());
    }

    @Test
    @DisplayName("非 JSAPI 方式(扫码/付款码/H5/APP/支付宝系等): 完全不调解析, 零行为变化")
    void nonJsapiMethods_skipResolveEntirely() {
        for (String method : new String[]{"wechat_qr", "wechat_barcode", "wechat_h5", "wechat_app",
                "alipay_jsapi", "alipay_qr", "union_barcode", "douyin_h5", "aggregate_pay_qrcode"}) {
            NormalPayParam payParam = wxJsapiParam(null, method);
            newStrategy().doBeforePay(context(payParam));
            assertNull(payParam.getChannelAppId(), "非 JSAPI 方式不应回填: " + method);
        }
        // 整个循环未命中 JSAPI 族, 解析门面零交互
        verifyNoInteractions(wxAppFacade);
    }

    @Test
    @DisplayName("isWxJsapiFamily 判定: wechat_jsapi/wechat_mini/wechat_cashier 命中, 其余方式不命中")
    void wxJsapiFamilyMethodScope() {
        assertTrue(MockPayService.isWxJsapiFamily("wechat_jsapi"));
        assertTrue(MockPayService.isWxJsapiFamily("wechat_mini"));
        assertTrue(MockPayService.isWxJsapiFamily("wechat_cashier"), "小程序收银台同属小程序内拉起, 已纳入解析族");
        assertFalse(MockPayService.isWxJsapiFamily("wechat_qr"));
        assertFalse(MockPayService.isWxJsapiFamily("alipay_jsapi"));
        assertFalse(MockPayService.isWxJsapiFamily("aggregate_pay_qrcode"));
    }

    @Test
    @DisplayName("doPay: 直接委托 MockPayService(上下文三件套原样传递)")
    void doPay_delegatesToService() {
        PayTrade order = mock(PayTrade.class);
        NormalPayOrder container = mock(NormalPayOrder.class);
        NormalPayParam payParam = wxJsapiParam(DIRECT_APP_ID, "wechat_jsapi");
        PayStrategyContext ctx = context(payParam).setTrade(order).setNormalOrder(container);
        when(mockPayService.pay(eq(order), eq(payParam), eq(container))).thenReturn(new PayTradeResultBo());

        newStrategy().doPay(ctx);

        verify(mockPayService).pay(order, payParam, container);
        // doPay 不再触发任何应用解析
        verifyNoInteractions(wxAppFacade);
    }

    @Test
    @DisplayName("服务层请求装配: 东八区过期时间/way/provider/mchNo/appId 回落容器快照")
    void serviceAssemblesRequest() {
        NormalPayParam payParam = new NormalPayParam();
        payParam.setMchNo(MCH_NO);
        payParam.setMethod("wechat_qr");
        payParam.setTitle("扫码单");
        payParam.setAmount(100L);
        payParam.setBizOrderNo("BIZ-001");
        payParam.setDescription("描述");
        payParam.setAttach("attach-1");
        // 无时区偏移固定东八区, 对外报文口径
        payParam.setExpiredTime(OffsetDateTime.of(2026, 10, 31, 10, 0, 0, 0, ZoneOffset.ofHours(8)));

        PayTrade order = mock(PayTrade.class);
        when(order.getTradeNo()).thenReturn("T20261031001");
        when(order.getMchNo()).thenReturn(MCH_NO);
        when(order.getChannelMchNo()).thenReturn(CHANNEL_MCH_NO);
        NormalPayOrder container = mock(NormalPayOrder.class);
        when(container.getAppId()).thenReturn("app-parent");
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
        resp.setFinishTime(OffsetDateTime.parse("2026-10-31T02:00:00Z"));
        ArgumentCaptor<MockPayReq> captor = ArgumentCaptor.forClass(MockPayReq.class);
        when(mockChannelClient.pay(captor.capture())).thenReturn(new DaxResult<>(0, resp, "ok"));

        MockPayService service = new MockPayService(mockChannelClient, platformUrlConfigService, mockCredentialSupport);
        PayTradeResultBo bo = service.pay(order, payParam, container);

        MockPayReq req = captor.getValue();
        assertEquals("T20261031001", req.getOutTradeNo());
        assertEquals("2026-10-31 10:00:00", req.getExpireTime(), "过期时间应固定按东八区格式化");
        assertEquals("QR", req.getWay());
        assertEquals("qr", req.getMethod());
        assertEquals("WECHAT", req.getProvider());
        // 未显式 channelAppId 且非 JSAPI: 回落容器应用号快照
        assertEquals("app-parent", req.getAppId());
        assertEquals(MCH_NO, req.getMchNo());
        assertEquals(CHANNEL_MCH_NO, req.getChannelMchNo());
        assertEquals("http://127.0.0.1:9999/unipay/callback/" + MCH_NO + "/" + CHANNEL_MCH_NO + "/mock",
                req.getNotifyUrl());
        // 响应映射: URL→LINK, 完成时间直取网关 OffsetDateTime
        assertEquals("MOCK-T-1", bo.getOutOrderNo());
        assertTrue(bo.isComplete());
        assertEquals(PayBodyTypeEnum.LINK, bo.getPayBodyType());
        assertEquals("http://127.0.0.1:10888/confirm", bo.getPayBody());
        assertEquals(OffsetDateTime.parse("2026-10-31T02:00:00Z"), bo.getFinishTime());
    }
}
