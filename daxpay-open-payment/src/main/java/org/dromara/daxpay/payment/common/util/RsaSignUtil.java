package org.dromara.daxpay.payment.common.util;

import cn.bootx.platform.core.exception.ValidationFailedException;
import org.dromara.daxpay.payment.common.properties.DaxPayProperties;
import cn.hutool.core.util.ReflectUtil;
import cn.hutool.extra.spring.SpringUtil;
import lombok.SneakyThrows;
import lombok.experimental.UtilityClass;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.security.*;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

/**
 * Rsa签名工具类
 * @author xxm
 * @since 2025/10/1
 */
@UtilityClass
public class RsaSignUtil {

    private static final String SIGNATURE_ALGORITHM = "SHA256withRSA";

    /**
     * 读取私钥
     */
    public PrivateKey loadPrivateKeyFromPem(String pemContent) {
        String privateKeyPEM = pemContent
                .replace("-----BEGIN PRIVATE KEY-----", "")
                .replace("-----END PRIVATE KEY-----", "")
                .replace("-----BEGIN RSA PRIVATE KEY-----", "")
                .replace("-----END RSA PRIVATE KEY-----", "")
                .replaceAll("\\s", "");

        byte[] decoded = Base64.getDecoder().decode(privateKeyPEM);

        // 尝试 PKCS#8
        try {
            PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(decoded);
            KeyFactory kf = KeyFactory.getInstance("RSA");
            return kf.generatePrivate(keySpec);
        } catch (Exception e) {
            throw new ValidationFailedException("私钥解析失败，需要为 PKCS#8 格式的Rsa私钥");
        }
    }

    /**
     * 从 PEM 文件读取公钥
     */
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
            throw new ValidationFailedException("公钥解析失败，需要为 PKCS#8 格式的Rsa公钥");
        }
    }

    /**
     * 私钥签名（SHA256withRSA）
     */
    @SneakyThrows
    public String sign(String data, String privateKeyContent)  {
        PrivateKey privateKey = loadPrivateKeyFromPem(privateKeyContent);
        Signature signature = Signature.getInstance(SIGNATURE_ALGORITHM);
        signature.initSign(privateKey);
        signature.update(data.getBytes());
        byte[] signedBytes = signature.sign();
        return Base64.getEncoder().encodeToString(signedBytes);
    }

    /**
     * 公钥验签
     */
    @SneakyThrows
    public boolean verify(String data, String sign, String publicKeyContent) {
        PublicKey publicKey = loadPublicKeyFromPem(publicKeyContent);
        Signature signature = Signature.getInstance(SIGNATURE_ALGORITHM);
        signature.initVerify(publicKey);
        signature.update(data.getBytes());
        byte[] sigBytes = Base64.getDecoder().decode(sign);
        return signature.verify(sigBytes);
    }

    /**
     * 生成 PEM 格式的 RSA 密钥对（文本形式）
     *
     * @return String[0] = 公钥 PEM, String[1] = 私钥 PEM
     */
    public String[] genRsaPemKey() {
        try {
            int keySize = 2048;
            // 生成密钥对
            KeyPairGenerator gen = KeyPairGenerator.getInstance("RSA");
            gen.initialize(keySize, new SecureRandom());
            KeyPair keyPair = gen.generateKeyPair();

            // 获取编码后的字节
            byte[] privateKeyBytes = keyPair.getPrivate().getEncoded(); // PKCS#8
            byte[] publicKeyBytes = keyPair.getPublic().getEncoded();   // X.509

            // 转为 PEM 文本
            String privateKeyPem = toPemString(privateKeyBytes, "PRIVATE KEY");
            String publicKeyPem = toPemString(publicKeyBytes, "PUBLIC KEY");

            return new String[]{publicKeyPem, privateKeyPem};

        } catch (Exception e) {
            throw new RuntimeException("生成 RSA PEM 密钥失败", e);
        }
    }

    /**
     * 将 DER 编码的字节数组转换为 PEM 格式字符串
     *
     * @param derBytes DER 编码的密钥字节
     * @param type     类型，如 "PRIVATE KEY" 或 "PUBLIC KEY"
     * @return PEM 格式的字符串
     */
    private String toPemString(byte[] derBytes, String type) {
        String base64 = Base64.getEncoder().encodeToString(derBytes);
        StringBuilder pem = new StringBuilder();
        pem.append("-----BEGIN ").append(type).append("-----\n");

        // 每 64 个字符换行（PEM 标准）
        int lineLength = 64;
        for (int i = 0; i < base64.length(); i += lineLength) {
            int end = Math.min(i + lineLength, base64.length());
            pem.append(base64, i, end).append("\n");
        }

        pem.append("-----END ").append(type).append("-----\n");
        return pem.toString();
    }

    /**
     * 读取系统识别密钥
     */
    public String getSystemPemKey() {
        try {
            DaxPayProperties daxPayProperties = SpringUtil.getBean(DaxPayProperties.class);
            String clazz = daxPayProperties.getLicenseKey().getClazz();
            String method = daxPayProperties.getLicenseKey().getPasswordMethod();
            if (clazz == null || method == null) {
                return null;
            }
            Class<?> aClass = Class.forName(clazz);
            Method generateFixedPassword = ReflectUtil.getMethodByName(aClass, method);
            Object invoke = generateFixedPassword.invoke(null);
            return invoke.toString();
        } catch (ClassNotFoundException | IllegalAccessException | InvocationTargetException e) {
            return null;
        }
    }

    /**
     * 获取公钥
     */
    public String getPublicKey() {
        try {
            DaxPayProperties daxPayProperties = SpringUtil.getBean(DaxPayProperties.class);
            String clazz = daxPayProperties.getLicenseKey().getClazz();
            String method = daxPayProperties.getLicenseKey().getPublicKeyMethod();
            if (clazz == null || method == null) {
                return null;
            }
            Class<?> aClass = Class.forName(clazz);
            Method generateFixedPassword = ReflectUtil.getMethodByName(aClass, method);
            Object invoke = generateFixedPassword.invoke(null);
            return invoke.toString();
        } catch (ClassNotFoundException | IllegalAccessException | InvocationTargetException e) {
            return null;
        }
    }
}
