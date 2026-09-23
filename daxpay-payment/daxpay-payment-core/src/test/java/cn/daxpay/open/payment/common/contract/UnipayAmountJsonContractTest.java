package cn.daxpay.open.payment.common.contract;

import cn.daxpay.open.payment.common.json.UnipayAmount;
import cn.daxpay.open.payment.common.json.UnipayJson;
import cn.daxpay.open.payment.common.result.DaxResult;
import cn.daxpay.open.payment.common.util.PaySignUtil;
import cn.daxpay.open.payment.testsupport.JacksonTestSupport;
import cn.daxpay.open.payment.unipay.result.assist.UnipayPingResult;
import cn.daxpay.open.payment.unipay.result.gateway.CashierItemPublicResult;
import cn.daxpay.open.payment.unipay.result.gateway.GatewayOrderResult;
import cn.daxpay.open.payment.unipay.result.gateway.GatewayPrePayResult;
import cn.daxpay.open.payment.unipay.result.trade.PayResultRedirectResult;
import cn.daxpay.open.payment.unipay.result.trade.PayResultResult;
import cn.daxpay.open.payment.unipay.result.trade.alloc.AllocOrderResult;
import cn.daxpay.open.payment.unipay.result.trade.alloc.AllocResult;
import cn.daxpay.open.payment.unipay.result.trade.pay.NormalPayOrderResult;
import cn.daxpay.open.payment.unipay.result.trade.pay.NormalPayResult;
import cn.daxpay.open.payment.unipay.result.trade.refund.RefundOrderResult;
import cn.daxpay.open.payment.unipay.result.trade.refund.RefundResult;
import cn.daxpay.open.payment.unipay.result.trade.transfer.TransferCreateResult;
import cn.daxpay.open.payment.unipay.result.trade.transfer.TransferOrderResult;
import cn.daxpay.open.platform.common.json.util.JacksonUtil;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import tools.jackson.databind.JsonNode;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/// # unipay 契约金额 JSON 口径测试
///
/// 锁定两条不变量:
/// 1. **金额字段数字输出**: 平台全局 Jackson 把 Long 输出为字符串(防前端 ID 精度丢失),
///    契约响应类的金额 Long 字段必须在字段级加 [UnipayAmount] 覆盖回数字, 漏注解即失败;
///    ID 类 Long 字段(C 端收银台 itemId 等)保持字符串输出, 不得加该注解(见豁免清单)。
/// 2. **签名口径不随形态变化**: 待签串由扁平化取值字面量构造, 金额数字与字符串形态结果一致。
class UnipayAmountJsonContractTest {

    /// 契约响应类清单(其中的 Long 字段一律要求 @UnipayAmount)
    private static final List<Class<?>> RESPONSE_CLASSES = List.of(
            DaxResult.class,
            NormalPayResult.class,
            NormalPayOrderResult.class,
            RefundResult.class,
            RefundOrderResult.class,
            TransferCreateResult.class,
            TransferOrderResult.class,
            AllocResult.class,
            AllocOrderResult.class,
            GatewayPrePayResult.class,
            GatewayOrderResult.class,
            PayResultResult.class,
            PayResultRedirectResult.class,
            UnipayPingResult.class
    );

    /// ID 类 Long 字段豁免清单(类名#字段名): 保持字符串输出(精度保护), 不得加 @UnipayAmount
    private static final List<String> ID_FIELD_EXEMPTIONS = List.of(
            "CashierItemPublicResult#id"
    );

    @BeforeAll
    static void initJackson() {
        JacksonTestSupport.init();
    }

    /// 护栏: 契约响应类的每个 Long 字段都必须带 @UnipayAmount(除豁免的 ID 类字段)
    @Test
    @DisplayName("契约金额字段注解护栏")
    void everyContractLongFieldAnnotated() {
        List<String> missing = new ArrayList<>();
        for (Class<?> clazz : RESPONSE_CLASSES) {
            collect(clazz, missing);
        }
        assertTrue(missing.isEmpty(),
                "以下契约 Long 字段缺少 @UnipayAmount 注解(金额字段漏注解会以字符串输出): " + missing);
    }

    /// 递归收集类与其内部类的 Long 字段, 校验注解(豁免 ID 类字段)
    private void collect(Class<?> clazz, List<String> missing) {
        for (Class<?> current = clazz; current != null && current != Object.class; current = current.getSuperclass()) {
            for (Field field : current.getDeclaredFields()) {
                if (!Long.class.equals(field.getType())) {
                    continue;
                }
                String key = current.getSimpleName() + "#" + field.getName();
                if (ID_FIELD_EXEMPTIONS.contains(key)) {
                    assertNull(field.getAnnotation(UnipayAmount.class),
                            "ID 类字段应保持字符串输出, 不得加 @UnipayAmount: " + key);
                    continue;
                }
                if (field.getAnnotation(UnipayAmount.class) == null) {
                    missing.add(key);
                }
            }
        }
        for (Class<?> nested : clazz.getDeclaredClasses()) {
            collect(nested, missing);
        }
    }

    /// 金额序列化为数字, ID 类 Long 仍为字符串且不丢精度
    @Test
    @DisplayName("金额数字输出与 ID 字符串输出并存")
    void amountIsNumberAndIdStaysString() {
        NormalPayOrderResult order = new NormalPayOrderResult()
                .setAmount(100L)
                .setRealAmount(100L)
                .setRefundableBalance(60L);
        JsonNode node = JacksonUtil.getObjectMapper().readTree(JacksonUtil.toJson(order, false));
        assertFalse(node.get("amount").isString(), "amount 应为数字: " + node.get("amount"));
        assertEquals(100L, node.get("amount").asLong());
        assertEquals(60L, node.get("refundableBalance").asLong());

        // C 端收银台支付项 ID(雪花): 保持字符串, 防 JS 精度丢失
        CashierItemPublicResult item = new CashierItemPublicResult().setId(1853123456789012345L);
        JsonNode itemNode = JacksonUtil.getObjectMapper()
                .readTree(JacksonUtil.toJson(item, false));
        assertTrue(itemNode.get("id").isString(), "雪花 ID 应为字符串: " + itemNode.get("id"));
        assertEquals("1853123456789012345", itemNode.get("id").asString());
    }

    /// 金额数字形态不改变签名串(待签串取裸字面量)
    @Test
    @DisplayName("金额数字形态不影响签名串")
    void amountFormDoesNotAffectSignStr() {
        NormalPayOrderResult order = new NormalPayOrderResult()
                .setBizOrderNo("B1")
                .setAmount(100L);
        String signStr = PaySignUtil.buildSignStr(order);
        assertTrue(signStr.contains("amount=100"), "待签串金额应为裸字面量: " + signStr);
        assertFalse(signStr.contains("amount=\"100\""), "待签串不应含引号包裹: " + signStr);
    }

    /// 异步通知报文与查询响应同一口径(金额均为数字)
    @Test
    @DisplayName("通知报文金额同为数字")
    void noticePayloadAmountIsNumber() {
        UnipayJson unipayJson = new UnipayJson(JacksonUtil.getObjectMapper());
        NormalPayOrderResult order = new NormalPayOrderResult().setAmount(100L);
        String json = unipayJson.toJson(order);
        JsonNode node;
        try {
            node = JacksonUtil.getObjectMapper().readTree(json);
        } catch (Exception e) {
            throw new AssertionError("通知报文解析失败: " + json, e);
        }
        assertFalse(node.get("amount").isString(), "通知报文 amount 应为数字: " + node.get("amount"));
    }
}
