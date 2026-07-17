package cn.daxpay.open.payment.unipay.aop;

import cn.daxpay.open.platform.core.exception.NonceInvalidException;
import cn.daxpay.open.platform.core.exception.NonceMissingException;
import cn.daxpay.open.platform.core.exception.TimestampExpiredException;
import cn.daxpay.open.platform.iam.auth.service.IamSecurityConfigService;
import cn.daxpay.open.platform.system.entity.config.platform.security.PlatformApiSecurityConfig;
import cn.daxpay.open.payment.unipay.param.PaymentCommonParam;
import cn.hutool.core.util.StrUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.OffsetDateTime;

/// # 开放支付防重放校验服务
///
/// 依据平台 API 安全配置（[PlatformApiSecurityConfig]）对开放支付接口请求做防重放校验:
/// - **Nonce 一次性消费**: 商户在签名时自行生成 `nonceStr`，平台使用 Redis SETNX 语义保证同一
///   nonce 在 TTL 内仅能被消费一次，第二次到达即判定为重放并拒绝。
/// - **请求时间窗口校验**: `reqTime` 与服务器当前时间的差值（绝对值）超过配置阈值则拒绝，
///   双向校验既防过期请求也防未来时间请求。
///
/// 与平台登录用的 [cn.daxpay.open.platform.capability.nonce.service.NonceService] 不同:
/// 登录是「平台签发 nonce → 消费时删除」的预存模式；支付是「商户自生成 nonce → SETNX 判首次」
/// 的去重模式，两者语义不同故独立实现。
///
/// 校验顺序: Nonce 缺失检查 → Nonce 一次性消费 → 请求时间窗口。
@Slf4j
@Service
@RequiredArgsConstructor
public class PaymentReplayProtectService {

    private final IamSecurityConfigService iamSecurityConfigService;
    private final StringRedisTemplate stringRedisTemplate;

    /// Redis key 前缀，带 `pay` 命名空间与登录 nonce 隔离
    private static final String PAY_NONCE_PREFIX = "nonce:pay:";

    /// 执行防重放校验（配置全关时直接放行）
    ///
    /// @param param 支付公共参数（含 nonceStr / reqTime）
    /// @param mchNo 商户号（用于 nonce key 隔离，防跨商户碰撞）
    public void verify(PaymentCommonParam param, String mchNo) {
        PlatformApiSecurityConfig config = iamSecurityConfigService.getApiSecurityConfig();
        if (config == null) {
            return;
        }
        // Nonce 防重放
        if (Boolean.TRUE.equals(config.getNonceVerifyEnabled())) {
            verifyNonce(param.getNonceStr(), mchNo, config.getNonceTtlSeconds());
        }
        // 请求时间窗口校验
        if (Boolean.TRUE.equals(config.getReqTimeoutEnabled())) {
            verifyReqTime(param.getReqTime(), config.getReqTimeoutSeconds());
        }
    }

    /// Nonce 一次性消费校验（SETNX 语义）
    private void verifyNonce(String nonceStr, String mchNo, Integer ttlSeconds) {
        if (StrUtil.isBlank(nonceStr)) {
            // Nonce 缺失
            throw new NonceMissingException();
        }
        int ttl = ttlSeconds == null || ttlSeconds < 1 ? 300 : ttlSeconds;
        // key 带商户号隔离，避免不同商户使用相同 nonceStr 时误判
        String key = PAY_NONCE_PREFIX + mchNo + ":" + nonceStr;
        Boolean firstOccupy = stringRedisTemplate.opsForValue()
                .setIfAbsent(key, "1", Duration.ofSeconds(ttl));
        if (!Boolean.TRUE.equals(firstOccupy)) {
            // 已存在 = 重放请求
            log.warn("支付接口Nonce重放拦截, mchNo: {}, nonce: {}", mchNo, nonceStr);
            throw new NonceInvalidException();
        }
    }

    /// 请求时间窗口校验（双向绝对值）
    private void verifyReqTime(OffsetDateTime reqTime, Integer timeoutSeconds) {
        if (reqTime == null) {
            // reqTime 由 @NotNull 已校验，兜底
            throw new TimestampExpiredException();
        }
        int tolerance = timeoutSeconds == null || timeoutSeconds < 1 ? 300 : timeoutSeconds;
        OffsetDateTime now = OffsetDateTime.now();
        long diffSeconds = Math.abs(Duration.between(reqTime, now).getSeconds());
        if (diffSeconds > tolerance) {
            log.warn("支付接口请求时间超出窗口, reqTime: {}, 偏差: {}s, 允许: {}s", reqTime, diffSeconds, tolerance);
            throw new TimestampExpiredException();
        }
    }
}
