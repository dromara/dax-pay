package cn.daxpay.open.platform.core.util;

import java.math.BigInteger;
import java.security.GeneralSecurityException;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.RSAPrivateCrtKeySpec;
import java.security.spec.RSAPublicKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Arrays;
import java.util.Base64;

/// # RSA 密钥加载工具（PKCS#8 / PKCS#1 双格式 + PEM 头尾剥离）
///
/// 商户从各通道平台导出的密钥格式并不统一：
/// - 私钥可能是 PKCS#8(`-----BEGIN PRIVATE KEY-----`)也可能是 PKCS#1(`-----BEGIN RSA PRIVATE KEY-----`)；
/// - 公钥可能是 X.509(`-----BEGIN PUBLIC KEY-----`)也可能是 PKCS#1(`-----BEGIN RSA PUBLIC KEY-----`)。
///
/// 本工具统一剥离 PEM 头尾与空白后，先按标准格式(PKCS#8 / X.509)解析，失败再回退 PKCS#1
/// （DER 解析出 RSA 参数后用 [RSAPrivateCrtKeySpec] / [RSAPublicKeySpec] 重建），
/// 避免商户被迫手工转换密钥格式。
///
/// 通道子应用与主应用为独立工程、无共享 jar，各自保留同源副本（包路径一致），改一处需同步其余两处。
///
/// 异常为内部技术异常，由调用方包装成业务异常（不直接面向用户），故文案不参与国际化。
public final class RsaKeyLoader {

    private static final String KEY_ALGORITHM = "RSA";

    private RsaKeyLoader() {
    }

    /// 加载私钥（PKCS#8 优先，失败回退 PKCS#1）
    ///
    /// @param keyContent 私钥内容，PEM 全文或裸 Base64 均可
    /// @return RSA 私钥
    /// @throws InvalidKeySpecException 两种格式均无法解析时抛出
    public static PrivateKey loadPrivateKey(String keyContent) throws InvalidKeySpecException {
        byte[] decoded = decode(keyContent);
        KeyFactory factory = keyFactory();
        try {
            return factory.generatePrivate(new PKCS8EncodedKeySpec(decoded));
        } catch (InvalidKeySpecException e) {
            // 非 PKCS#8 结构, 按 PKCS#1 RSAPrivateKey(SEQUENCE{version,n,e,d,p,q,dp,dq,qInv}) 解析
            DerReader reader = new DerReader(decoded);
            reader.readSequenceHeader();
            if (!BigInteger.ZERO.equals(reader.readInteger())) {
                throw new InvalidKeySpecException("不支持的 PKCS#1 私钥: 多素数版本号非 0");
            }
            return factory.generatePrivate(new RSAPrivateCrtKeySpec(
                    reader.readInteger(), reader.readInteger(), reader.readInteger(),
                    reader.readInteger(), reader.readInteger(), reader.readInteger(),
                    reader.readInteger(), reader.readInteger()));
        }
    }

    /// 加载公钥（X.509 优先，失败回退 PKCS#1）
    ///
    /// @param keyContent 公钥内容，PEM 全文或裸 Base64 均可
    /// @return RSA 公钥
    /// @throws InvalidKeySpecException 两种格式均无法解析时抛出
    public static PublicKey loadPublicKey(String keyContent) throws InvalidKeySpecException {
        byte[] decoded = decode(keyContent);
        KeyFactory factory = keyFactory();
        try {
            return factory.generatePublic(new X509EncodedKeySpec(decoded));
        } catch (InvalidKeySpecException e) {
            // 非 X.509 结构, 按 PKCS#1 RSAPublicKey(SEQUENCE{n,e}) 解析
            DerReader reader = new DerReader(decoded);
            reader.readSequenceHeader();
            return factory.generatePublic(new RSAPublicKeySpec(reader.readInteger(), reader.readInteger()));
        }
    }

    /// 归一化为 PKCS#8 裸 Base64 私钥
    ///
    /// 供只认 PKCS#8 的第三方 SDK 使用：先按双格式加载，再取 `getEncoded()`（JDK 对 RSA 私钥固定输出 PKCS#8 DER）。
    public static String toPkcs8Base64(String keyContent) throws InvalidKeySpecException {
        return Base64.getEncoder().encodeToString(loadPrivateKey(keyContent).getEncoded());
    }

