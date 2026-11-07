package cn.daxpay.open.platform.common.json.sensitive;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import tools.jackson.databind.json.JsonMapper;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/// # 敏感信息脱敏序列化测试
///
/// 覆盖 PEM 多行脱敏与原有 front/end 分支的边界:
/// - PEM 按内容识别, 与注解参数无关(6/6 与 24/24 统一为可核对的密钥形态);
/// - 非 PEM 长串回退原单行压缩(24 + 6 星 + 24), 非 compact 回退全长度星号;
/// - 短值与空值保持既有行为。
/// 脱敏是全局出参行为且无其他测试兜底, 此处改动需同步更新本测试。
class SensitiveInfoSerializeTest {

    private final JsonMapper mapper = JsonMapper.builder().build();

    /// PKCS#8 私钥形态(多行, 正文每行 64 字符)
    private static final String PEM_PRIVATE_KEY = """
            -----BEGIN PRIVATE KEY-----
            MIIEvQIBADANBgkqhkiG9w0BAQEFAASCBKcwggSjAgEAAoIBAQC7VJTUt9Us8cKj
            MzEfYyjiWA4R4/M2bS1GB4t7NXp98C3SC6dVMvDuictGeurT8jNbvJZHtCSuYEvu
            NMoSfm76oqFvAp8Gy0iz5sxjZmSnXyCdPEovGhLa0VzMaQ8s+CLOyS56YyCFGeJZ
            qgtzJ6GR3eqoYSW9b9UMvkBpZODSctWSNGj3P7jRFDO5VoTwCQAWbFnOjDfH5Ulg
            p2PKSQnSJP3AJLQNFNe7br1XbrhV//eO+t51mIpGSDCUv3E0DDFcWDTH9cXDTTlR
            ZVEiR2BwpZOOkE/Z0/BVnhZYL71oZV34bKfWjQIt6V/isSMahdsAASACp4ZTGtwi
            VuNd9tybAgMBAAECggEBAKTmjaS6tkK8BlPXClTQ2vpz/N6uxDeS35mXpqasqskV
            laAidgg/sWqpjXDbXr93otIMLlWsM+X0CqMDgSXKejLS2jx4GDjI1ZTXg++0AMJ8
            sJ74pWzVDOfmCEQ/7wXs3+cbnXhKriO8Z036q92Qc1+N87SI38nkGa0ABH9CN83H
            mQqt4fB7UdHzuIRe/me2PGhIq5ZBzj6h3BpoPGzEP+x3l9YmK8t/1cN0pqI+dQwY
            dgfGjackLu/2qH80MCF7IyQaseZUOJyKrCLtSD/Iixv/hzDEUPfOCjFDgTpzf3cw
            ta8+oE4wHCo1iI1/4TlPkwmXx4qSXtmw4aQPz7IDQvECgYEA8KNThCO2gsC2I9PQ
            DM/8Cw0O983WCDY+oi+7JPiNAJwv5DYBqEZB1QYdj06YD16XlC/HAZMsMku1na2T
            N0driwenQQWzoev3g2S7gRDoS/FCJSI3jJ+kjgtaA7Qmzlgk1TxODN+G1H91HW7t
            0l7VnL27IWyYo2qRRK3jzxqUiPUCgYEAx0oQs2reBQGMVZnApD1jeq7n4MvNLcPv
            t8b/eU9iUv6Y4Mj0Suo/AU8lYZXm8ubbqAlwz2VSVunD2tOplHyMUrtCtObAfVDU
            AhCndKaA9gApgfb3xw1IKbuQ1u4IF1FJl3VtumfQn//LiH1B3rXhcdyo3/vIttEk
            48RakUKClU8CgYEAzV7W3COOlDDcQd935DdtKBFRAPRPAlspQUnzMi5eSHMD/ISL
            DY5IiQHbIH83D4bvXq0X7qQoSBSNP7Dvv3HYuqMhf0DaegrlBuJllFVVq9qPVRnK
            xt1Il2HgxOBvbhOT+9in1BzA+YJ99UzC85O0Qz06A+CmtHEy4aZ2kj5hHjECgYEA
            mNS4+A8Fkss8Js1RieK2LniBxMgmYml3pfVLKGnzmng7H2+cwPLhPIzIuwytXywh
            2bzbsYEfYx3EoEVgMEpPhoarQnYPukrJO4gwE2o5Te6T5mJSZGlQJQj9q4ZB2Dfz
            et6INsK0oG8XVGXSpQvQh3RUYekCZQkBBFcpqWpbIEsCgYAnM3DQf3FJoSnXaMhr
            VBIovic5l0xFkEHskAjFTevO86Fsz1C2aSeRKSqGFoOQ0tmJzBEs1R6KqnHInicD
            TQrKhArgLXX4v3CddjfTRJkFWDbE/CkvKZNOrcf1nhaGCPspRJj2KUkj1Fhl9Cnc
            dn/RsYEONbwQSjIfMPkvxF+8HQ==
            -----END PRIVATE KEY-----
            """;

