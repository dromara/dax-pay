package cn.daxpay.open.channel.mock.service;

import cn.daxpay.open.channel.mock.client.enums.MockPayBodyType;
import cn.daxpay.open.channel.mock.strategy.MockProductCapabilityCatalog;
import cn.daxpay.open.platform.core.enums.pay.channel.PayCapabilityEnum;
import cn.daxpay.open.platform.core.enums.pay.channel.PayMethodEnum;
import cn.daxpay.open.platform.core.enums.pay.channel.PayProviderEnum;
import cn.daxpay.open.platform.core.exception.BizInfoException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.EnumSet;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

/// # mock 通道支付方式对齐契约测试
///
/// 把"平台支付方式 ↔ mock 产品声明 ↔ 子应用 way"三段的对应关系固化成断言,
/// 任一段单独变更(平台新增方式、产品漏声明、子应用改枚举)都会在此失败, 强制同批改齐。
///
/// 四类断言:
/// 1. **覆盖完整**: 产品声明 == 主数据 `pay_md_product_capability`(mock_pay) 21 项, 不多不少;
/// 2. **枚举可达**: 每个声明方式都能用 [PayMethodEnum]/[PayCapabilityEnum] 查到, 且方式↔能力同名;
/// 3. **映射可达**: 每个声明方式都能映射出子应用 way 与渠道, 不落入"暂不支持"分支;
/// 4. **镜像一致**: 本仓 [MockPayBodyType] 与子应用同名枚举值集相同(跨仓无法直接引用,
///    以快照集合断言, 改一侧必须同步改另一侧)。
///
/// 服务的映射方法为包级可见(仅测试与同包调用), 故本测试与 [MockPayService] 同包。
class MockPayMethodAlignmentTest {

    /// 主数据 mock_pay 已挂的 21 项支付能力(与 _config/sql/data.sql 逐项对齐, 改 SQL 须同步此集合)
    private static final Set<String> SQL_CAPABILITY_CODES = Set.of(
            // 微信 7
            "wechat_cashier", "wechat_qr", "wechat_jsapi", "wechat_mini", "wechat_h5", "wechat_app", "wechat_barcode",
            // 支付宝 6
            "alipay_qr", "alipay_jsapi", "alipay_pc", "alipay_h5", "alipay_app", "alipay_barcode",
            // 银联 4
            "union_qr", "union_jsapi", "union_h5", "union_barcode",
            // 抖音 4
            "douyin_qr", "douyin_jsapi", "douyin_h5", "douyin_app");

    /// 子应用 daxpay-channel-mock 的 way 全量枚举名(跨仓快照, 改子应用枚举须同步此集合)
    private static final Set<String> SUB_APP_WAYS = Set.of(
            "QR", "SCAN", "BARCODE", "H5", "PC", "APP", "MP", "JSAPI", "MINI_APP");

    /// 子应用 MockPayBodyType 全量枚举名(跨仓快照, 与下面镜像断言互为校验)
    private static final Set<String> SUB_APP_BODY_TYPES = Set.of("URL", "PARAMS", "JSON");

    /// 不纳入模拟通道的方式: 数字人民币/卡组/无固定渠道
    private static final Set<PayMethodEnum> EXCLUDED = EnumSet.of(
            PayMethodEnum.ECNY_QR,
            PayMethodEnum.VISA_CARD_GATEWAY,
            PayMethodEnum.VISA_CARD_PRESENT,
            PayMethodEnum.MASTERCARD_CARD_GATEWAY,
            PayMethodEnum.MASTERCARD_CARD_PRESENT,
            PayMethodEnum.OTHER);

    @Test
    @DisplayName("产品声明与主数据 SQL 21 项一致, 不多不少")
    void catalogMatchesSqlCapabilities() {
        Set<String> declared = MockProductCapabilityCatalog.METHOD_CAP_MAP.keySet().stream()
                .map(PayMethodEnum::getCode)
                .collect(Collectors.toSet());
        assertEquals(SQL_CAPABILITY_CODES, declared,
                "产品声明能力集与主数据不一致: 改 SQL 时须同步 MockProductCapabilityCatalog");
        assertEquals(21, declared.size(), "主数据 mock_pay 能力数量变化, 请复核 SQL 与产品声明");
    }

    @Test
    @DisplayName("每个声明方式都能用 PayMethodEnum/PayCapabilityEnum 查到, 且方式与能力同名")
    void everyDeclaredMethodResolvableAndSameNamedCapability() {
        MockProductCapabilityCatalog.METHOD_CAP_MAP.forEach((method, capabilities) -> {
            assertNotNull(PayMethodEnum.findByCodeOrNull(method.getCode()),
                    "方式未登记: " + method.getCode());
            PayCapabilityEnum capability = PayCapabilityEnum.findByCode(method.getCode());
            assertNotNull(capability, "能力未登记: " + method.getCode());
            List<PayCapabilityEnum> expected = List.of(PayCapabilityEnum.valueOf(method.name()));
            assertEquals(expected, capabilities, "方式与能力不对应: " + method.getCode());
        });
    }

