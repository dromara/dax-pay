package org.dromara.daxpay.payment.common.util;

import org.dromara.daxpay.platform.core.util.RsaSignUtil;
import cn.hutool.core.date.DatePattern;
import cn.hutool.json.JSONConfig;
import cn.hutool.json.JSONUtil;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.TreeMap;

import static org.junit.jupiter.api.Assertions.*;

/// # 支付签名测试
class PaySignTest {
    private static final String PUBLIC_KEY = """
            -----BEGIN PUBLIC KEY-----
            MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEApUzVac6wC/SIEGeh3lfD
            r9OYFfQy74BFTsZsZ3gsmjC3gGQTMsbPFL7ncOEITx3q3By4YNEpYZ6In1yO7Sjv
            v55toMXGrX0M82MvQDewNgVnCWQfGLuG1mssyt1/ZUbfZFzE8oi8L5OKO/cf0kxX
            Mq2iBVnqcgRINu9s9+IBpPDx/GE45DPEydp8GLkdF77Px3RJtU8wroMsGTdnIK/+
            ji34N/pmq8eaUb4swVjgJ2m0hMpQoFgJXfUwuB0biLTBqeM1+dHeEC7k2Eb2YXZD
            qEYdxyjbqztxm0SmYwl885mcGPBNPTt9ApoRWR3CGRfE8pcQieuB0YdsdSh9+ZSa
            IwIDAQAB
            -----END PUBLIC KEY-----
            """;
    private static final String PRIVATE_KEY = """
            -----BEGIN PRIVATE KEY-----
            MIIEvgIBADANBgkqhkiG9w0BAQEFAASCBKgwggSkAgEAAoIBAQClTNVpzrAL9IgQ
            Z6HeV8Ov05gV9DLvgEVOxmxneCyaMLeAZBMyxs8Uvudw4QhPHercHLhg0Slhnoif
            XI7tKO+/nm2gxcatfQzzYy9AN7A2BWcJZB8Yu4bWayzK3X9lRt9kXMTyiLwvk4o7
            9x/STFcyraIFWepyBEg272z34gGk8PH8YTjkM8TJ2nwYuR0Xvs/HdEm1TzCugywZ
            N2cgr/6OLfg3+marx5pRvizBWOAnabSEylCgWAld9TC4HRuItMGp4zX50d4QLuTY
            RvZhdkOoRh3HKNurO3GbRKZjCXzzmZwY8E09O30CmhFZHcIZF8TylxCJ64HRh2x1
            KH35lJojAgMBAAECggEAOR28XCwLzoW3Ahwc5UvkFPwDAAr6EqF60UZkrLfsiXat
            4VIzBAeIBD4WkH1hNp06ysWtu95p8w4pXQ9JX48WkFp4vOW5ybZ85BhwejsDyxbA
            zJDo4c3iQHKV7p7sZx0/EVmwv7EZfUL4r9GrECpKsvsmEb1I8g6iuUCvoVNZiBkf
            BMy7bkDXJDJ/KYuRqEkfPfgqOOicJlXgjyiwtV9RN9SKErvnHo3kyt8RrSbCEYh2
            y787gtPYf2U1XH674SAxd2Ki0rQ3CqHRjBUQGix7l8G5Hcv+TBJITNIktj8aJfu3
            CvMnslQU9HEjbU8dMclywRSrjW839rym5lf+ptU/aQKBgQDJ6Bt5gsJlysy8P7ag
            JlmQS0D5GmTx3GELLim1Uhbwwt6gCsQJf2l+0CnuEOBfJPGRdAUi35Tv8gDTG+9X
            ai0wunNki6AHIfCtvY+zDxdgFtut1eIyq1ASFbAkW1jQRlXshVILAcU5GNjv8wOp
            V9KcgkMGIsqGUdGb5drsbKDUPwKBgQDRlgtjktHXxDFsdkxUXCObCu6LevgdqgMH
            qZ4mfui/NkTAuLFaFPddR+cmJNPPsy6MrHj97rZsPSTpaP0vQfJykFDlkm0xfBwv
            dp41jLhWDN+OxsSs0eyoXptl3XUArsfipQ84tUODp9h86DoPs2oS39LuZj0DncY7
            yXq1jCOxHQKBgFj603DfcXCOyV+E7KTzgbEXmRCu0yHLr3DP7U2dWcLM/nOlivNs
            lT9v2aqzAU6s51Dkwoa15dtA2aAvxXDOuA+re8MpzWKXUIwg6D1PP0v3huS7R65w
            1R7DNBcxsphHBwLvVlLHevVIwAIvJMPykjyrI4KGvp4nXKrJx4s97DrdAoGBAJab
            401HuWH7C6UskYdhuvh0b51t3ZS7knfULODu++REZD21u0THoka3H+UqO8eatI3E
            dyHLg+3eNoNAvghStJ4dFPUUN0GDNWHqNKC4odK8Z35bWgPyysTnT3ZxIN4/u0Yk
            ZP7US1L1r716yBZ2UHiFvTcx4xCRNV3LWFHUBeYFAoGBAJKdUemy9ZFhtox7vLOG
            U9EPScsh7xcyDcR8dEk+Ixjh8T8TnYjFxg4URAk7/uTki8WF9xcp6YntA92W22+J
            Eje42gDW9bDb8I7BDlMigVCkM2A8cAsr3sHDdfo4N/PISL0dHpt2izEKWb6HZriK
            AxnJl5/NyDmL/YfSVksVOZud
            -----END PRIVATE KEY-----
            """;

    @Data
    @NoArgsConstructor
    public static class ComplexPaymentRequest {
        private String merchantId;
        private String orderNo;
        private Boolean isTest;
        private Integer version;

