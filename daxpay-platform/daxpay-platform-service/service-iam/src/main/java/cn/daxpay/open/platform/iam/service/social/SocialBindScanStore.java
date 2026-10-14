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

/// # 公众号扫码绑定票据存储
///
/// PC 扫码跨设备绑定的票据生命周期管理, 与
/// [cn.daxpay.open.platform.iam.auth.service.email.EmailCodeService] 同模式:
/// ticket 持有绑定上下文(待绑定用户/身份域/平台), 供 PC 轮询绑定结果;
/// state 为 OAuth2 回调换取 ticket 的一次性映射(读取即删除, 防重放).
/// 票据与映射均为 UUID 随机值, 全流程 5 分钟过期.
@Service
@RequiredArgsConstructor
public class SocialBindScanStore {

    /// 票据上下文前缀(键为 ticket)
    public static final String TICKET_PREFIX = "iam:social:bind:scan:";

    /// state 到 ticket 的一次性映射前缀(键为 OAuth2 state)
    public static final String STATE_PREFIX = "iam:social:bind:scan:state:";

    /// 票据/映射过期时间(覆盖 PC 生成二维码到手机完成授权的全流程)
    private static final Duration EXPIRE = Duration.ofMinutes(5);

    /// 等待扫码授权
    public static final String STATUS_WAITING = "waiting";

    /// 授权并绑定完成
    public static final String STATUS_SUCCESS = "success";

    private final StringRedisTemplate stringRedisTemplate;

    /// 票据上下文(userId 待绑定用户 / clientCode 身份域 / source 平台 / status 绑定状态)
    @Data
    @Accessors(chain = true)
    public static class BindScanContext {

        private Long userId;

        private String clientCode;

        private String source;

        private String status;
    }

    /// 新建 waiting 状态票据(5 分钟过期)
    public void saveWaiting(String ticket, Long userId, String clientCode, String source) {
        BindScanContext context = new BindScanContext()
            .setUserId(userId)
            .setClientCode(clientCode)
            .setSource(source)
            .setStatus(STATUS_WAITING);
        stringRedisTemplate.opsForValue().set(TICKET_PREFIX + ticket, JSONUtil.toJsonStr(context), EXPIRE);
    }

    /// 查询票据上下文(不存在或过期返回 null)
    public BindScanContext get(String ticket) {
        if (StrUtil.isBlank(ticket)) {
            return null;
        }
        String json = stringRedisTemplate.opsForValue().get(TICKET_PREFIX + ticket);
        return Objects.isNull(json) ? null : JSONUtil.toBean(json, BindScanContext.class);
    }

    /// 标记绑定成功(TTL 重置 5 分钟, 保留 PC 轮询读取窗口)
    public void markSuccess(String ticket, BindScanContext context) {
        context.setStatus(STATUS_SUCCESS);
        stringRedisTemplate.opsForValue().set(TICKET_PREFIX + ticket, JSONUtil.toJsonStr(context), EXPIRE);
    }

    /// 保存 state 到 ticket 的一次性映射(5 分钟过期)
    public void saveState(String state, String ticket) {
        stringRedisTemplate.opsForValue().set(STATE_PREFIX + state, ticket, EXPIRE);
    }

    /// 消费 state 映射换取 ticket(读取即删除, 一次性使用防重放)
    public String consumeTicketByState(String state) {
        if (StrUtil.isBlank(state)) {
            return null;
        }
        return stringRedisTemplate.opsForValue().getAndDelete(STATE_PREFIX + state);
    }
}
