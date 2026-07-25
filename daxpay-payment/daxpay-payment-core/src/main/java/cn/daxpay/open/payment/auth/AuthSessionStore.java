package cn.daxpay.open.payment.auth;

import cn.daxpay.open.payment.unipay.result.assist.AuthResult;
import cn.daxpay.open.platform.common.json.util.JacksonUtil;
import cn.daxpay.open.platform.core.enums.unipay.ChannelAuthStatusEnum;
import cn.hutool.core.util.StrUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.Objects;

/// # 认证会话与结果缓存
///
/// 统一管理通道认证/平台级认证共用的会话上下文(authToken)与轮询结果(queryCode)的 Redis 读写,
/// 与具体认证来源(通道商户策略 / 平台级配置)解耦, 供 [ChannelAuthService]、[ChannelProductAuthService]、
/// [PlatformAuthService] 及各端调试入口(admin DevelopAuthAdminService / merchant MchDevelopAuthService)复用。
/// 授权成功后由 Facade 调用 [#deleteSession] 使 authToken 一次使用失效。
@Slf4j
@Service
@RequiredArgsConstructor
public class AuthSessionStore {

    /// 付款码/道通/调试场景的认证结果查询缓存前缀
    public static final String CHANNEL_AUTH_KEY_PREFIX = "payment:channel-auth:";

    /// H5授权重定向场景的认证会话上下文缓存前缀(与 queryCode 机制解耦)
    public static final String AUTH_SESSION_KEY_PREFIX = "payment:auth-session:";

    /// 会话码/查询码过期时间
    public static final Duration AUTH_TIMEOUT = Duration.ofMinutes(5);

    private final RedisTemplate<String, Object> redisTemplate;

    /// 保存认证会话上下文(以 authToken 为 key)
    public void saveSession(String authToken, AuthSession session) {
        redisTemplate.opsForValue().set(
                AUTH_SESSION_KEY_PREFIX + authToken,
                session,
                AUTH_TIMEOUT);
    }

    /// 根据 authToken 加载会话上下文, 不存在或过期返回 null
    public AuthSession loadSession(String authToken) {
        if (StrUtil.isBlank(authToken)) {
            return null;
        }
        var cached = redisTemplate.opsForValue().get(AUTH_SESSION_KEY_PREFIX + authToken);
        if (Objects.isNull(cached)) {
            return null;
        }
        return JacksonUtil.convert(cached, AuthSession.class);
    }

    /// 删除认证会话(授权成功后一次使用失效, 防止 TTL 内重复消费)
    public void deleteSession(String authToken) {
        if (StrUtil.isBlank(authToken)) {
            return;
        }
        redisTemplate.delete(AUTH_SESSION_KEY_PREFIX + authToken);
    }

    /// 写入 WAITING 状态的查询结果
    public void saveWaitingResult(String queryCode) {
        AuthResult authResult = new AuthResult().setStatus(ChannelAuthStatusEnum.WAITING.getCode());
        redisTemplate.opsForValue().set(
                CHANNEL_AUTH_KEY_PREFIX + queryCode,
                authResult,
                AUTH_TIMEOUT);
    }

    /// 通过查询码获取认证结果(付款码/道通/调试场景)
    public AuthResult queryAuthResult(String queryCode) {
        var authResult = redisTemplate.opsForValue().get(CHANNEL_AUTH_KEY_PREFIX + queryCode);
        if (Objects.isNull(authResult)) {
            return new AuthResult().setStatus(ChannelAuthStatusEnum.NOT_EXIST.getCode());
        }
        return JacksonUtil.convert(authResult, AuthResult.class);
    }

    /// 写回轮询结果: 优先 paramQueryCode, 否则从会话恢复(微信等 OAuth 重定向通道)
    public void writeResultByQueryCode(String paramQueryCode, AuthSession session, AuthResult authResult) {
        String queryCode = StrUtil.blankToDefault(paramQueryCode,
                session != null ? session.getQueryCode() : null);
        if (StrUtil.isNotBlank(queryCode)) {
            redisTemplate.opsForValue().set(
                    CHANNEL_AUTH_KEY_PREFIX + queryCode,
                    authResult,
                    AUTH_TIMEOUT);
        }
    }
}
