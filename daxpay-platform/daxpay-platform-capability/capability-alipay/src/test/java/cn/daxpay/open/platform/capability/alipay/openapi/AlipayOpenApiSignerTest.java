package cn.daxpay.open.platform.capability.alipay.openapi;

import org.junit.jupiter.api.Test;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.util.Base64;
import java.util.LinkedHashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/// 支付宝 OpenAPI 签名工具单测
class AlipayOpenApiSignerTest {

    @Test
    void getSignContent_sortsAndSkipsEmpty() {
        Map<String, String> params = new LinkedHashMap<>();
        params.put("method", "alipay.system.oauth.token");
        params.put("app_id", "2021000000000000");
        params.put("sign", "should-skip");
        params.put("code", "authcode");
        params.put("empty", "");
        params.put("charset", "UTF-8");

        String content = AlipayOpenApiSigner.getSignContent(params);
        assertEquals(
                "app_id=2021000000000000&charset=UTF-8&code=authcode&method=alipay.system.oauth.token",
                content);
    }

    @Test
    void rsa2SignAndVerify_roundTrip() throws Exception {
        KeyPairGenerator generator = KeyPairGenerator.getInstance("RSA");
        generator.initialize(2048);
        KeyPair keyPair = generator.generateKeyPair();
        String privateKey = Base64.getEncoder().encodeToString(keyPair.getPrivate().getEncoded());
        String publicKey = Base64.getEncoder().encodeToString(keyPair.getPublic().getEncoded());

        String content = "app_id=2021&method=alipay.system.oauth.token";
        String sign = AlipayOpenApiSigner.rsa2Sign(content, privateKey);
        assertTrue(AlipayOpenApiSigner.rsa2Verify(content, sign, publicKey));
    }
}
