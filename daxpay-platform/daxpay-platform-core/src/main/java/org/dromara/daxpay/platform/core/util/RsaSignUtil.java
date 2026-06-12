package org.dromara.daxpay.platform.core.util;

import org.dromara.daxpay.platform.core.exception.ValidationFailedException;
import lombok.SneakyThrows;
import lombok.experimental.UtilityClass;

import javax.crypto.Cipher;
import java.security.*;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

/// # Rsa签名工具类
///
@UtilityClass
public class RsaSignUtil {

    private static final String SIGNATURE_ALGORITHM = "SHA256withRSA";

    /// 读取私钥
    public PrivateKey loadPrivateKeyFromPem(String pemContent) {
        String privateKeyPEM = pemContent
                .replace("-----BEGIN PRIVATE KEY-----", "")
                .replace("-----END PRIVATE KEY-----", "")
                .replace("-----BEGIN RSA PRIVATE KEY-----", "")
                .replace("-----END RSA PRIVATE KEY-----", "")
                .replaceAll("\\s", "");

        byte[] decoded = Base64.getDecoder().decode(privateKeyPEM);

        try {
            PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(decoded);
            KeyFactory kf = KeyFactory.getInstance("RSA");
            return kf.generatePrivate(keySpec);
        } catch (Exception e) {
            // 私钥解析失败，需要为 PKCS#8 格式的Rsa私钥
            throw new ValidationFailedException("error.common.rsaPrivateKeyParseFailed");
        }
    }

    /// 从 PEM 文件读取公钥
    public PublicKey loadPublicKeyFromPem(String pemContent) {
        String publicKeyPEM = pemContent
                .replace("-----BEGIN PUBLIC KEY-----", "")
                .replace("-----END PUBLIC KEY-----", "")
                .replaceAll("\\s", "");

        byte[] decoded = Base64.getDecoder().decode(publicKeyPEM);
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(decoded);
        try {
            KeyFactory kf = KeyFactory.getInstance("RSA");
            return kf.generatePublic(keySpec);
        } catch (Exception e) {
            // 公钥解析失败，需要为 PKCS#8 格式的Rsa公钥
            throw new ValidationFailedException("error.common.rsaPublicKeyParseFailed");
        }
    }

    /// 私钥签名（SHA256withRSA）
    @SneakyThrows
    public String sign(String data, String privateKeyContent) {
        PrivateKey privateKey = loadPrivateKeyFromPem(privateKeyContent);
        Signature signature = Signature.getInstance(SIGNATURE_ALGORITHM);
        signature.initSign(privateKey);
        signature.update(data.getBytes());
        byte[] signedBytes = signature.sign();
        return Base64.getEncoder().encodeToString(signedBytes);
    }

    /// 公钥验签
    @SneakyThrows
    public boolean verify(String data, String sign, String publicKeyContent) {
        PublicKey publicKey = loadPublicKeyFromPem(publicKeyContent);
        Signature signature = Signature.getInstance(SIGNATURE_ALGORITHM);
        signature.initVerify(publicKey);
        signature.update(data.getBytes());
        byte[] sigBytes = Base64.getDecoder().decode(sign);
        return signature.verify(sigBytes);
    }

    /// 私钥解密（RSA/ECB/PKCS1Padding）
    /// @param ciphertext Base64编码的密文
    /// @param privateKeyContent PEM格式的私钥
    /// @return 解密后的明文
    @SneakyThrows
    public String decrypt(String ciphertext, String privateKeyContent) {
        PrivateKey privateKey = loadPrivateKeyFromPem(privateKeyContent);
        Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
        cipher.init(Cipher.DECRYPT_MODE, privateKey);
        byte[] decryptedBytes = cipher.doFinal(Base64.getDecoder().decode(ciphertext));
        return new String(decryptedBytes);
    }

    /// 公钥加密（RSA/ECB/PKCS1Padding）
    /// @param plaintext 明文
    /// @param publicKeyContent PEM格式的公钥
    /// @return Base64编码的密文
    @SneakyThrows
    public String encrypt(String plaintext, String publicKeyContent) {
        PublicKey publicKey = loadPublicKeyFromPem(publicKeyContent);
        Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
        cipher.init(Cipher.ENCRYPT_MODE, publicKey);
        byte[] encryptedBytes = cipher.doFinal(plaintext.getBytes());
        return Base64.getEncoder().encodeToString(encryptedBytes);
    }

    /// 生成 PEM 格式的 RSA 密钥对（文本形式）
    /// @return String[0] = 公钥 PEM, String[1] = 私钥 PEM
    public String[] genRsaPemKey() {
        try {
            int keySize = 2048;
            KeyPairGenerator gen = KeyPairGenerator.getInstance("RSA");
            gen.initialize(keySize, new SecureRandom());
            KeyPair keyPair = gen.generateKeyPair();

            byte[] privateKeyBytes = keyPair.getPrivate().getEncoded();
            byte[] publicKeyBytes  = keyPair.getPublic().getEncoded();

            String privateKeyPem = toPemString(privateKeyBytes, "PRIVATE KEY");
            String publicKeyPem  = toPemString(publicKeyBytes,  "PUBLIC KEY");

            return new String[]{publicKeyPem, privateKeyPem};
        } catch (Exception e) {
            throw new RuntimeException("生成 RSA PEM 密钥失败", e);
        }
    }

    /// 将 DER 编码的字节数组转换为 PEM 格式字符串
    private String toPemString(byte[] derBytes, String type) {
        String base64 = Base64.getEncoder().encodeToString(derBytes);
        StringBuilder pem = new StringBuilder();
        pem.append("-----BEGIN ").append(type).append("-----\n");
        int lineLength = 64;
        for (int i = 0; i < base64.length(); i += lineLength) {
            int end = Math.min(i + lineLength, base64.length());
            pem.append(base64, i, end).append("\n");
        }
        pem.append("-----END ").append(type).append("-----\n");
        return pem.toString();
    }
}

