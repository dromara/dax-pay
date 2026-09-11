package cn.daxpay.open.payment.common.json;

import cn.daxpay.open.payment.testsupport.JacksonTestSupport;
import cn.daxpay.open.payment.common.result.DaxNoticeResult;
import cn.daxpay.open.payment.common.result.DaxResult;
import cn.daxpay.open.payment.common.util.PaySignUtil;
import cn.daxpay.open.payment.unipay.param.MerchantPaymentCommonParam;
import cn.daxpay.open.payment.unipay.param.PaymentCommonParam;
import cn.daxpay.open.payment.unipay.param.gateway.GatewayPrePayParam;
import cn.daxpay.open.payment.unipay.param.trade.pay.NormalPayParam;
import cn.daxpay.open.payment.unipay.result.gateway.GatewayOrderResult;
import cn.daxpay.open.payment.unipay.result.gateway.GatewayPrePayResult;
import cn.daxpay.open.payment.unipay.result.trade.alloc.AllocOrderResult;
import cn.daxpay.open.payment.unipay.result.trade.pay.NormalPayOrderResult;
import cn.daxpay.open.payment.unipay.result.trade.refund.RefundOrderResult;
import cn.daxpay.open.payment.unipay.result.trade.transfer.TransferOrderResult;
import cn.daxpay.open.platform.common.json.util.JacksonUtil;
import cn.daxpay.open.platform.core.util.RsaSignUtil;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.regex.Pattern;

import static org.junit.jupiter.api.Assertions.*;

/// # 对外开放接口(unipay)时间契约测试
///
/// 锁定三条不变量:
/// 1. 契约类的每个 `OffsetDateTime` 字段都带北京时间注解(新增字段漏注解即失败);
/// 2. 出入参签名与报文同源 —— 「签名串 == 报文规范字面量」, 调用方按报文即可验签;
/// 3. 报文中时间字面量为 `yyyy-MM-dd HH:mm:ss`(北京时间, 不带时区后缀)。
class UnipayTimeContractTest {

    /// [0]公钥 [1]私钥 (运行时生成, 避免长 PEM 常量被改写)
    private static String[] keyPair;

    /// 北京时间字面量: yyyy-MM-dd HH:mm:ss, 不带时区后缀
    private static final Pattern WALL_CLOCK = Pattern.compile("^\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}:\\d{2}$");

    /// 覆盖 /unipay 报文的全部契约类(不含内部接口共用的 DTO)
    private static final List<Class<?>> CONTRACT_CLASSES = List.of(
            DaxResult.class,
            NormalPayOrderResult.class,
            RefundOrderResult.class,
            AllocOrderResult.class,
            TransferOrderResult.class,
            GatewayOrderResult.class,
            GatewayPrePayResult.class,
            PaymentCommonParam.class,
            NormalPayParam.class,
            GatewayPrePayParam.class
    );

    @Data
    @EqualsAndHashCode(callSuper = true)
    public static class ProbeParam extends MerchantPaymentCommonParam {
    }

    /// 模拟通知内容快照来源(内部 DTO, 不带契约注解)
    @Data
    public static class InternalSnapshot {
        private String orderNo;

        private OffsetDateTime payTime;

        private BigDecimal amount;
    }

    @BeforeAll
    static void initJackson() {
        JacksonTestSupport.init();
        keyPair = RsaSignUtil.genRsaPemKey();
    }

    /// 护栏: 契约类里任何 OffsetDateTime 字段都必须带北京时间注解
    @Test
    @DisplayName("契约类时间字段注解护栏")
    void everyContractTimeFieldAnnotated() {
        List<String> missing = new ArrayList<>();
        for (Class<?> clazz : CONTRACT_CLASSES) {
            collect(clazz, missing);
        }
        assertTrue(missing.isEmpty(),
                "以下契约时间字段缺少 @JsonFormat(UnipayTimeFormat) 注解: " + missing);
    }

    /// 递归收集类与其内部类的 OffsetDateTime 字段, 校验注解
    private void collect(Class<?> clazz, List<String> missing) {
        for (Class<?> current = clazz; current != null && current != Object.class; current = current.getSuperclass()) {
            for (Field field : current.getDeclaredFields()) {
                if (!OffsetDateTime.class.equals(field.getType())) {
                    continue;
                }
                JsonFormat format = field.getAnnotation(JsonFormat.class);
                boolean ok = format != null
                        && UnipayTimeFormat.PATTERN.equals(format.pattern())
                        && UnipayTimeFormat.ZONE.equals(format.timezone());
                if (!ok) {
                    missing.add(current.getSimpleName() + "#" + field.getName());
                }
            }
        }
        for (Class<?> nested : clazz.getDeclaredClasses()) {
            collect(nested, missing);
        }
    }

    /// 响应: 签名串与报文同源, 且时间字面量为北京时间
    @Test
    @DisplayName("响应报文时间格式与验签闭环")
    void responseTimeIsBeijingTimeAndVerifiable() {
        Map<String, Object> data = new TreeMap<>();
        data.put("amount", "1");
        DaxResult<Map<String, Object>> result = new DaxResult<>(0, data, "成功");
        // UTC 10:46:45 == 北京 18:46:45
        result.setResTime(OffsetDateTime.parse("2026-09-11T10:46:45Z"));
        result.setReqId("R1");
        result.setSign(PaySignUtil.sign(result, keyPair[1]));

        String wire = JacksonUtil.toJson(result, false);
        String sign = PaySignUtil.sign(result, keyPair[1]);
        assertTrue(assertBeijingTime(wire, "resTime"), "报文 resTime 应为北京时间: " + wire);
        assertFalse(wire.contains("2026-09-11T10:46:45Z"), "报文不应出现 UTC ISO 字面量");

        // 商户按报文验签(平台公钥)
        result.setSign(sign);
        String signedWire = JacksonUtil.toJson(result, false);
        assertTrue(PaySignUtil.verify(signedWire, keyPair[0]), "按报文验签应通过");

        // 签名串里的时间字面量与报文完全一致
        String signStr = PaySignUtil.buildSignStr(result);
        assertTrue(signStr.contains("resTime=2026-09-11 18:46:45"), "签名串应为北京时间字面量: " + signStr);
        assertFalse(signStr.contains("resTime=2026-09-11T10:46:45Z"), "签名串不应残留 UTC ISO 字面量");
    }

