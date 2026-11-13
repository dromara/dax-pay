package cn.daxpay.open.platform.capability.alipay.openapi;

import cn.daxpay.open.platform.core.util.RsaKeyLoader;
import lombok.experimental.UtilityClass;

import java.nio.charset.StandardCharsets;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/// # 支付宝开放平台 RSA2 签名/验签
///
/// 对齐官方 SDK `AlipaySignature` 的 RSA2 行为:
/// - 签名串: 除 sign 外非空参数按 key 字典序 `k=v&k=v`
/// - 算法: SHA256withRSA + Base64
/// - 密钥: 支持 PEM 与裸 Base64(PKCS#8/PKCS#1 私钥 / X.509/PKCS#1 公钥)
/// - 字符集固定 UTF-8(避免平台默认编码差异)
///
@UtilityClass
public class AlipayOpenApiSigner {

    private static final String SIGN_ALGORITHM = "SHA256withRSA";

    /// 将参数拼成待签名字符串(排除 sign, 跳过空值, key 字典序)
    public String getSignContent(Map<String, String> params) {
        List<String> keys = new ArrayList<>(params.keySet());
        Collections.sort(keys);
        StringBuilder content = new StringBuilder();
        int index = 0;
        for (String key : keys) {
            if ("sign".equals(key)) {
                continue;
            }
            String value = params.get(key);
            if (Objects.isNull(key) || key.isEmpty() || Objects.isNull(value) || value.isEmpty()) {
                continue;
            }
            if (index > 0) {
                content.append('&');
            }
            content.append(key).append('=').append(value);
            index++;
        }
        return content.toString();
    }

    /// RSA2 私钥签名, 返回 Base64
    public String rsa2Sign(String content, String privateKeyContent) {
        try {
            PrivateKey privateKey = loadPrivateKey(privateKeyContent);
            Signature signature = Signature.getInstance(SIGN_ALGORITHM);
            signature.initSign(privateKey);
            signature.update(content.getBytes(StandardCharsets.UTF_8));
            return Base64.getEncoder().encodeToString(signature.sign());
        } catch (Exception e) {
            throw new IllegalStateException("alipay rsa2 sign failed: " + e.getMessage(), e);
        }
    }

    /// RSA2 公钥验签
    public boolean rsa2Verify(String content, String sign, String publicKeyContent) {
        try {
            PublicKey publicKey = loadPublicKey(publicKeyContent);
            Signature signature = Signature.getInstance(SIGN_ALGORITHM);
            signature.initVerify(publicKey);
            signature.update(content.getBytes(StandardCharsets.UTF_8));
            return signature.verify(Base64.getDecoder().decode(sign));
        } catch (Exception e) {
            return false;
        }
    }

    /// 加载私钥(PKCS#8 优先, 回退 PKCS#1; 兼容 PEM 头与裸 Base64)
    public PrivateKey loadPrivateKey(String keyContent) {
        try {
            return RsaKeyLoader.loadPrivateKey(keyContent);
        } catch (Exception e) {
            throw new IllegalStateException("alipay private key parse failed: " + e.getMessage(), e);
        }
    }

    /// 加载公钥(X.509 优先, 回退 PKCS#1; 兼容 PEM 头与裸 Base64)
    public PublicKey loadPublicKey(String keyContent) {
        try {
            return RsaKeyLoader.loadPublicKey(keyContent);
        } catch (Exception e) {
            throw new IllegalStateException("alipay public key parse failed: " + e.getMessage(), e);
        }
    }
}