    @Test
    @DisplayName("每个声明方式都能映射到子应用已定义的 way")
    void everyDeclaredMethodMapsToSubAppWay() {
        for (PayMethodEnum method : MockProductCapabilityCatalog.METHOD_CAP_MAP.keySet()) {
            MockPayService.WayMapping mapping = assertDoesNotThrow(
                    () -> MockPayService.mapWay(method.getCode()),
                    "方式未接入 mapWay: " + method.getCode());
            assertNotNull(mapping.way(), "映射缺少 way: " + method.getCode());
            assertTrue(SUB_APP_WAYS.contains(mapping.way()),
                    "映射出的 way 不在子应用枚举内: " + method.getCode() + " -> " + mapping.way());
        }
    }

    @Test
    @DisplayName("每个声明方式都能映射出渠道, 且渠道在产品渠道声明内")
    void everyDeclaredMethodMapsToRoutableProvider() {
        Set<String> declaredProviders = MockProductCapabilityCatalog.SUPPORTED_PROVIDERS.stream()
                .map(MockPayMethodAlignmentTest::subAppProviderName)
                .collect(Collectors.toSet());
        for (PayMethodEnum method : MockProductCapabilityCatalog.METHOD_CAP_MAP.keySet()) {
            String provider = MockPayService.mapProvider(method.getCode());
            assertNotNull(provider, "方式未映射出渠道: " + method.getCode());
            assertTrue(declaredProviders.contains(provider),
                    "方式渠道未被产品渠道声明覆盖: " + method.getCode() + " -> " + provider);
        }
    }

    @Test
    @DisplayName("四渠道各自至少有一个方式可路由")
    void eachProviderHasAtLeastOneMethod() {
        Set<String> providers = MockProductCapabilityCatalog.METHOD_CAP_MAP.keySet().stream()
                .map(method -> MockPayService.mapProvider(method.getCode()))
                .collect(Collectors.toSet());
        assertEquals(Set.of("WECHAT", "ALIPAY", "UNION", "DOUYIN"), providers,
                "渠道覆盖不全: 任一钱包渠道缺失会导致该渠道订单无法路由到 mock");
    }

    @Test
    @DisplayName("支付内容类型镜像与子应用一致")
    void subAppBodyTypeMirrorIsComplete() {
        Set<String> mirror = Arrays.stream(MockPayBodyType.values())
                .map(Enum::name)
                .collect(Collectors.toSet());
        assertEquals(SUB_APP_BODY_TYPES, mirror,
                "MockPayBodyType 与子应用不一致: 少值会在子应用返回该值时反序列化失败");
    }

    @Test
    @DisplayName("数字人民币/卡组/OTHER 显式拒绝, 不静默落到兜底")
    void excludedMethodsAreExplicitlyRejected() {
        for (PayMethodEnum method : EXCLUDED) {
            assertThrows(BizInfoException.class, () -> MockPayService.mapWay(method.getCode()),
                    "应显式拒绝而非静默映射: " + method.getCode());
        }
    }

    @Test
    @DisplayName("聚合扫码映射 QR 且实际钱包逆向解析对称(能力未挂, 仅映射备用)")
    void aggregateMapsToQrAndActualWalletRoundTrips() {
        MockPayService.WayMapping mapping = MockPayService.mapWay(PayMethodEnum.AGGREGATE_PAY_QRCODE.getCode());
        assertEquals("QR", mapping.way(), "聚合扫码应复用扫码族 way=QR(确认页即一码多付的码)");
        assertEquals("AGGREGATE", MockPayService.mapProvider(PayMethodEnum.AGGREGATE_PAY_QRCODE.getCode()),
                "聚合扫码应携带聚合渠道, 子应用按聚合单落单");
        // 实际钱包逆向映射(回调/同步回传): 子应用渠道名 → 平台支付渠道
        assertEquals(PayProviderEnum.WECHAT, MockPayService.resolveActualProvider("WECHAT"));
        assertEquals(PayProviderEnum.ALIPAY, MockPayService.resolveActualProvider("ALIPAY"));
        assertEquals(PayProviderEnum.UNION_PAY, MockPayService.resolveActualProvider("UNION"));
        assertEquals(PayProviderEnum.DOUYIN, MockPayService.resolveActualProvider("DOUYIN"));
        assertNull(MockPayService.resolveActualProvider("AGGREGATE"),
                "聚合渠道名不代表实际钱包, 不应回填");
        assertNull(MockPayService.resolveActualProvider(""));
    }

    @Test
    @DisplayName("H5 族四渠道全部纳入映射, 未被遗漏为不支持")
    void h5FamilyIsFullyMapped() {
        Set<PayMethodEnum> h5Methods = new HashSet<>(Set.of(
                PayMethodEnum.WECHAT_H5, PayMethodEnum.ALIPAY_H5,
                PayMethodEnum.UNION_H5, PayMethodEnum.DOUYIN_H5));
        for (PayMethodEnum method : h5Methods) {
            MockPayService.WayMapping mapping = assertDoesNotThrow(
                    () -> MockPayService.mapWay(method.getCode()), "H5 方式未接入: " + method.getCode());
            assertEquals("H5", mapping.way(), "H5 方式 way 应为 H5: " + method.getCode());
        }
    }

    /// 产品渠道枚举 -> 子应用渠道枚举名(与 MockPayService#mapProvider 同构)
    private static String subAppProviderName(PayProviderEnum provider) {
        return switch (provider) {
            case WECHAT -> "WECHAT";
            case ALIPAY -> "ALIPAY";
            case UNION_PAY -> "UNION";
            case DOUYIN -> "DOUYIN";
            case AGGREGATE_PAY -> "AGGREGATE";
            case ECNY, VISA, MASTERCARD -> null;
        };
    }
}