    /// 通知: 顶层 resTime 与 data 内时间均为北京时间
    @Test
    @DisplayName("通知报文时间格式与验签闭环")
    void noticeTimeIsBeijingTimeAndVerifiable() {
        TreeMap<String, String> data = new TreeMap<>();
        data.put("orderNo", "PAY001");
        DaxNoticeResult<TreeMap<String, String>> notice = new DaxNoticeResult<>(0, data, "成功");
        notice.setEvent("pay.success");
        notice.setProtocol("system");
        notice.setMchNo("M1");
        notice.setAppId("A1");
        notice.setResTime(OffsetDateTime.parse("2026-09-11T10:46:45Z"));
        notice.setSign(PaySignUtil.sign(notice, keyPair[1]));

        String wire = JacksonUtil.toJson(notice, false);
        assertTrue(assertBeijingTime(wire, "resTime"), "通知 resTime 应为北京时间: " + wire);
        assertTrue(PaySignUtil.verify(wire, keyPair[0]), "通知按报文验签应通过");
    }

    /// 内部 DTO 的快照内容经 unipay 序列化器输出北京时间(通知 data.* 的来源)
    @Test
    @DisplayName("内部DTO快照经UnipayJson输出北京时间")
    void internalSnapshotUsesBeijingTime() {
        UnipayJson unipayJson = new UnipayJson(JacksonUtil.getObjectMapper());
        InternalSnapshot snapshot = new InternalSnapshot();
        snapshot.setOrderNo("PAY001");
        snapshot.setPayTime(OffsetDateTime.parse("2026-09-11T10:46:45Z"));
        snapshot.setAmount(new BigDecimal("100.00"));

        String json = unipayJson.toJson(snapshot);
        assertTrue(assertBeijingTime(json, "payTime"), "内部快照时间应为北京时间: " + json);
        // 与主 mapper(内部接口)的 ISO UTC 输出形成对照
        String internalJson = JacksonUtil.toJson(snapshot, false);
        assertTrue(internalJson.contains("2026-09-11T10:46:45Z"), "内部接口仍应为 ISO UTC: " + internalJson);
    }

    /// 请求: 按平台规范字面量验签(契约 DTO 与入参共用同一格式)
    @Test
    @DisplayName("请求按平台规范字面量验签")
    void requestVerifiesAgainstCanonicalLiteral() {
        ProbeParam param = new ProbeParam();
        param.setMchNo("M1");
        param.setAppId("A1");
        param.setReqId("R1");
        param.setNonceStr("N1");
        param.setReqTime(OffsetDateTime.parse("2026-09-11T02:58:12Z")); // 北京 10:58:12

        // 调用方按平台规范字面量(北京时间)构造签名串
        String signStr = PaySignUtil.buildSignStr(param);
        assertTrue(signStr.contains("reqTime=2026-09-11 10:58:12"), "规范签名串应为北京时间字面量: " + signStr);
        String sign = RsaSignUtil.sign(signStr, keyPair[1]);
        param.setSign(sign);

        assertTrue(PaySignUtil.verify(param, param.getSign(), keyPair[0]), "按规范字面量验签应通过");
    }

    /// 入参解析保持宽松(刻意非对称): ISO 带偏移与北京时间都能绑定, 只有签名串格式要求严格
    @Test
    @DisplayName("入参时间解析保持宽松")
    void requestBindingStaysLenient() {
        // ISO 带偏移: 仍可绑定(向后兼容, 避免历史调用方直接绑定失败)
        ProbeParam iso = JacksonUtil.toBean(
                "{\"reqTime\":\"2026-09-11T02:58:12Z\"}", ProbeParam.class);
        assertEquals(OffsetDateTime.parse("2026-09-11T02:58:12Z"), iso.getReqTime(),
                "ISO 入参应仍可绑定");

        // 北京时间字面量: 按东八区解析后转 UTC
        ProbeParam beijingTime = JacksonUtil.toBean(
                "{\"reqTime\":\"2026-09-11 10:58:12\"}", ProbeParam.class);
        assertEquals(OffsetDateTime.parse("2026-09-11T02:58:12Z"), beijingTime.getReqTime(),
                "北京时间入参应按东八区解析");

        // 两种入参在规范签名串中都是同一个北京时间字面量
        String isoSignStr = PaySignUtil.buildSignStr(iso);
        String beijingTimeSignStr = PaySignUtil.buildSignStr(beijingTime);
        assertTrue(isoSignStr.contains("reqTime=2026-09-11 10:58:12"), isoSignStr);
        assertEquals(isoSignStr, beijingTimeSignStr, "同一瞬时不同入参写法的规范签名串应一致");
    }

    /// 校验 JSON 中指定字段是否为北京时间字面量
    private boolean assertBeijingTime(String json, String field) {
        for (String part : json.split(",")) {
            if (part.contains("\"" + field + "\"")) {
                int idx = part.indexOf(':', part.indexOf("\"" + field + "\""));
                String literal = part.substring(idx + 1).replace("\"", "").replace("}", "").trim();
                return WALL_CLOCK.matcher(literal).matches();
            }
        }
        return false;
    }
}
