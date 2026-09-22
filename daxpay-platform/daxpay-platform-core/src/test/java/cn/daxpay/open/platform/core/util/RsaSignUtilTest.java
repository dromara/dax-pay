package cn.daxpay.open.platform.core.util;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class RsaSignUtilTest {

    @Test
    void normalizePublicKeyPem_foldedSingleLine_shouldReturnStandardPem() {
        // 样本密钥现生成, 避免硬编码
        String publicKeyPem = RsaSignUtil.genRsaPemKey()[0];
        // yml 引号跨行会被 YAML 折成空格: 换行丢失, 压成一整行
        String folded = publicKeyPem.replace("\n", " ").trim();
        assertFalse(folded.contains("\n"));

        String normalized = RsaSignUtil.normalizePublicKeyPem(folded);

        assertEquals(publicKeyPem, normalized);
        assertNotNull(RsaSignUtil.loadPublicKeyFromPem(normalized));
    }

    @Test
    void normalizePublicKeyPem_bareBase64_shouldReturnStandardPem() {
        String publicKeyPem = RsaSignUtil.genRsaPemKey()[0];
        // 运维面板注入的是单行纯 Base64(env 文件按行解析, 承载不了换行)
        String bare = publicKeyPem
                .replace("-----BEGIN PUBLIC KEY-----", "")
                .replace("-----END PUBLIC KEY-----", "")
                .replaceAll("\\s", "");

        assertEquals(publicKeyPem, RsaSignUtil.normalizePublicKeyPem(bare));
    }

    @Test
    void normalizePublicKeyPem_standardPem_shouldStayUnchanged() {
        String publicKeyPem = RsaSignUtil.genRsaPemKey()[0];

        // 幂等: 标准 PEM 归一后仍是自身
        assertEquals(publicKeyPem, RsaSignUtil.normalizePublicKeyPem(publicKeyPem));
    }

    @Test
    void normalizePublicKeyPem_shouldWrapBodyAt64Chars() {
        String normalized = RsaSignUtil.normalizePublicKeyPem(RsaSignUtil.genRsaPemKey()[0]);
        String[] lines = normalized.split("\n");

        assertEquals("-----BEGIN PUBLIC KEY-----", lines[0]);
        assertEquals("-----END PUBLIC KEY-----", lines[lines.length - 1]);
        // 正文按 64 字符换行(末行除外), 与 OpenSSL 等工具产出的 PEM 一致
        for (int i = 1; i < lines.length - 1; i++) {
            assertTrue(lines[i].length() <= 64, "正文行超过 64 字符: " + lines[i].length());
        }
        assertEquals(64, lines[1].length());
    }

    @Test
    void normalizePublicKeyPem_nonBase64Content_shouldReturnAsIs() {
        // PKCS#1 等非 X.509 形态不做包装: 原样返回, 不把配置错误包装成看似正常的 PEM
        String pkcs1 = "-----BEGIN RSA PUBLIC KEY-----\nMIIBCgKCAQEA\n-----END RSA PUBLIC KEY-----";

        assertEquals(pkcs1, RsaSignUtil.normalizePublicKeyPem(pkcs1));
    }

    @Test
    void normalizePublicKeyPem_nullOrBlank_shouldReturnAsIs() {
        assertNull(RsaSignUtil.normalizePublicKeyPem(null));
        assertEquals("  ", RsaSignUtil.normalizePublicKeyPem("  "));
    }
}
