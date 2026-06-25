package cn.daxpay.open.platform.iam.auth.service.twofactor;

import cn.hutool.core.lang.UUID;
import cn.hutool.json.JSONUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;

/// # 双因素认证预认证令牌服务
///
/// 密码校验通过但用户已启用 2FA 时, 颁发一次性预认证令牌(preAuthToken)存入 Redis,
/// 携带登录上下文(userId/clientCode/loginType)。二次验证通过后凭此令牌恢复登录流程。
/// 令牌单次有效, 5 分钟过期, 与 [cn.daxpay.open.platform.iam.auth.service.CaptchaService] 同模式。
///
@Service
@RequiredArgsConstructor
public class TwoFactorPreAuthService {

    private static final String PREFIX = "2fa:preauth:";

    private static final Duration EXPIRE = Duration.ofMinutes(5);

    private final StringRedisTemplate stringRedisTemplate;

    /// 创建预认证令牌并缓存登录上下文
    public String create(Long userId, String clientCode, String loginType) {
        String token = UUID.fastUUID().toString(true);
        PreAuthContext context = new PreAuthContext(userId, clientCode, loginType);
        stringRedisTemplate.opsForValue().set(PREFIX + token, JSONUtil.toJsonStr(context), EXPIRE);
        return token;
    }

    /// 消费预认证令牌(取出并立即删除, 单次有效), 不存在或已过期返回 null
    public PreAuthContext consume(String token) {
        if (token == null || token.isBlank()) {
            return null;
        }
        String key = PREFIX + token;
        String json = stringRedisTemplate.opsForValue().get(key);
        if (json == null) {
            return null;
        }
        // 立即删除, 保证单次有效
        stringRedisTemplate.delete(key);
        return JSONUtil.toBean(json, PreAuthContext.class);
    }

    /// 预认证上下文
    public record PreAuthContext(Long userId, String clientCode, String loginType) {
    }
}
