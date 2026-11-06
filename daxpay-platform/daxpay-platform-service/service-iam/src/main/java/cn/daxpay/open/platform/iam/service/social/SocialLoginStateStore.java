package cn.daxpay.open.platform.iam.service.social;

import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.Objects;

/// # 标准 OAuth state 存储
///
/// 授权地址生成时把 state 对应的业务上下文(身份域/平台/场景/绑定用户)一次性落 Redis,
/// 兑换时读取即删除并校验上下文一致, 使随机 UUID state 具备防伪造/防重放/防跨场景串用的能力
/// (如登录 state 被拿来走绑定、运营端 state 被拿来走商户端).
/// 与扫码族的票据映射([cn.daxpay.open.platform.iam.service.social.scan.SocialLoginScanStore]/
/// SocialBindScanStore)键空间独立, 互不可串用.
@Service
@RequiredArgsConstructor
public class SocialLoginStateStore {

    /// state 上下文前缀(键为 OAuth2 state)
    public static final String STATE_PREFIX = "iam:social:oauth:state:";

    /// state 上下文过期时间(覆盖用户跳转第三方并完成授权的最短窗口)
    private static final Duration EXPIRE = Duration.ofMinutes(5);

    private final StringRedisTemplate stringRedisTemplate;

    /// state 上下文(clientCode 身份域 / source 平台 / mode 场景 / userId 绑定场景发起人)
    @Data
    @Accessors(chain = true)
    public static class StateContext {

        private String clientCode;

        private String source;

        private String mode;

        private Long userId;
    }

    /// 保存 state 上下文(5 分钟过期)
    public void save(String state, String clientCode, String source, String mode, Long userId) {
        StateContext context = new StateContext()
            .setClientCode(clientCode)
            .setSource(source)
            .setMode(mode)
            .setUserId(userId);
        stringRedisTemplate.opsForValue().set(STATE_PREFIX + state, JSONUtil.toJsonStr(context), EXPIRE);
    }

    /// 消费 state 上下文(读取即删除, 一次性使用防重放; 不存在或过期返回 null)
    public StateContext consume(String state) {
        if (StrUtil.isBlank(state)) {
            return null;
        }
        String json = stringRedisTemplate.opsForValue().getAndDelete(STATE_PREFIX + state);
        return Objects.isNull(json) ? null : JSONUtil.toBean(json, StateContext.class);
    }
}
