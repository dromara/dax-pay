package cn.daxpay.open.platform.iam.auth.service.twofactor;

import cn.daxpay.open.platform.iam.auth.service.IamSecurityConfigService;
import cn.daxpay.open.platform.system.entity.config.platform.security.PlatformTwoFactorAuthConfig;
import cn.hutool.core.codec.Base32;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.URLUtil;
import cn.hutool.crypto.digest.HMac;
import cn.hutool.crypto.digest.HmacAlgorithm;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/// # TOTP 双因素认证核心服务
///
/// 基于 RFC 6238 自研实现(无第三方依赖), 复用项目已有的 hutool(HMAC + Base32)。
/// 提供 TOTP 密钥生成、otpauth URI 构造、动态码校验。
/// 算法 / 步长 / 位数 / 时间窗口偏移均由平台配置 [PlatformTwoFactorAuthConfig] 驱动,
/// 校验时按配置的 timeWindowOffset 容忍时钟漂移。
/// 二维码图片由前端根据 otpauth URI 自行渲染, 本服务不依赖任何图片库。
///
@Slf4j
@Service
@RequiredArgsConstructor
public class TotpService {

    /// 密钥字节数(160 bit, RFC 6238 推荐最小长度)
    private static final int SECRET_BYTES = 20;

    private final IamSecurityConfigService iamSecurityConfigService;

    /// 生成新的 TOTP 密钥(Base32 编码)
    public String generateSecret() {
        return Base32.encode(RandomUtil.randomBytes(SECRET_BYTES));
    }

    /// 构建 otpauth URI, 供前端渲染二维码或手动录入
    ///
    /// @param secret  TOTP 密钥(Base32)
    /// @param account 账号(作为 label, 区分不同用户)
    public String buildOtpAuthUri(String secret, String account) {
        PlatformTwoFactorAuthConfig config = getConfig();
        String algorithm = parseAlgorithm(config.getAlgorithm());
        int digits = defaultIfNull(config.getCodeLength(), 6);
        int period = defaultIfNull(config.getTimeStep(), 30);
        String issuer = (config.getIssuer() == null || config.getIssuer().isBlank()) ? "DaxPay" : config.getIssuer();
        // otpauth://totp/Issuer:account?secret=...&issuer=...&algorithm=...&digits=...&period=...
        String label = URLUtil.encodeAll(issuer) + ":" + URLUtil.encodeAll(account == null ? "" : account);
        return String.format(
                "otpauth://totp/%s?secret=%s&issuer=%s&algorithm=%s&digits=%d&period=%d",
                label, secret, URLUtil.encodeAll(issuer), algorithm, digits, period);
    }

    /// 校验 TOTP 动态码
    ///
    /// @param secret TOTP 密钥(Base32)
    /// @param code   用户输入的动态码
    /// @return 校验通过返回 true, 失败或异常返回 false
    public boolean verifyCode(String secret, String code) {
        if (secret == null || secret.isBlank() || code == null || code.isBlank()) {
            return false;
        }
        try {
            PlatformTwoFactorAuthConfig config = getConfig();
            String algorithm = parseAlgorithm(config.getAlgorithm());
            int digits = defaultIfNull(config.getCodeLength(), 6);
            int period = defaultIfNull(config.getTimeStep(), 30);
            int discrepancy = defaultIfNull(config.getTimeWindowOffset(), 1);
            byte[] key = Base32.decode(secret);
            // 当前时间桶(Unix 秒 / 步长)
            long currentBucket = System.currentTimeMillis() / 1000L / period;
            // 容忍时钟漂移: 前后各 discrepancy 个时间桶
            for (long offset = -discrepancy; offset <= discrepancy; offset++) {
                String expected = generateCode(key, currentBucket + offset, algorithm, digits);
                if (expected != null && expected.equals(code)) {
                    return true;
                }
            }
            return false;
        }
        catch (Exception e) {
            log.warn("TOTP 校验异常: {}", e.getMessage());
            return false;
        }
    }

    /// 按指定时间桶生成 TOTP 码(RFC 6238 动态截取)
    private String generateCode(byte[] key, long timeBucket, String algorithm, int digits) {
        byte[] timeBytes = longToBytes(timeBucket);
        HMac hmac = new HMac(toHmacAlgorithm(algorithm), key);
        byte[] hash = hmac.digest(timeBytes);
        // 动态截取: 取最后一个字节低 4 位作为偏移
        int offset = hash[hash.length - 1] & 0xf;
        int binary = ((hash[offset] & 0x7f) << 24)
                | ((hash[offset + 1] & 0xff) << 16)
                | ((hash[offset + 2] & 0xff) << 8)
                | (hash[offset + 3] & 0xff);
        int mod = (int) Math.pow(10, digits);
        int code = binary % mod;
        // 前导补零到指定位数
        return String.format("%0" + digits + "d", code);
    }

    /// long 转大端 8 字节(RFC 4226 要求的时间因素)
    private byte[] longToBytes(long value) {
        byte[] bytes = new byte[8];
        for (int i = 7; i >= 0; i--) {
            bytes[i] = (byte) (value & 0xff);
            value >>>= 8;
        }
        return bytes;
    }

    /// 算法字符串映射到 hutool 枚举
    private HmacAlgorithm toHmacAlgorithm(String algorithm) {
        return switch (algorithm) {
            case "SHA256" -> HmacAlgorithm.HmacSHA256;
            case "SHA512" -> HmacAlgorithm.HmacSHA512;
            default -> HmacAlgorithm.HmacSHA1;
        };
    }

    /// 解析算法配置为大写标准名(兼容 HmacSHA256 / SHA256 两种写法), 无法识别回退 SHA1
    private String parseAlgorithm(String algorithm) {
        if (algorithm == null || algorithm.isBlank()) {
            return "SHA1";
        }
        // 兼容前端 "HmacSHA256" 与标准 "SHA256" 两种格式, 统一去除 Hmac 前缀
        String upper = algorithm.toUpperCase().replace("HMAC", "");
        return switch (upper) {
            case "SHA256" -> "SHA256";
            case "SHA512" -> "SHA512";
            default -> "SHA1";
        };
    }

    private PlatformTwoFactorAuthConfig getConfig() {
        return iamSecurityConfigService.getTwoFactorAuthConfig();
    }

    private int defaultIfNull(Integer value, int defaultValue) {
        return value == null ? defaultValue : value;
    }
}
