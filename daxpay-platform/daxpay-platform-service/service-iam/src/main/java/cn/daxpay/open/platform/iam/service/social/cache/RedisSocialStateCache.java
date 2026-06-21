package cn.daxpay.open.platform.iam.service.social.cache;

import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

/// # 社交登录 state 缓存(Redis 实现)
///
/// 以 state 为键缓存授权上下文, 用于回调时校验 state 合法性(防 CSRF)并恢复授权场景.
/// state 超时时间由调用方(render 阶段)按平台配置传入, 不再依赖全局 yml 配置.
///
@Component
@RequiredArgsConstructor
public class RedisSocialStateCache {

    private static final String KEY_PREFIX = "social:state:";

    private final StringRedisTemplate stringRedisTemplate;

    /// 缓存授权上下文
    /// @param state 授权 state
    /// @param context 授权上下文(含 stateTimeout 等)
    /// @param stateTimeout state 缓存超时时间(秒), 来自平台配置
    public void cache(String state, SocialAuthContext context, long stateTimeout) {
        String json = JSONUtil.toJsonStr(context);
        stringRedisTemplate.opsForValue().set(
            KEY_PREFIX + state,
            json,
            stateTimeout,
            TimeUnit.SECONDS
        );
    }

    /// 取出并删除上下文(回调校验用, 一次性)
    public SocialAuthContext getAndRemove(String state) {
        String key = KEY_PREFIX + state;
        String json = stringRedisTemplate.opsForValue().get(key);
        if (StrUtil.isBlank(json)) {
            return null;
        }
        // 取出后立即删除, 防止重放
        stringRedisTemplate.delete(key);
        return JSONUtil.toBean(json, SocialAuthContext.class);
    }
}
