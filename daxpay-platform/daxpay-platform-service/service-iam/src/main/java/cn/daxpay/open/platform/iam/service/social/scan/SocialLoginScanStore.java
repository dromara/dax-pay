package cn.daxpay.open.platform.iam.service.social.scan;

import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.Objects;

/// # 公众号扫码登录票据存储
///
/// PC 扫码跨设备登录的票据生命周期管理, 与 [SocialBindScanStore] 同模式但独立键空间
/// (两类票据互不可串用): PC 端匿名发起(无登录态), ticket 仅持身份域与平台上下文;
/// 手机端授权兑换后按绑定关系回填 userId, PC 轮询到 success 时凭票据一次性换取登录令牌.
/// state 为 OAuth2 回调换取 ticket 的一次性映射(读取即删除, 防重放).
/// 票据与映射均为 UUID 随机值, 全流程 5 分钟过期.
@Service
@RequiredArgsConstructor
public class SocialLoginScanStore {

    /// 票据上下文前缀(键为 ticket)
    public static final String TICKET_PREFIX = "iam:social:login:scan:";

    /// state 到 ticket 的一次性映射前缀(键为 OAuth2 state)
    public static final String STATE_PREFIX = "iam:social:login:scan:state:";

    /// 票据/映射过期时间(覆盖 PC 生成二维码到手机完成授权的全流程)
    private static final Duration EXPIRE = Duration.ofMinutes(5);

    /// 等待扫码授权
    public static final String STATUS_WAITING = "waiting";

    /// 已扫码(手机端打开 H5 发起页, 尚未确认授权), PC 端凭此展示"扫码成功待操作"中间态
    public static final String STATUS_SCANNED = "scanned";

    /// 授权完成且账号已绑定(可签发登录态)
    public static final String STATUS_SUCCESS = "success";

    /// 授权完成但微信未绑定账号(不自动注册, 引导先用账号密码登录后绑定)
    public static final String STATUS_NOT_BOUND = "not_bound";

    /// 手机端用户在确认页点击取消, 拒绝为 PC 端签发登录态
    public static final String STATUS_CANCELLED = "cancelled";

    private final StringRedisTemplate stringRedisTemplate;

    /// 票据上下文(userId 授权后回填 / clientCode 身份域 / source 平台 / status 进度)
    @Data
    @Accessors(chain = true)
    public static class LoginScanContext {

        private Long userId;

        private String clientCode;

        private String source;

        private String status;
    }

    /// 新建 waiting 状态票据(5 分钟过期, PC 匿名发起暂无用户身份)
    public void saveWaiting(String ticket, String clientCode, String source) {
        LoginScanContext context = new LoginScanContext()
            .setClientCode(clientCode)
            .setSource(source)
            .setStatus(STATUS_WAITING);
        stringRedisTemplate.opsForValue().set(TICKET_PREFIX + ticket, JSONUtil.toJsonStr(context), EXPIRE);
    }

    /// 查询票据上下文(不存在或过期返回 null)
    public LoginScanContext get(String ticket) {
        if (StrUtil.isBlank(ticket)) {
            return null;
        }
        String json = stringRedisTemplate.opsForValue().get(TICKET_PREFIX + ticket);
        return Objects.isNull(json) ? null : JSONUtil.toBean(json, LoginScanContext.class);
    }

    /// 消费票据(读取即删除): success 状态的登录令牌签发只允许一次, 并发轮询仅一方拿到
    public LoginScanContext consume(String ticket) {
        if (StrUtil.isBlank(ticket)) {
            return null;
        }
        String json = stringRedisTemplate.opsForValue().getAndDelete(TICKET_PREFIX + ticket);
        return Objects.isNull(json) ? null : JSONUtil.toBean(json, LoginScanContext.class);
    }

    /// 标记已扫码(手机端打开 H5 发起页, 仅 waiting 状态迁入, 终态不回退;
    /// TTL 重置 5 分钟, 用户已扫码后不应在确认操作中途过期)
    public void markScanned(String ticket, LoginScanContext context) {
        context.setStatus(STATUS_SCANNED);
        stringRedisTemplate.opsForValue().set(TICKET_PREFIX + ticket, JSONUtil.toJsonStr(context), EXPIRE);
    }

    /// 标记未绑定(不自动注册新账号, 手机端引导回 PC 用账号密码登录后在个人中心绑定)
    public void markNotBound(String ticket, LoginScanContext context) {
        context.setStatus(STATUS_NOT_BOUND);
        stringRedisTemplate.opsForValue().set(TICKET_PREFIX + ticket, JSONUtil.toJsonStr(context), EXPIRE);
    }

    /// 标记登录就绪(回填 userId, TTL 重置 5 分钟, 保留 PC 轮询读取窗口)
    public void markSuccess(String ticket, LoginScanContext context) {
        context.setStatus(STATUS_SUCCESS);
        stringRedisTemplate.opsForValue().set(TICKET_PREFIX + ticket, JSONUtil.toJsonStr(context), EXPIRE);
    }

    /// 标记已取消(手机端确认页拒绝本次登录, PC 轮询到该状态后停止轮询)
    public void markCancelled(String ticket, LoginScanContext context) {
        context.setStatus(STATUS_CANCELLED);
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
