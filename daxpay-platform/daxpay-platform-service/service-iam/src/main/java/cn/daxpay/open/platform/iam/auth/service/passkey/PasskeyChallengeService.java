package cn.daxpay.open.platform.iam.auth.service.passkey;

import cn.hutool.core.lang.UUID;
import cn.hutool.json.JSONUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;

/// # 通行密钥会话上下文服务
///
/// 注册/登录两阶段交互的 challenge(防重放一次性随机数)生命周期管理:
/// 下发选项时生成并存入 Redis(5 分钟过期), 验证时单次消费(getAndDelete)。
/// 注册上下文绑定用户, 登录上下文绑定终端, 与
/// [cn.daxpay.open.platform.iam.auth.service.twofactor.TwoFactorPreAuthService] 同模式。
///
@Service
@RequiredArgsConstructor
public class PasskeyChallengeService {

    private static final String REGISTER_PREFIX = "passkey:reg:";

    private static final String AUTH_PREFIX = "passkey:auth:";

    private static final Duration EXPIRE = Duration.ofMinutes(5);

    private final StringRedisTemplate stringRedisTemplate;

    /// 保存注册会话上下文, 返回会话ID
    public String saveRegister(RegisterContext context) {
        String id = UUID.fastUUID().toString(true);
        stringRedisTemplate.opsForValue().set(REGISTER_PREFIX + id, JSONUtil.toJsonStr(context), EXPIRE);
        return id;
    }

    /// 消费注册会话上下文(单次有效), 不存在或已过期返回 null
    public RegisterContext consumeRegister(String challengeId) {
        String json = stringRedisTemplate.opsForValue().getAndDelete(REGISTER_PREFIX + challengeId);
        return json == null ? null : JSONUtil.toBean(json, RegisterContext.class);
    }

    /// 保存登录会话上下文, 返回会话ID
    public String saveAuth(AuthContext context) {
        String id = UUID.fastUUID().toString(true);
        stringRedisTemplate.opsForValue().set(AUTH_PREFIX + id, JSONUtil.toJsonStr(context), EXPIRE);
        return id;
    }

    /// 消费登录会话上下文(单次有效), 不存在或已过期返回 null
    public AuthContext consumeAuth(String challengeId) {
        String json = stringRedisTemplate.opsForValue().getAndDelete(AUTH_PREFIX + challengeId);
        return json == null ? null : JSONUtil.toBean(json, AuthContext.class);
    }

    /// 注册会话上下文(challenge 随机值 + 发起注册的用户信息)
    public record RegisterContext(String challenge, Long userId, String clientCode, String account, String name) {
    }

    /// 登录会话上下文(challenge 随机值 + 发起登录的终端)
    public record AuthContext(String challenge, String clientCode) {
    }
}