    /// 证书链形态(两个 BEGIN/END 块)
    private static final String PEM_CERT_CHAIN = """
            -----BEGIN CERTIFICATE-----
            MIIBszCCAVmgAwIBAgIUYheadtailheadtailheadtailheadtailheadtailhe
            Y2hhaW5ib2R5bWlkZGxlbWlkZGxlbWlkZGxlbWlkZGxlbWlkZGxlbWlkZGxl
            -----END CERTIFICATE-----
            -----BEGIN CERTIFICATE-----
            MIIBszCCAVmgAwIBAgIUZsecondbodysecondbodysecondbodysecondbodyse
            Y2hhaW50YWlsY2hhaW50YWlsY2hhaW50YWlsY2hhaW50YWlsY2hhaW50YWls
            -----END CERTIFICATE-----
            """;

    @Test
    @DisplayName("PEM 私钥: 六行结构, 完整 BEGIN/END, 正文前后各露 32 字符, 中间两行星号")
    void pemPrivateKey_shouldMaskAsSixLines() {
        String masked = this.maskCompact24(PEM_PRIVATE_KEY);
        String[] lines = masked.split("\\R");
        String[] origin = PEM_PRIVATE_KEY.strip().split("\\R");

        assertEquals(6, lines.length);
        assertEquals("-----BEGIN PRIVATE KEY-----", lines[0]);
        assertEquals("-----END PRIVATE KEY-----", lines[5]);
        assertTrue(lines[1].startsWith(origin[1].substring(0, 32)));
        assertEquals(48, lines[1].length());
        assertTrue(lines[2].matches("\\*{48}"));
        assertTrue(lines[3].matches("\\*{48}"));
        assertTrue(lines[4].endsWith(origin[origin.length - 2]));
        assertEquals(48, lines[4].length());
    }

    @Test
    @DisplayName("PEM 内容即使注解为 6/6 非压缩, 也按 PEM 规则脱敏")
    void pemWithShortFrontEnd_shouldStillUsePemRule() {
        Holder holder = new Holder();
        holder.setValue(PEM_PRIVATE_KEY);
        String masked = this.roundTrip(holder).getValue();

        String[] lines = masked.split("\\R");
        assertEquals(6, lines.length);
        assertEquals("-----BEGIN PRIVATE KEY-----", lines[0]);
        assertTrue(lines[1].startsWith("MIIEvQIBADANBgkqhkiG9w0BAQEF"));
    }

    @Test
    @DisplayName("纯 Base64(无 PEM 头)保持旧压缩规则: 前 24 + 6 星 + 后 24")
    void plainBase64Compact_shouldKeepLegacyCompactRule() {
        String raw = "MIIEvQIBADANBgkqhkiG9w0BAQEFAASCBKcwggSjAgEAAoIBAQC7VJTUt9Us8cKj";
        String masked = this.maskCompact24(raw);

        assertEquals(raw.substring(0, 24) + "******" + raw.substring(raw.length() - 24), masked);
        assertTrue(!masked.contains("\n"));
    }

    @Test
    @DisplayName("短值(保留位数之和超过长度)统一四星全量脱敏")
    void shortValue_shouldMaskAll() {
        assertEquals("****", this.maskCompact24("abc"));
    }

