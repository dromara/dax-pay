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
///
/// 算法(HmacSHA1) / 步长(30s) / 位数(6) 均固定为 RFC 6238 标准值,
/// 确保 Google Authenticator / Microsoft Authenticator 等主流验证器兼容。
/// 校验时按配置的 timeWindowOffset 容忍时钟漂移。
/// 二维码图片由前端根据 otpauth URI 自行渲染, 本服务不依赖任何图片库。
///
@Slf4j
@Service
@RequiredArgsConstructor
public class TotpService {

    /// 密钥字节数(160 bit, RFC 6238 推荐最小长度)
    private static final int SECRET_BYTES = 20;

    /// 固定 6 位动态码(RFC 6238 标准, 主流验证器均支持)
    private static final int DIGITS = 6;

    /// 固定 30 秒步长(RFC 6238 标准, 主流验证器均支持)
    private static final int PERIOD = 30;

    /// 固定 HmacSHA1 算法(RFC 6238 标准, Google/Microsoft Authenticator 仅支持 SHA1)
    private static final HmacAlgorithm ALGORITHM = HmacAlgorithm.HmacSHA1;

    /// 固定时间窗口偏移(前后各 1 个时间桶, 行业标准默认值)
    private static final int DISCREPANCY = 1;

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
        String issuer = (config.getIssuer() == null || config.getIssuer().isBlank()) ? "DaxPay" : config.getIssuer();
        // otpauth://totp/Issuer:account?secret=...&issuer=...&algorithm=SHA1&digits=6&period=30
        String label = URLUtil.encodeAll(issuer) + ":" + URLUtil.encodeAll(account == null ? "" : account);
        return String.format(
                "otpauth://totp/%s?secret=%s&issuer=%s&algorithm=SHA1&digits=%d&period=%d",
                label, secret, URLUtil.encodeAll(issuer), DIGITS, PERIOD);
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
            byte[] key = Base32.decode(secret);
            // 当前时间桶(Unix 秒 / 步长)
            long currentBucket = System.currentTimeMillis() / 1000L / PERIOD;
            // 容忍时钟漂移: 前后各 DISCREPANCY 个时间桶
            for (long offset = -DISCREPANCY; offset <= DISCREPANCY; offset++) {
                String expected = generateCode(key, currentBucket + offset);
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
    private String generateCode(byte[] key, long timeBucket) {
        byte[] timeBytes = longToBytes(timeBucket);
        HMac hmac = new HMac(ALGORITHM, key);
        byte[] hash = hmac.digest(timeBytes);
        // 动态截取: 取最后一个字节低 4 位作为偏移
        int offset = hash[hash.length - 1] & 0xf;
        int binary = ((hash[offset] & 0x7f) << 24)
                | ((hash[offset + 1] & 0xff) << 16)
                | ((hash[offset + 2] & 0xff) << 8)
                | (hash[offset + 3] & 0xff);
        int mod = (int) Math.pow(10, DIGITS);
        int code = binary % mod;
        // 前导补零到指定位数
        return String.format("%0" + DIGITS + "d", code);
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

    private PlatformTwoFactorAuthConfig getConfig() {
        return iamSecurityConfigService.getTwoFactorAuthConfig();
    }
}