        // 金额字段
        private BigDecimal totalAmount;
        private BigDecimal discount;
        private BigDecimal zeroAmount;
        private BigDecimal negativeFee;
        private BigDecimal scientific;
        private BigDecimal preciseAmount;

        // 时间字段
        private LocalDateTime createTime;

        // 嵌套对象
        private Buyer buyer;
        private Address shippingAddress;

        // 列表
        private List<Goods> goodsList;
        private List<String> tags;
        private List<Goods> emptyGoodsList;

        // null 字段
        private String remark;
        private Buyer nullBuyer;

        // ===== 新增：签名字段 =====
        private String sign;

        // 静态字段（不会参与签名）
        public static final String STATIC_FIELD = "SHOULD_BE_IGNORED";
    }

    // ===== 内部类（保持不变）=====
    @Data
    @NoArgsConstructor
    public static class Buyer {
        private String name;
        private String idCard;
        private Contact contact;
    }

    @Data
    @NoArgsConstructor
    public static class Contact {
        private String phone;
        private String email;
    }

    @Data
    @NoArgsConstructor
    public static class Address {
        private String province;
        private String city;
        private String detail;
        private BigDecimal latitude;
        private BigDecimal longitude;
    }

    @Data
    @NoArgsConstructor
    public static class Goods {
        private String name;
        private BigDecimal price;
        private Integer quantity;
    }

    // ================== 手动赋值（不含 sign）==================
    public static ComplexPaymentRequest createComplexRequest() {
        ComplexPaymentRequest request = new ComplexPaymentRequest();

        // 基础字段
        request.setMerchantId("M1001");
        request.setOrderNo("ORDER_20250405_001");
        request.setIsTest(true);
        request.setVersion(2);

        // 金额
        request.setTotalAmount(new BigDecimal("100.00"));
        request.setDiscount(new BigDecimal("12.34500"));
        request.setZeroAmount(new BigDecimal("0.00"));
        request.setNegativeFee(new BigDecimal("-5.500"));
        request.setScientific(new BigDecimal("1E+3"));
        request.setPreciseAmount(new BigDecimal("0.1000000000000001"));

        // 时间
        request.setCreateTime(LocalDateTime.of(2025, 4, 5, 14, 30, 0));

        // 买家
        Buyer buyer = new Buyer();
        buyer.setName("张 三");
        buyer.setIdCard("110101199001011234");
        Contact contact = new Contact();
        contact.setPhone("+86-138-0013-8000");
        contact.setEmail("zhangsan@example.com?from=api&test=1");
        buyer.setContact(contact);
        request.setBuyer(buyer);

        // 地址
        Address address = new Address();
        address.setProvince("广东省");
        address.setCity("深圳市");
        address.setDetail("南山区科技园A座 101室");
        address.setLatitude(new BigDecimal("22.543100"));
        address.setLongitude(new BigDecimal("113.9432000"));
        request.setShippingAddress(address);

        // 商品
        Goods goods1 = new Goods();
        goods1.setName("商品A");
        goods1.setPrice(new BigDecimal("50.00"));
        goods1.setQuantity(1);
        Goods goods2 = new Goods();
        goods2.setName("商品B");
        goods2.setPrice(new BigDecimal("30.500"));
        goods2.setQuantity(2);
        request.setGoodsList(Arrays.asList(goods1, goods2));

        // 其他
        request.setTags(Arrays.asList("促销", "新品", "Free Shipping!"));
        request.setEmptyGoodsList(Arrays.asList());
        request.setRemark(null);
        request.setNullBuyer(null);

        // 注意：sign 字段暂不设置（签名后再赋值）
        return request;
    }

    /// 直接签名和验签
    @Test
    @DisplayName("直接签名和验签-对象方式")
    void shouldSignAndVerify_withObject() {
        ComplexPaymentRequest request = createComplexRequest();

        String signStr = ObjectSignStrUtil.buildSignStr(request);
        String sign = RsaSignUtil.sign(signStr, PRIVATE_KEY);
        request.setSign(sign);

        boolean verify = RsaSignUtil.verify(signStr, sign, PUBLIC_KEY);
        assertTrue(verify, "直接签名验签应该通过");

        // 转换成json后验签
        final JSONConfig JSON_CONFIG = JSONConfig.create()
                .setDateFormat(DatePattern.NORM_DATETIME_PATTERN);
        String json = JSONUtil.toJsonStr(request, JSON_CONFIG);
        TreeMap<String, String> map = JsonSignStrUtil.buildSortedMap(json);
        String jsonSign = map.remove("sign");
        String jsonSignStr = JsonSignStrUtil.buildSignStr(map);
        verify = RsaSignUtil.verify(jsonSignStr, jsonSign, PUBLIC_KEY);
        assertTrue(verify, "JSON方式验签应该通过");
    }

    /// 使用工具类签名和验签
    @Test
    @DisplayName("使用PaySignUtil工具类签名和验签")
    void shouldSignAndVerify_withPaySignUtil() {
        ComplexPaymentRequest request = createComplexRequest();

        String sign = PaySignUtil.sign(request, PRIVATE_KEY);
        assertNotNull(sign, "签名结果不应为null");
        request.setSign(sign);

        boolean verify = PaySignUtil.verify(request, PUBLIC_KEY);
        assertTrue(verify, "对象方式验签应该通过");

        // 转换成json后验签
        final JSONConfig JSON_CONFIG = JSONConfig.create()
                .setDateFormat(DatePattern.NORM_DATETIME_PATTERN);
        String json = JSONUtil.toJsonStr(request, JSON_CONFIG);
        boolean verify2 = PaySignUtil.verify(json, PUBLIC_KEY);
        assertTrue(verify2, "JSON方式验签应该通过");
    }

}
