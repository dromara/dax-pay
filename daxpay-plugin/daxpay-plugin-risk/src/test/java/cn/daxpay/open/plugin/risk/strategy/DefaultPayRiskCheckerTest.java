package cn.daxpay.open.plugin.risk.strategy;

import cn.daxpay.open.payment.strategy.risk.PayRiskCheckContext;
import cn.daxpay.open.payment.strategy.risk.RegionCodeResolver;
import cn.daxpay.open.platform.capability.audit.log.service.ip2region.IpRegion;
import cn.daxpay.open.platform.capability.audit.log.service.ip2region.IpToRegionService;
import cn.daxpay.open.platform.core.enums.pay.channel.ChannelEnum;
import cn.daxpay.open.platform.core.exception.BizInfoException;
import cn.daxpay.open.plugin.risk.entity.PayBlacklist;
import cn.daxpay.open.plugin.risk.enums.PayBlacklistTypeEnum;
import cn.daxpay.open.plugin.risk.service.PayBlacklistService;
import cn.daxpay.open.plugin.risk.service.PayRiskHitService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

/// # 默认支付风控检查器测试
///
/// 聚焦两条主路径:
/// 1. 海外 IP 地域拦截(网段短路 / xdb 文本兜底 / 海外阻断) — 覆盖本次「私网 IP 误判海外」修复回归
/// 2. 黑名单命中(IP / 支付宝 userId / 微信 openId) 与事前阻断 / 事后补录
///
/// 通过公共 SPI [DefaultPayRiskChecker#checkBeforePay] / [checkAfterPay] 验证,
/// 不直接调用 private 方法, 保证测试的是真实调用链。
///
/// ## strictness 说明
/// `@MockitoSettings(strictness = Strictness.LENIENT)`: `findActive` 在 setUp 中预先 stub 为「未命中」,
/// 但部分用例(空 IP / 未命中 / 网段短路)不会触达, strict 模式会误报未使用 stub, 故放宽。
/// 各用例仍由精确 verify 断言交互。
@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class DefaultPayRiskCheckerTest {

    private static final String IP_TYPE = PayBlacklistTypeEnum.IP.getCode();
    private static final String ALIPAY_USER_TYPE = PayBlacklistTypeEnum.ALIPAY_USER.getCode();
    private static final String WECHAT_OPENID_TYPE = PayBlacklistTypeEnum.WECHAT_OPENID.getCode();
    private static final String OVERSEAS_IP_TYPE = PayBlacklistTypeEnum.OVERSEAS_IP.getCode();
    private static final String PROVINCE_TYPE = PayBlacklistTypeEnum.PROVINCE.getCode();
    private static final String CITY_TYPE = PayBlacklistTypeEnum.CITY.getCode();
    private static final String BLACKLIST_MSG_KEY = "pay.error.risk.blacklist";

    @Mock
    private PayBlacklistService payBlacklistService;
    @Mock
    private PayRiskHitService payRiskHitService;
    @Mock
    private IpToRegionService ipToRegionService;
    @Mock
    private RegionCodeResolver regionCodeResolver;

    private DefaultPayRiskChecker checker;

    @BeforeEach
    void setUp() {
        checker = new DefaultPayRiskChecker(payBlacklistService, payRiskHitService, ipToRegionService, regionCodeResolver);
        // 名单默认不命中, 让流程走到海外检查; 个别用例用 eq() 精确覆写为命中
        lenient().when(payBlacklistService.findActive(anyString(), anyString(), any()))
                .thenReturn(Optional.empty());
    }

    // ==================== 海外 IP 拦截 ====================

    @Test
    @DisplayName("海外检查: 空 IP 不查库不记录")
    void overseasIp_blankIp_shouldSkipWithoutLookup() {
        PayRiskCheckContext ctx = overseasCtx("");

        assertDoesNotThrow(() -> checker.checkBeforePay(ctx));

        verify(ipToRegionService, never()).getRegionByIp(anyString());
        verifyNoInteractions(payRiskHitService);
    }

    @Test
    @DisplayName("海外检查: 私网 192.168 网段直通放行(本次修复回归核心)")
    void overseasIp_privateLan192_shouldPassThroughWithoutLookup() {
        PayRiskCheckContext ctx = overseasCtx("192.168.1.123");

        assertDoesNotThrow(() -> checker.checkBeforePay(ctx));

        // 网段判定直接放行, 根本不查库
        verify(ipToRegionService, never()).getRegionByIp(anyString());
        verifyNoInteractions(payRiskHitService);
    }

    @Test
    @DisplayName("海外检查: 私网 10/8 网段直通放行")
    void overseasIp_privateLan10_shouldPassThroughWithoutLookup() {
        assertDoesNotThrow(() -> checker.checkBeforePay(overseasCtx("10.0.0.5")));

        verify(ipToRegionService, never()).getRegionByIp(anyString());
        verifyNoInteractions(payRiskHitService);
    }

    @Test
    @DisplayName("海外检查: 私网 172.16/12 网段直通放行")
    void overseasIp_privateLan172_shouldPassThroughWithoutLookup() {
        assertDoesNotThrow(() -> checker.checkBeforePay(overseasCtx("172.16.0.1")));

        verify(ipToRegionService, never()).getRegionByIp(anyString());
        verifyNoInteractions(payRiskHitService);
    }

    @Test
    @DisplayName("海外检查: 回环地址直通放行")
    void overseasIp_loopback_shouldPassThroughWithoutLookup() {
        assertDoesNotThrow(() -> checker.checkBeforePay(overseasCtx("127.0.0.1")));

        verify(ipToRegionService, never()).getRegionByIp(anyString());
        verifyNoInteractions(payRiskHitService);
    }

    @Test
    @DisplayName("海外检查: IPv6 回环直通放行(ipv6MatchEnabled 默认关闭)")
    void overseasIp_ipv6Loopback_shouldPassThroughWithoutLookup() {
        assertDoesNotThrow(() -> checker.checkBeforePay(overseasCtx("::1")));

        verify(ipToRegionService, never()).getRegionByIp(anyString());
        verifyNoInteractions(payRiskHitService);
    }

    @Test
    @DisplayName("海外检查: IPv6 公网地址直通放行(ipv6MatchEnabled 默认关闭)")
    void overseasIp_ipv6Public_shouldPassThroughWithoutLookup() {
        assertDoesNotThrow(() -> checker.checkBeforePay(overseasCtx("2409:8a20:210:0:0:0:0:1")));

        verify(ipToRegionService, never()).getRegionByIp(anyString());
        verifyNoInteractions(payRiskHitService);
    }

    @Test
    @DisplayName("海外检查: ipv6MatchEnabled 开启时 IPv6 海外地址命中拦截")
    void overseasIp_ipv6MatchEnabled_shouldLookupAndBlock() {
        // 开关开启: IPv6 走 getRegionByIp 查询
        IpRegion overseas = new IpRegion();
        overseas.setCountry("United States");
        when(ipToRegionService.getRegionByIp("2606:4700:4700::1111")).thenReturn(overseas);

        assertThrows(BizInfoException.class,
                () -> checker.checkBeforePay(overseasCtx("2606:4700:4700::1111").setIpv6MatchEnabled(true)));

        verify(ipToRegionService).getRegionByIp("2606:4700:4700::1111");
        verify(payRiskHitService).recordHit(any(), eq(OVERSEAS_IP_TYPE), eq("2606:4700:4700::1111"), eq(null));
    }

    @Test
    @DisplayName("海外检查: ipv6MatchEnabled 开启时 IPv6 国内地址放行")
    void overseasIp_ipv6MatchEnabled_chinaIpShouldPass() {
        // 开关开启: IPv6 走查询, 结果为中国 → 放行
        IpRegion china = new IpRegion();
        china.setCountry("中国");
        when(ipToRegionService.getRegionByIp("240e:3b7::1")).thenReturn(china);

        assertDoesNotThrow(() -> checker.checkBeforePay(overseasCtx("240e:3b7::1").setIpv6MatchEnabled(true)));

        verify(ipToRegionService).getRegionByIp("240e:3b7::1");
        verify(payRiskHitService, never()).recordHit(any(), eq(OVERSEAS_IP_TYPE), anyString(), any());
    }

    @Test
    @DisplayName("海外检查: xdb 查询失败(region=null)放行不记录")
    void overseasIp_lookupReturnsNull_shouldPass() {
        when(ipToRegionService.getRegionByIp("8.8.8.8")).thenReturn(null);

        assertDoesNotThrow(() -> checker.checkBeforePay(overseasCtx("8.8.8.8")));

        verify(payRiskHitService, never()).recordHit(any(), anyString(), anyString(), any());
    }

    @Test
    @DisplayName("海外检查: 国内 IP 放行不记录")
    void overseasIp_chinaIp_shouldPass() {
        when(ipToRegionService.getRegionByIp("114.114.114.114"))
                .thenReturn(region("中国", "电信"));

        assertDoesNotThrow(() -> checker.checkBeforePay(overseasCtx("114.114.114.114")));

        verify(payRiskHitService, never()).recordHit(any(), anyString(), anyString(), any());
    }

    @Test
    @DisplayName("海外检查: 港澳台(country=中国)放行不记录")
    void overseasIp_bigChina_shouldPass() {
        when(ipToRegionService.getRegionByIp("203.198.1.1"))
                .thenReturn(region("中国", "香港").setProvince("香港"));

        assertDoesNotThrow(() -> checker.checkBeforePay(overseasCtx("203.198.1.1")));

        verify(payRiskHitService, never()).recordHit(any(), anyString(), anyString(), any());
    }

    @Test
    @DisplayName("海外检查: 新版 xdb country=Reserved 文本兜底放行")
    void overseasIp_reservedCountryFallback_shouldPass() {
        // 169.254 链路本地不在 NetUtil.isInnerIP 覆盖范围, 独立验证 Reserved 文本兜底分支
        when(ipToRegionService.getRegionByIp("169.254.1.1"))
                .thenReturn(region("Reserved", "0"));

        assertDoesNotThrow(() -> checker.checkBeforePay(overseasCtx("169.254.1.1")));

        verify(payRiskHitService, never()).recordHit(any(), anyString(), anyString(), any());
    }

    @Test
    @DisplayName("海外检查: 老版 xdb isp=内网IP 文本兜底放行")
    void overseasIp_innerIpLabelFallback_shouldPass() {
        when(ipToRegionService.getRegionByIp("169.254.1.1"))
                .thenReturn(region("Reserved", "内网IP"));

        assertDoesNotThrow(() -> checker.checkBeforePay(overseasCtx("169.254.1.1")));

        verify(payRiskHitService, never()).recordHit(any(), anyString(), anyString(), any());
    }

    @Test
    @DisplayName("海外检查: 海外 IP 命中阻断下单")
    void overseasIp_foreignIp_shouldBlock() {
        when(ipToRegionService.getRegionByIp("8.8.8.8"))
                .thenReturn(region("美国", "Google"));

        BizInfoException ex = assertThrows(BizInfoException.class,
                () -> checker.checkBeforePay(overseasCtx("8.8.8.8")));

        assertEquals(BLACKLIST_MSG_KEY, ex.getMessageKey());
    }

    @Test
    @DisplayName("海外检查: 海外 IP 命中记录参数正确(type=overseas_ip, blacklistId=null)")
    void overseasIp_foreignHit_shouldRecordOverseasTypeAndNullBlacklistId() {
        when(ipToRegionService.getRegionByIp("8.8.8.8"))
                .thenReturn(region("美国", "Google"));

        assertThrows(BizInfoException.class,
                () -> checker.checkBeforePay(overseasCtx("8.8.8.8")));

        ArgumentCaptor<String> typeCap = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<String> valueCap = ArgumentCaptor.forClass(String.class);
        verify(payRiskHitService).recordHit(any(), typeCap.capture(), valueCap.capture(), eq(null));
        assertEquals(OVERSEAS_IP_TYPE, typeCap.getValue());
        assertEquals("8.8.8.8", valueCap.getValue());
    }

    @Test
    @DisplayName("海外检查: blockOnHit=false 时海外 IP 仅记录不阻断")
    void overseasIp_blockOnHitFalse_shouldRecordWithoutThrow() {
        when(ipToRegionService.getRegionByIp("8.8.8.8"))
                .thenReturn(region("美国", "Google"));
        PayRiskCheckContext ctx = overseasCtx("8.8.8.8").setBlockOnHit(false);

        assertDoesNotThrow(() -> checker.checkBeforePay(ctx));

        verify(payRiskHitService, times(1)).recordHit(any(), eq(OVERSEAS_IP_TYPE), eq("8.8.8.8"), eq(null));
    }

    @Test
    @DisplayName("海外检查: checkAfterPay 海外 IP 仅记录不阻断")
    void overseasIp_afterPay_shouldRecordWithoutThrow() {
        when(ipToRegionService.getRegionByIp("8.8.8.8"))
                .thenReturn(region("美国", "Google"));

        assertDoesNotThrow(() -> checker.checkAfterPay(overseasCtx("8.8.8.8")));

        verify(payRiskHitService, times(1)).recordHit(any(), eq(OVERSEAS_IP_TYPE), eq("8.8.8.8"), eq(null));
    }

    @Test
    @DisplayName("海外检查: blockOverseasIp=null 时不触发海外命中")
    void overseasIp_switchNull_shouldNotTriggerOverseasCheck() {
        PayRiskCheckContext ctx = new PayRiskCheckContext()
                .setClientIp("8.8.8.8")
                .setBlockOverseasIp(null)
                .setBlockOnHit(true);

        assertDoesNotThrow(() -> checker.checkBeforePay(ctx));

        // 海外 IP 开关 null → 不产生 overseas_ip 命中
        verify(payRiskHitService, never()).recordHit(any(), eq(OVERSEAS_IP_TYPE), anyString(), any());
    }

    // ==================== 黑名单开关 ====================

    @Test
    @DisplayName("黑名单开关: blacklistEnabled 关闭(null)时不触发 IP/用户标识检查")
    void blacklist_switchOff_shouldNotCheck() {
        PayRiskCheckContext ctx = new PayRiskCheckContext()
                .setClientIp("1.2.3.4")
                .setChannel(ChannelEnum.ALIPAY.getCode())
                .setOpenId("uid-1")
                .setBlockOverseasIp(false)
                .setBlacklistEnabled(null)
                .setRegionBlacklistEnabled(false)
                .setBlockOnHit(true);

        assertDoesNotThrow(() -> checker.checkBeforePay(ctx));

        // 黑名单开关关闭 → 不查 IP/用户标识名单
        verify(payBlacklistService, never()).findActive(eq(IP_TYPE), anyString(), any());
        verify(payBlacklistService, never()).findActive(eq(ALIPAY_USER_TYPE), anyString(), any());
        verify(payBlacklistService, never()).findActive(eq(WECHAT_OPENID_TYPE), anyString(), any());
    }

    // ==================== 黑名单 - IP ====================

    @Test
    @DisplayName("IP名单: 命中 + blockOnHit=true 阻断下单")
    void ipBlacklist_hitWithBlock_shouldThrow() {
        when(payBlacklistService.findActive(eq(IP_TYPE), eq("1.2.3.4"), eq(null)))
                .thenReturn(Optional.of(bl(99L)));

        BizInfoException ex = assertThrows(BizInfoException.class,
                () -> checker.checkBeforePay(noOverseasCtx().setClientIp("1.2.3.4")));

        assertEquals(BLACKLIST_MSG_KEY, ex.getMessageKey());
        verify(payRiskHitService).recordHit(any(), eq(IP_TYPE), eq("1.2.3.4"), eq(99L));
    }

    @Test
    @DisplayName("IP名单: 命中 + blockOnHit=false 仅记录不阻断")
    void ipBlacklist_hitWithoutBlock_shouldRecordOnly() {
        when(payBlacklistService.findActive(eq(IP_TYPE), eq("1.2.3.4"), eq(null)))
                .thenReturn(Optional.of(bl(99L)));
        PayRiskCheckContext ctx = noOverseasCtx().setClientIp("1.2.3.4").setBlockOnHit(false);

        assertDoesNotThrow(() -> checker.checkBeforePay(ctx));

        verify(payRiskHitService).recordHit(any(), eq(IP_TYPE), eq("1.2.3.4"), eq(99L));
    }

    @Test
    @DisplayName("IP名单: 命中 + blockOnHit=null 默认阻断")
    void ipBlacklist_hitWithNullBlock_shouldThrowByDefault() {
        when(payBlacklistService.findActive(eq(IP_TYPE), eq("1.2.3.4"), eq(null)))
                .thenReturn(Optional.of(bl(99L)));
        PayRiskCheckContext ctx = noOverseasCtx().setClientIp("1.2.3.4").setBlockOnHit(null);

        assertThrows(BizInfoException.class, () -> checker.checkBeforePay(ctx));
    }

    @Test
    @DisplayName("IP名单: clientIp 为空不查名单")
    void ipBlacklist_blankIp_shouldNotLookup() {
        assertDoesNotThrow(() -> checker.checkBeforePay(noOverseasCtx().setClientIp("")));

        verify(payBlacklistService, never()).findActive(anyString(), anyString(), any());
        verifyNoInteractions(payRiskHitService);
    }

    // ==================== 黑名单 - 用户标识 ====================

    @Test
    @DisplayName("用户标识: 支付宝 openId 命中阻断")
    void alipayUser_hit_shouldThrow() {
        when(payBlacklistService.findActive(eq(ALIPAY_USER_TYPE), eq("uid-1"), eq(null)))
                .thenReturn(Optional.of(bl(88L)));
        PayRiskCheckContext ctx = noOverseasCtx()
                .setChannel(ChannelEnum.ALIPAY.getCode())
                .setOpenId("uid-1");

        assertThrows(BizInfoException.class, () -> checker.checkBeforePay(ctx));

        verify(payRiskHitService).recordHit(any(), eq(ALIPAY_USER_TYPE), eq("uid-1"), eq(88L));
    }

    @Test
    @DisplayName("用户标识: 支付宝 openId 未命中放行")
    void alipayUser_miss_shouldPass() {
        PayRiskCheckContext ctx = noOverseasCtx()
                .setChannel(ChannelEnum.ALIPAY.getCode())
                .setOpenId("uid-1");

        assertDoesNotThrow(() -> checker.checkBeforePay(ctx));

        verify(payBlacklistService).findActive(eq(ALIPAY_USER_TYPE), eq("uid-1"), eq(null));
        verify(payRiskHitService, never()).recordHit(any(), anyString(), anyString(), any());
    }

    @Test
    @DisplayName("用户标识: 微信 openId 命中(带 channelAppId)阻断")
    void wechatOpenId_hitWithAppId_shouldThrow() {
        when(payBlacklistService.findActive(eq(WECHAT_OPENID_TYPE), eq("wx-oid"), eq("wx-app-123")))
                .thenReturn(Optional.of(bl(77L)));
        PayRiskCheckContext ctx = noOverseasCtx()
                .setChannel(ChannelEnum.WECHAT.getCode())
                .setOpenId("wx-oid")
                .setChannelAppId("wx-app-123");

        assertThrows(BizInfoException.class, () -> checker.checkBeforePay(ctx));

        // recordHit 入参不含 wxAppId, 仅 findActive 查询时带
        verify(payRiskHitService).recordHit(any(), eq(WECHAT_OPENID_TYPE), eq("wx-oid"), eq(77L));
    }

    @Test
    @DisplayName("用户标识: 微信 openId 未命中放行")
    void wechatOpenId_miss_shouldPass() {
        PayRiskCheckContext ctx = noOverseasCtx()
                .setChannel(ChannelEnum.WECHAT.getCode())
                .setOpenId("wx-oid")
                .setChannelAppId("wx-app-123");

        assertDoesNotThrow(() -> checker.checkBeforePay(ctx));

        verify(payBlacklistService).findActive(eq(WECHAT_OPENID_TYPE), eq("wx-oid"), eq("wx-app-123"));
        verify(payRiskHitService, never()).recordHit(any(), anyString(), anyString(), any());
    }

    @Test
    @DisplayName("用户标识: 非支付宝/微信通道不查用户标识名单")
    void otherChannel_shouldNotCheckUserIdentity() {
        PayRiskCheckContext ctx = noOverseasCtx()
                .setChannel("union_pay")
                .setClientIp("1.2.3.4")
                .setOpenId("some-uid");

        assertDoesNotThrow(() -> checker.checkBeforePay(ctx));

        // 仅 IP 名单查询一次, 用户标识名单不查
        verify(payBlacklistService, times(1)).findActive(eq(IP_TYPE), eq("1.2.3.4"), eq(null));
        verify(payBlacklistService, never()).findActive(eq(ALIPAY_USER_TYPE), anyString(), any());
        verify(payBlacklistService, never()).findActive(eq(WECHAT_OPENID_TYPE), anyString(), any());
    }

    // ==================== 事后检查 checkAfterPay ====================

    @Test
    @DisplayName("事后检查: IP 命中仅记录不阻断")
    void afterPay_ipHit_shouldRecordOnly() {
        when(payBlacklistService.findActive(eq(IP_TYPE), eq("1.2.3.4"), eq(null)))
                .thenReturn(Optional.of(bl(99L)));
        PayRiskCheckContext ctx = noOverseasCtx().setClientIp("1.2.3.4");

        assertDoesNotThrow(() -> checker.checkAfterPay(ctx));

        verify(payRiskHitService).recordHit(any(), eq(IP_TYPE), eq("1.2.3.4"), eq(99L));
    }

    @Test
    @DisplayName("事后检查: buyerId≠openId 时补查 buyerId 命中并记录")
    void afterPay_buyerIdDiff_shouldSupplementCheck() {
        when(payBlacklistService.findActive(eq(ALIPAY_USER_TYPE), eq("uid-b"), eq(null)))
                .thenReturn(Optional.of(bl(66L)));
        PayRiskCheckContext ctx = noOverseasCtx()
                .setChannel(ChannelEnum.ALIPAY.getCode())
                .setOpenId("uid-a")
                .setBuyerId("uid-b");

        assertDoesNotThrow(() -> checker.checkAfterPay(ctx));

        // openId 查一次(未命中) + buyerId 补查一次(命中), 共两次
        verify(payRiskHitService).recordHit(any(), eq(ALIPAY_USER_TYPE), eq("uid-b"), eq(66L));
        verify(payRiskHitService, never()).recordHit(any(), eq(ALIPAY_USER_TYPE), eq("uid-a"), any());
    }

    // ==================== 地区黑名单（省级 + 市级, 合并开关） ====================

    @Test
    @DisplayName("地区拦截: regionBlacklistEnabled 关闭(null/false)时不触发检查")
    void regionBlacklist_switchOff_shouldNotCheck() {
        when(ipToRegionService.getRegionByIp("114.114.114.114"))
                .thenReturn(regionWithCity("中国", "广东省", "深圳市", "电信"));
        PayRiskCheckContext ctx = new PayRiskCheckContext()
                .setClientIp("114.114.114.114")
                .setBlockOverseasIp(false)
                .setRegionBlacklistEnabled(null)
                .setBlockOnHit(true);

        assertDoesNotThrow(() -> checker.checkBeforePay(ctx));

        // 开关关闭 → 不调 ip2region, 不查省/市级名单
        verify(ipToRegionService, never()).getRegionByIp(anyString());
        verify(payBlacklistService, never()).findActive(eq(PROVINCE_TYPE), anyString(), any());
        verify(payBlacklistService, never()).findActive(eq(CITY_TYPE), anyString(), any());
    }

    @Test
    @DisplayName("地区拦截: IP 归属省在省级名单中命中阻断")
    void regionBlacklist_provinceHit_shouldThrow() {
        when(ipToRegionService.getRegionByIp("114.114.114.114"))
                .thenReturn(regionWithCity("中国", "广东省", "深圳市", "电信"));
        when(regionCodeResolver.resolveProvinceCode("广东省")).thenReturn("44");
        when(payBlacklistService.findActive(eq(PROVINCE_TYPE), eq("44"), eq(null)))
                .thenReturn(Optional.of(bl(55L)));
        PayRiskCheckContext ctx = noOverseasCtx().setClientIp("114.114.114.114");

        BizInfoException ex = assertThrows(BizInfoException.class,
                () -> checker.checkBeforePay(ctx));

        assertEquals(BLACKLIST_MSG_KEY, ex.getMessageKey());
        verify(payRiskHitService).recordHit(any(), eq(PROVINCE_TYPE), eq("44"), eq(55L));
    }

    @Test
    @DisplayName("地区拦截: 省级名单命中后短路市级检查")
    void regionBlacklist_provinceHit_shouldShortCircuitCity() {
        when(ipToRegionService.getRegionByIp("114.114.114.114"))
                .thenReturn(regionWithCity("中国", "广东省", "深圳市", "电信"));
        when(regionCodeResolver.resolveProvinceCode("广东省")).thenReturn("44");
        when(payBlacklistService.findActive(eq(PROVINCE_TYPE), eq("44"), eq(null)))
                .thenReturn(Optional.of(bl(55L)));
        PayRiskCheckContext ctx = noOverseasCtx().setClientIp("114.114.114.114");

        assertThrows(BizInfoException.class, () -> checker.checkBeforePay(ctx));

        // 省命中 → 不查市级名单
        verify(payBlacklistService).findActive(eq(PROVINCE_TYPE), eq("44"), eq(null));
        verify(payBlacklistService, never()).findActive(eq(CITY_TYPE), anyString(), any());
    }

    @Test
    @DisplayName("地区拦截: 省级未命中、市级命中时阻断")
    void regionBlacklist_cityHit_shouldThrow() {
        when(ipToRegionService.getRegionByIp("114.114.114.114"))
                .thenReturn(regionWithCity("中国", "广东省", "深圳市", "电信"));
        when(regionCodeResolver.resolveProvinceCode("广东省")).thenReturn("44");
        when(regionCodeResolver.resolveCityCode("广东省", "深圳市")).thenReturn("4403");
        when(payBlacklistService.findActive(eq(CITY_TYPE), eq("4403"), eq(null)))
                .thenReturn(Optional.of(bl(66L)));
        PayRiskCheckContext ctx = noOverseasCtx().setClientIp("114.114.114.114");

        BizInfoException ex = assertThrows(BizInfoException.class,
                () -> checker.checkBeforePay(ctx));

        assertEquals(BLACKLIST_MSG_KEY, ex.getMessageKey());
        verify(payRiskHitService).recordHit(any(), eq(CITY_TYPE), eq("4403"), eq(66L));
    }

    @Test
    @DisplayName("地区拦截: 省/市名单均未命中放行")
    void regionBlacklist_miss_shouldPass() {
        when(ipToRegionService.getRegionByIp("114.114.114.114"))
                .thenReturn(regionWithCity("中国", "广东省", "深圳市", "电信"));
        when(regionCodeResolver.resolveProvinceCode("广东省")).thenReturn("44");
        when(regionCodeResolver.resolveCityCode("广东省", "深圳市")).thenReturn("4403");
        PayRiskCheckContext ctx = noOverseasCtx().setClientIp("114.114.114.114");

        assertDoesNotThrow(() -> checker.checkBeforePay(ctx));

        // 省级查一次未命中, 继续查市级
        verify(payBlacklistService).findActive(eq(PROVINCE_TYPE), eq("44"), eq(null));
        verify(payBlacklistService).findActive(eq(CITY_TYPE), eq("4403"), eq(null));
        verify(payRiskHitService, never()).recordHit(any(), anyString(), anyString(), any());
    }

    @Test
    @DisplayName("地区拦截: 内网 IP 不查归属地")
    void regionBlacklist_innerIp_shouldSkip() {
        PayRiskCheckContext ctx = noOverseasCtx().setClientIp("192.168.1.1");

        assertDoesNotThrow(() -> checker.checkBeforePay(ctx));

        verify(ipToRegionService, never()).getRegionByIp(anyString());
        verify(payBlacklistService, never()).findActive(eq(PROVINCE_TYPE), anyString(), any());
        verify(payBlacklistService, never()).findActive(eq(CITY_TYPE), anyString(), any());
    }

    @Test
    @DisplayName("地区拦截: ip2region 返回 null fail-open")
    void regionBlacklist_nullRegion_shouldFailOpen() {
        when(ipToRegionService.getRegionByIp("114.114.114.114")).thenReturn(null);
        PayRiskCheckContext ctx = noOverseasCtx().setClientIp("114.114.114.114");

        assertDoesNotThrow(() -> checker.checkBeforePay(ctx));

        verify(payBlacklistService, never()).findActive(eq(PROVINCE_TYPE), anyString(), any());
        verify(payBlacklistService, never()).findActive(eq(CITY_TYPE), anyString(), any());
        verify(payRiskHitService, never()).recordHit(any(), eq(PROVINCE_TYPE), anyString(), any());
    }

    @Test
    @DisplayName("地区拦截: checkAfterPay 事后补录仅记录不阻断")
    void regionBlacklist_afterPay_shouldRecordOnly() {
        when(ipToRegionService.getRegionByIp("114.114.114.114"))
                .thenReturn(regionWithCity("中国", "广东省", "深圳市", "电信"));
        when(regionCodeResolver.resolveProvinceCode("广东省")).thenReturn("44");
        when(payBlacklistService.findActive(eq(PROVINCE_TYPE), eq("44"), eq(null)))
                .thenReturn(Optional.of(bl(55L)));
        PayRiskCheckContext ctx = noOverseasCtx().setClientIp("114.114.114.114");

        assertDoesNotThrow(() -> checker.checkAfterPay(ctx));

        verify(payRiskHitService).recordHit(any(), eq(PROVINCE_TYPE), eq("44"), eq(55L));
    }

    // ==================== hasOpenIdBlacklist 缓存 ====================

    @Test
    @DisplayName("缓存: 首次调用查 DB 返回结果")
    void hasOpenIdBlacklist_firstCall_shouldQueryDb() {
        when(payBlacklistService.hasActiveOpenIdBlacklist()).thenReturn(true);

        assertTrue(checker.hasOpenIdBlacklist());

        verify(payBlacklistService, times(1)).hasActiveOpenIdBlacklist();
    }

    @Test
    @DisplayName("缓存: 30s 内再次调用走缓存, 不重复查 DB")
    void hasOpenIdBlacklist_secondCallWithinTtl_shouldHitCache() {
        when(payBlacklistService.hasActiveOpenIdBlacklist()).thenReturn(false);

        checker.hasOpenIdBlacklist();
        checker.hasOpenIdBlacklist();

        // Caffeine 缓存命中, DB 查询仅一次
        verify(payBlacklistService, times(1)).hasActiveOpenIdBlacklist();
        assertFalse(checker.hasOpenIdBlacklist());
    }

    // ==================== 公共夹具 ====================

    /** 开启海外拦截 + 阻断下单的上下文 */
    private static PayRiskCheckContext overseasCtx(String ip) {
        return new PayRiskCheckContext()
                .setClientIp(ip)
                .setBlockOverseasIp(true)
                .setBlockOnHit(true);
    }

    /** 关闭海外拦截的上下文(隔离黑名单路径, 避免公网 IP 触发海外检查干扰断言) */
    private static PayRiskCheckContext noOverseasCtx() {
        return new PayRiskCheckContext()
                .setBlockOverseasIp(false)
                .setBlacklistEnabled(true)
                .setRegionBlacklistEnabled(true)
                .setBlockOnHit(true);
    }

    /** 构造只设 id 的名单行(recordHit 仅读取 id) */
    private static PayBlacklist bl(Long id) {
        PayBlacklist entity = new PayBlacklist();
        // setId 来自父类 MpBaseEntity, 链式返回父类型, 单独赋值不链式
        entity.setId(id);
        return entity;
    }

    /** 构造仅含 country/isp 的 IpRegion */
    private static IpRegion region(String country, String isp) {
        return new IpRegion().setCountry(country).setIsp(isp);
    }

    /** 构造含 province/city 的 IpRegion（地区名单测试用） */
    private static IpRegion regionWithCity(String country, String province, String city, String isp) {
        return new IpRegion().setCountry(country).setProvince(province).setCity(city).setIsp(isp);
    }
}
