package cn.daxpay.open.platform.core.util;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.nio.charset.StandardCharsets;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.util.Base64;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

/// [RsaKeyLoader] 双格式密钥加载单测
///
/// 覆盖：PKCS#8 / PKCS#1 私钥、X.509 / PKCS#1 公钥、PEM 与裸 Base64、转义换行、非法输入收敛。
class RsaKeyLoaderTest {

    private static final String DATA = "daxpay-rsa-key-loader-test";

    private static KeyPair keyPair;

    @BeforeAll
    static void init() throws Exception {
        KeyPairGenerator generator = KeyPairGenerator.getInstance("RSA");
        generator.initialize(2048);
        keyPair = generator.generateKeyPair();
    }

    @Test
    @DisplayName("PKCS#8 私钥: PEM 全文与裸 Base64 均可加载")
    void loadPkcs8PrivateKey() throws Exception {
        String pkcs8Base64 = Base64.getEncoder().encodeToString(keyPair.getPrivate().getEncoded());
        PrivateKey fromRaw = RsaKeyLoader.loadPrivateKey(pkcs8Base64);
        PrivateKey fromPem = RsaKeyLoader.loadPrivateKey(wrapPem(pkcs8Base64, "PRIVATE KEY"));
        assertArrayEquals(keyPair.getPrivate().getEncoded(), fromRaw.getEncoded());
        assertArrayEquals(fromRaw.getEncoded(), fromPem.getEncoded());
    }

    @Test
    @DisplayName("PKCS#1 私钥: BEGIN RSA PRIVATE KEY 形态可加载且与 PKCS#8 等价")
    void loadPkcs1PrivateKey() throws Exception {
        String pkcs1Base64 = Base64.getEncoder().encodeToString(pkcs1PrivateKeyDer());
        PrivateKey loaded = RsaKeyLoader.loadPrivateKey(wrapPem(pkcs1Base64, "RSA PRIVATE KEY"));
        assertArrayEquals(keyPair.getPrivate().getEncoded(), loaded.getEncoded());
        // 签名/验签互通
        assertTrue(verify(loaded, keyPair.getPublic()));
    }

    @Test
    @DisplayName("X.509 公钥: PEM 与裸 Base64 均可加载")
    void loadX509PublicKey() throws Exception {
        String x509Base64 = Base64.getEncoder().encodeToString(keyPair.getPublic().getEncoded());
        PublicKey fromRaw = RsaKeyLoader.loadPublicKey(x509Base64);
        PublicKey fromPem = RsaKeyLoader.loadPublicKey(wrapPem(x509Base64, "PUBLIC KEY"));
        assertArrayEquals(keyPair.getPublic().getEncoded(), fromRaw.getEncoded());
        assertArrayEquals(fromRaw.getEncoded(), fromPem.getEncoded());
    }

    @Test
    @DisplayName("PKCS#1 公钥: BEGIN RSA PUBLIC KEY 形态可加载且验签通过")
    void loadPkcs1PublicKey() throws Exception {
        String pkcs1Base64 = Base64.getEncoder().encodeToString(pkcs1PublicKeyDer());
        PublicKey loaded = RsaKeyLoader.loadPublicKey(wrapPem(pkcs1Base64, "RSA PUBLIC KEY"));
        assertArrayEquals(keyPair.getPublic().getEncoded(), loaded.getEncoded());
        assertTrue(verify(keyPair.getPrivate(), loaded));
    }

    @Test
    @DisplayName("转义换行与单行 Base64 均可加载")
    void loadEscapedNewlineKey() throws Exception {
        String pkcs8Base64 = Base64.getEncoder().encodeToString(keyPair.getPrivate().getEncoded());
        String escaped = "-----BEGIN PRIVATE KEY-----\\n" + pkcs8Base64 + "\\n-----END PRIVATE KEY-----";
        assertArrayEquals(keyPair.getPrivate().getEncoded(),
                RsaKeyLoader.loadPrivateKey(escaped).getEncoded());
    }

    @Test
    @DisplayName("归一化: toPkcs8Base64 / toX509Base64 输出可被标准格式解析")
    void normalizeKey() throws Exception {
        String pkcs1Priv = wrapPem(Base64.getEncoder().encodeToString(pkcs1PrivateKeyDer()), "RSA PRIVATE KEY");
        String pkcs8 = RsaKeyLoader.toPkcs8Base64(pkcs1Priv);
        assertFalse(pkcs8.contains("BEGIN"));
        assertArrayEquals(keyPair.getPrivate().getEncoded(), RsaKeyLoader.loadPrivateKey(pkcs8).getEncoded());

        String pkcs1Pub = wrapPem(Base64.getEncoder().encodeToString(pkcs1PublicKeyDer()), "RSA PUBLIC KEY");
        String x509 = RsaKeyLoader.toX509Base64(pkcs1Pub);
        assertFalse(x509.contains("BEGIN"));
        assertArrayEquals(keyPair.getPublic().getEncoded(), RsaKeyLoader.loadPublicKey(x509).getEncoded());
    }