    /// 归一化为 X.509 裸 Base64 公钥
    ///
    /// 供只认 X.509 的第三方 SDK 使用：先按双格式加载，再取 `getEncoded()`（JDK 对 RSA 公钥固定输出 X.509 DER）。
    public static String toX509Base64(String keyContent) throws InvalidKeySpecException {
        return Base64.getEncoder().encodeToString(loadPublicKey(keyContent).getEncoded());
    }

    /// 剥离 PEM 头尾与全部空白（兼容 PKCS#8 / X.509 / PKCS#1 各类头尾，含转义换行 `\n` 形态）
    public static String stripPem(String keyContent) {
        if (keyContent == null) {
            return null;
        }
        return keyContent
                // 配置文件里被转义的换行(字面量 \n / \r), Base64 正文不含反斜杠, 可安全剔除
                .replace("\\n", "")
                .replace("\\r", "")
                .replaceAll("-----[A-Z0-9 ]+-----", "")
                .replaceAll("\\s+", "");
    }

    /// Base64 解码（先剥离 PEM 头尾），非法输入统一收敛为 [InvalidKeySpecException]
    private static byte[] decode(String keyContent) throws InvalidKeySpecException {
        String stripped = stripPem(keyContent);
        if (stripped == null || stripped.isEmpty()) {
            throw new InvalidKeySpecException("RSA 密钥内容为空");
        }
        try {
            return Base64.getDecoder().decode(stripped);
        } catch (IllegalArgumentException e) {
            throw new InvalidKeySpecException("RSA 密钥不是合法的 Base64/PEM 文本", e);
        }
    }

    private static KeyFactory keyFactory() throws InvalidKeySpecException {
        try {
            return KeyFactory.getInstance(KEY_ALGORITHM);
        } catch (GeneralSecurityException e) {
            throw new InvalidKeySpecException("RSA KeyFactory 不可用", e);
        }
    }

    /// 极简 DER 读取器（仅 SEQUENCE / INTEGER，服务于 PKCS#1 密钥解析，零外部依赖）
    private static final class DerReader {

        private final byte[] data;

        private int pos;

        private DerReader(byte[] data) {
            this.data = data;
        }

        /// 读 SEQUENCE 头（跳过 tag+len，不校验内容边界）
        private void readSequenceHeader() throws InvalidKeySpecException {
            int tag = readTag();
            if (tag != 0x30) {
                throw new InvalidKeySpecException("DER 结构异常: 期望 SEQUENCE, 实际 tag=0x" + Integer.toHexString(tag));
            }
            readLength();
        }

        /// 读 INTEGER（正数语义，忽略前导 0x00 符号位）
        private BigInteger readInteger() throws InvalidKeySpecException {
            int tag = readTag();
            if (tag != 0x02) {
                throw new InvalidKeySpecException("DER 结构异常: 期望 INTEGER, 实际 tag=0x" + Integer.toHexString(tag));
            }
            int len = readLength();
            if (pos + len > data.length) {
                throw new InvalidKeySpecException("DER 结构异常: INTEGER 长度越界");
            }
            byte[] value = Arrays.copyOfRange(data, pos, pos + len);
            pos += len;
            return new BigInteger(1, value);
        }

        private int readTag() throws InvalidKeySpecException {
            if (pos >= data.length) {
                throw new InvalidKeySpecException("DER 结构异常: 数据提前结束");
            }
            return data[pos++] & 0xFF;
        }

        /// 读长度（短形式 / 长形式 0x81、0x82）
        private int readLength() throws InvalidKeySpecException {
            if (pos >= data.length) {
                throw new InvalidKeySpecException("DER 结构异常: 数据提前结束");
            }
            int len = data[pos++] & 0xFF;
            if ((len & 0x80) == 0) {
                return len;
            }
            int count = len & 0x7F;
            if (count == 0 || count > 4) {
                throw new InvalidKeySpecException("DER 长度字节异常: 0x" + Integer.toHexString(len));
            }
            int value = 0;
            for (int i = 0; i < count; i++) {
                if (pos >= data.length) {
                    throw new InvalidKeySpecException("DER 结构异常: 长度字段越界");
                }
                value = (value << 8) | (data[pos++] & 0xFF);
            }
            return value;
        }
    }
}
