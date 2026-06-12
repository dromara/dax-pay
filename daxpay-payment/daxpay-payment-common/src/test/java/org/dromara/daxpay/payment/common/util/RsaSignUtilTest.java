package org.dromara.daxpay.payment.common.util;

import org.dromara.daxpay.platform.core.util.RsaSignUtil;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/// # Rsa签名测试类
class RsaSignUtilTest {

    @Test
    @DisplayName("RSA签名和验签-动态密钥")
    void shouldSignAndVerify_withDynamicKey() {
        String[] strings = RsaSignUtil.genRsaPemKey();
        String publicKeyStr = strings[0];
        String privateKeyStr = strings[1];

        String message = "This is a test message for RSA signature.";

        String signature = RsaSignUtil.sign(message, privateKeyStr);

        boolean isValid = RsaSignUtil.verify(message, signature, publicKeyStr);
        assertTrue(isValid, "签名验签应该通过");

        boolean isTamperedValid = RsaSignUtil.verify("This is a TAMPERED message", signature, publicKeyStr);
        assertFalse(isTamperedValid, "篡改数据验签应该失败");
    }

    @Test
    @DisplayName("RSA签名和验签-固定密钥")
    void shouldSignAndVerify_withFixedKey() {
        String publicKeyStr = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEApUzVac6wC/SIEGeh3lfDr9OYFfQy74BFTsZsZ3gsmjC3gGQTMsbPFL7ncOEITx3q3By4YNEpYZ6In1yO7Sjvv55toMXGrX0M82MvQDewNgVnCWQfGLuG1mssyt1/ZUbfZFzE8oi8L5OKO/cf0kxXMq2iBVnqcgRINu9s9+IBpPDx/GE45DPEydp8GLkdF77Px3RJtU8wroMsGTdnIK/+ji34N/pmq8eaUb4swVjgJ2m0hMpQoFgJXfUwuB0biLTBqeM1+dHeEC7k2Eb2YXZDqEYdxyjbqztxm0SmYwl885mcGPBNPTt9ApoRWR3CGRfE8pcQieuB0YdsdSh9+ZSaIwIDAQAB";
        String privateKeyStr = "MIIEvgIBADANBgkqhkiG9w0BAQEFAASCBKgwggSkAgEAAoIBAQClTNVpzrAL9IgQZ6HeV8Ov05gV9DLvgEVOxmxneCyaMLeAZBMyxs8Uvudw4QhPHercHLhg0SlhnoifXI7tKO+/nm2gxcatfQzzYy9AN7A2BWcJZB8Yu4bWayzK3X9lRt9kXMTyiLwvk4o79x/STFcyraIFWepyBEg272z34gGk8PH8YTjkM8TJ2nwYuR0Xvs/HdEm1TzCugywZN2cgr/6OLfg3+marx5pRvizBWOAnabSEylCgWAld9TC4HRuItMGp4zX50d4QLuTYRvZhdkOoRh3HKNurO3GbRKZjCXzzmZwY8E09O30CmhFZHcIZF8TylxCJ64HRh2x1KH35lJojAgMBAAECggEAOR28XCwLzoW3Ahwc5UvkFPwDAAr6EqF60UZkrLfsiXat4VIzBAeIBD4WkH1hNp06ysWtu95p8w4pXQ9JX48WkFp4vOW5ybZ85BhwejsDyxbAzJDo4c3iQHKV7p7sZx0/EVmwv7EZfUL4r9GrECpKsvsmEb1I8g6iuUCvoVNZiBkfBMy7bkDXJDJ/KYuRqEkfPfgqOOicJlXgjyiwtV9RN9SKErvnHo3kyt8RrSbCEYh2y787gtPYf2U1XH674SAxd2Ki0rQ3CqHRjBUQGix7l8G5Hcv+TBJITNIktj8aJfu3CvMnslQU9HEjbU8dMclywRSrjW839rym5lf+ptU/aQKBgQDJ6Bt5gsJlysy8P7agJlmQS0D5GmTx3GELLim1Uhbwwt6gCsQJf2l+0CnuEOBfJPGRdAUi35Tv8gDTG+9Xai0wunNki6AHIfCtvY+zDxdgFtut1eIyq1ASFbAkW1jQRlXshVILAcU5GNjv8wOpV9KcgkMGIsqGUdGb5drsbKDUPwKBgQDRlgtjktHXxDFsdkxUXCObCu6LevgdqgMHqZ4mfui/NkTAuLFaFPddR+cmJNPPsy6MrHj97rZsPSTpaP0vQfJykFDlkm0xfBwvdp41jLhWDN+OxsSs0eyoXptl3XUArsfipQ84tUODp9h86DoPs2oS39LuZj0DncY7yXq1jCOxHQKBgFj603DfcXCOyV+E7KTzgbEXmRCu0yHLr3DP7U2dWcLM/nOlivNslT9v2aqzAU6s51Dkwoa15dtA2aAvxXDOuA+re8MpzWKXUIwg6D1PP0v3huS7R65w1R7DNBcxsphHBwLvVlLHevVIwAIvJMPykjyrI4KGvp4nXKrJx4s97DrdAoGBAJab401HuWH7C6UskYdhuvh0b51t3ZS7knfULODu++REZD21u0THoka3H+UqO8eatI3EdyHLg+3eNoNAvghStJ4dFPUUN0GDNWHqNKC4odK8Z35bWgPyysTnT3ZxIN4/u0YkZP7US1L1r716yBZ2UHiFvTcx4xCRNV3LWFHUBeYFAoGBAJKdUemy9ZFhtox7vLOGU9EPScsh7xcyDcR8dEk+Ixjh8T8TnYjFxg4URAk7/uTki8WF9xcp6YntA92W22+JEje42gDW9bDb8I7BDlMigVCkM2A8cAsr3sHDdfo4N/PISL0dHpt2izEKWb6HZriKAxnJl5/NyDmL/YfSVksVOZud";

        String message = "buyer.contact.phone=8613800138000&buyer.name=张三&goodsList[0].name=商品A&goodsList[0].price=50&merchantId=M1001&nonce=a1b2c3d4e5&orderNo=ORDER_20250405&timestamp=1712345678&totalAmount=100";

        String signature = RsaSignUtil.sign(message, privateKeyStr);

        boolean isValid = RsaSignUtil.verify(message, signature, publicKeyStr);
        assertTrue(isValid, "固定密钥签名验签应该通过");

        boolean isTamperedValid = RsaSignUtil.verify("This is a TAMPERED message", signature, publicKeyStr);
        assertFalse(isTamperedValid, "篡改数据验签应该失败");
    }

    /// 生成公私钥
    @Test
    @DisplayName("生成RSA公私钥对")
    void shouldGenPemKey() {
        String[] strings = RsaSignUtil.genRsaPemKey();
        assertNotNull(strings, "密钥对不应为null");
        assertEquals(2, strings.length, "应返回公钥和私钥两个元素");
        assertNotNull(strings[0], "公钥不应为null");
        assertNotNull(strings[1], "私钥不应为null");
        assertFalse(strings[0].isEmpty(), "公钥不应为空");
        assertFalse(strings[1].isEmpty(), "私钥不应为空");
    }

}