    @Test
    @DisplayName("非法输入: 空内容与乱码统一抛 InvalidKeySpecException")
    void invalidKey() {
        assertThrows(java.security.spec.InvalidKeySpecException.class, () -> RsaKeyLoader.loadPrivateKey(""));
        assertThrows(java.security.spec.InvalidKeySpecException.class, () -> RsaKeyLoader.loadPrivateKey("not-a-key"));
        assertThrows(java.security.spec.InvalidKeySpecException.class, () -> RsaKeyLoader.loadPublicKey(null));
    }

    @Test
    @DisplayName("stripPem: 各类 PEM 头尾与空白全部剔除")
    void stripPem() {
        assertEquals("ABC", RsaKeyLoader.stripPem("-----BEGIN RSA PRIVATE KEY-----\nA B\tC\n-----END RSA PRIVATE KEY-----"));
        assertEquals("ABC", RsaKeyLoader.stripPem("-----BEGIN PUBLIC KEY-----ABC-----END PUBLIC KEY-----"));
    }

    /// 从 PKCS#8 的 PrivateKeyInfo 中取出 OCTET STRING 负载，即 PKCS#1 RSAPrivateKey DER
    private static byte[] pkcs1PrivateKeyDer() {
        byte[] pkcs8 = keyPair.getPrivate().getEncoded();
        // SEQUENCE { INTEGER version, SEQUENCE algorithm, OCTET STRING privateKey }
        int pos = 0;
        pos++; // SEQUENCE tag
        pos = skipLength(pkcs8, pos);
        pos++; // version INTEGER tag
        pos = skipLength(pkcs8, pos) + 0; // 版本值(1 字节) 由下面统一跳过
        pos++; // version 值(0x00)
        pos++; // algorithm SEQUENCE tag
        pos = skipLength(pkcs8, pos);
        // 跳过 algorithm OID + NULL
        pos++; // OID tag
        int oidLen = pkcs8[pos++] & 0xFF;
        pos += oidLen;
        pos++; // NULL tag
        pos++; // NULL 长度
        // OCTET STRING
        assertEquals(0x04, pkcs8[pos] & 0xFF);
        pos++;
        int len = readLength(pkcs8, pos);
        pos = pos + lengthBytes(pkcs8, pos);
        return java.util.Arrays.copyOfRange(pkcs8, pos, pos + len);
    }

    /// 从 X.509 SubjectPublicKeyInfo 中取出 BIT STRING 负载，即 PKCS#1 RSAPublicKey DER
    private static byte[] pkcs1PublicKeyDer() {
        byte[] spki = keyPair.getPublic().getEncoded();
        int pos = 0;
        pos++; // SEQUENCE tag
        pos = skipLength(spki, pos);
        pos++; // algorithm SEQUENCE tag
        pos = skipLength(spki, pos);
        pos++; // OID tag
        int oidLen = spki[pos++] & 0xFF;
        pos += oidLen;
        pos++; // NULL tag
        pos++; // NULL 长度
        // BIT STRING: 首字节为未使用位数(0), 其余为 RSAPublicKey DER
        assertEquals(0x03, spki[pos] & 0xFF);
        pos++;
        int len = readLength(spki, pos);
        pos = pos + lengthBytes(spki, pos);
        return java.util.Arrays.copyOfRange(spki, pos + 1, pos + len);
    }

    private static int skipLength(byte[] data, int pos) {
        return pos + lengthBytes(data, pos);
    }

    private static int lengthBytes(byte[] data, int pos) {
        int len = data[pos] & 0xFF;
        if ((len & 0x80) == 0) {
            return 1;
        }
        return 1 + (len & 0x7F);
    }

    private static int readLength(byte[] data, int pos) {
        int len = data[pos] & 0xFF;
        if ((len & 0x80) == 0) {
            return len;
        }
        int count = len & 0x7F;
        int value = 0;
        for (int i = 0; i < count; i++) {
            value = (value << 8) | (data[pos + 1 + i] & 0xFF);
        }
        return value;
    }

    private static String wrapPem(String base64, String label) {
        StringBuilder sb = new StringBuilder();
        sb.append("-----BEGIN ").append(label).append("-----\n");
        for (int i = 0; i < base64.length(); i += 64) {
            sb.append(base64, i, Math.min(i + 64, base64.length())).append('\n');
        }
        sb.append("-----END ").append(label).append("-----");
        return sb.toString();
    }

    private static boolean verify(PrivateKey privateKey, PublicKey publicKey) throws Exception {
        Signature signer = Signature.getInstance("SHA256withRSA");
        signer.initSign(privateKey);
        signer.update(DATA.getBytes(StandardCharsets.UTF_8));
        byte[] sign = signer.sign();
        Signature verifier = Signature.getInstance("SHA256withRSA");
        verifier.initVerify(publicKey);
        verifier.update(DATA.getBytes(StandardCharsets.UTF_8));
        return verifier.verify(sign);
    }
}
