package cn.daxpay.open.platform.iam.auth.service.twofactor;

import cn.hutool.core.lang.UUID;
import cn.hutool.json.JSONUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;

/// # 二次验证临时凭证服务
///
/// 密码通过但用户已启用 2FA 时, 下发一次性临时凭证(preAuthToken)到 Redis,
/// 并带上登录上下文(userId/clientCode/loginType)。二次验证通过后凭此凭证完成登录。
/// 单次有效, 5 分钟过期, 与 [cn.daxpay.open.platform.iam.auth.service.CaptchaService] 同模式。
///
@Service
@RequiredArgsConstructor
public class TwoFactorPreAuthService {

    private static final String PREFIX = "2fa:preauth:";

    private static final Duration EXPIRE = Duration.ofMinutes(5);

    private final StringRedisTemplate stringRedisTemplate;

    /// 创建临时凭证并缓存登录上下文
    public String create(Long userId, String clientCode, String loginType) {
        String token = UUID.fastUUID().toString(true);
        PreAuthContext context = new PreAuthContext(userId, clientCode, loginType);
        stringRedisTemplate.opsForValue().set(PREFIX + token, JSONUtil.toJsonStr(context), EXPIRE);
        return token;
    }

    /// 读取临时凭证上下文(不删除, 供二次验证重试), 不存在或已过期返回 null
    public PreAuthContext get(String token) {
        if (token == null || token.isBlank()) {
            return null;
        }
        String json = stringRedisTemplate.opsForValue().get(PREFIX + token);
        if (json == null) {
            return null;
        }
        return JSONUtil.toBean(json, PreAuthContext.class);
    }

    /// 删除临时凭证(二次验证通过后调用, 保证单次有效)
    public void delete(String token) {
        if (token != null && !token.isBlank()) {
            stringRedisTemplate.delete(PREFIX + token);
        }
    }

    /// 预认证上下文
    public record PreAuthContext(Long userId, String clientCode, String loginType) {
    }
}