    @Test
    @DisplayName("空值与空白值返回空串")
    void blankValue_shouldReturnEmpty() {
        assertEquals("", this.maskCompact24(""));
        assertEquals("", this.maskCompact24("   "));
    }

    @Test
    @DisplayName("证书链: 保留首个 BEGIN 行与末个 END 行")
    void pemCertChain_shouldKeepFirstBeginAndLastEnd() {
        String masked = this.maskCompact24(PEM_CERT_CHAIN);
        String[] lines = masked.split("\\R");
        String[] origin = PEM_CERT_CHAIN.strip().split("\\R");
        String originTail = origin[origin.length - 2];

        assertEquals(6, lines.length);
        assertEquals("-----BEGIN CERTIFICATE-----", lines[0]);
        assertEquals("-----END CERTIFICATE-----", lines[5]);
        assertTrue(lines[1].startsWith(origin[1].substring(0, 32)));
        assertTrue(lines[4].endsWith(originTail.substring(originTail.length() - 32)));
    }

    @Test
    @DisplayName("仅有 BEGIN/END 两行的空 PEM 回退原逻辑, 不抛异常")
    void pemWithoutBody_shouldFallback() {
        String masked = this.maskCompact24("-----BEGIN PRIVATE KEY-----\n-----END PRIVATE KEY-----");

        assertTrue(!masked.isEmpty());
        assertTrue(!masked.contains("\n"));
    }

    @Test
    @DisplayName("单行 PEM(BEGIN/正文/END 挤在同一行): 还原为六行结构脱敏")
    void inlinePem_shouldMaskAsSixLines() {
        String body = "MIIEvQIBADANBgkqhkiG9w0BAQEFAASCBKcwggSjAgEAAoIBAQC7VJTUt9Us8cKjMzEfYyjiWA4R4";
        String raw = "-----BEGIN PRIVATE KEY-----" + body + "-----END PRIVATE KEY-----";
        String masked = this.maskCompact24(raw);
        String[] lines = masked.split("\\R");

        assertEquals(6, lines.length);
        assertEquals("-----BEGIN PRIVATE KEY-----", lines[0]);
        assertEquals("-----END PRIVATE KEY-----", lines[5]);
        assertTrue(lines[1].startsWith(body.substring(0, 32)));
        assertEquals(48, lines[1].length());
        assertTrue(lines[2].matches("\\*{48}"));
        assertTrue(lines[4].endsWith(body.substring(body.length() - 32)));
        assertEquals(48, lines[4].length());
    }

    @Test
    @DisplayName("纯 Base64 带尾换行: 压缩串不把换行带出")
    void plainBase64WithTrailingNewline_shouldNotContainNewline() {
        String raw = "MIIEvQIBADANBgkqhkiG9w0BAQEFAASCBKcwggSjAgEAAoIBAQC7VJTUt9Us8cKj\n";
        String stripped = raw.strip();
        String masked = this.maskCompact24(raw);

        assertTrue(!masked.contains("\n"));
        assertEquals(stripped.substring(0, 24) + "******" + stripped.substring(stripped.length() - 24), masked);
    }

    /// 以 24/24 压缩注解序列化再反序列化, 取出脱敏后的字段值
    private String maskCompact24(String value) {
        CompactHolder holder = new CompactHolder();
        holder.setValue(value);
        return this.roundTrip(holder).getValue();
    }

    private <T> T roundTrip(T holder) {
        try {
            String json = this.mapper.writeValueAsString(holder);
            @SuppressWarnings("unchecked")
            Class<T> type = (Class<T>) holder.getClass();
            return this.mapper.readValue(json, type);
        } catch (Exception e) {
            throw new IllegalStateException("脱敏序列化往返失败", e);
        }
    }

    /// 24/24 + compact 脱敏持有者(支付宝/微信密钥与模拟通道凭证的实际注解配置)
    public static class CompactHolder {
        @SensitiveInfo(front = 24, end = 24, compact = true)
        private String value;

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }
    }

    /// 裸注解持有者(默认 4/4 + 非压缩), 验证 PEM 识别优先于注解参数
    public static class Holder {
        @SensitiveInfo
        private String value;

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }
    }

}
