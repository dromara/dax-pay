package cn.daxpay.open.platform.iam.auth.service.email;

import cn.daxpay.open.platform.core.exception.BizInfoException;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.json.JSONUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;

/// # 邮箱验证码上下文服务
///
/// 验证码及其流程上下文的 Redis 生命周期管理, 与
/// [cn.daxpay.open.platform.iam.auth.service.passkey.PasskeyChallengeService] 同模式:
/// 生成存入(5 分钟过期), 校验成功后由调用方消费(单次使用),
/// 校验失败递增错误计数并回写, 连续错 [MAX_FAIL_COUNT] 次作废(防穷举);
/// 校验([#verify])与消费([#consume])拆分为两步, 供"验证码校验在前、
/// 业务校验在后"的流程在业务校验(如密码策略)全部通过后再消费,
/// 避免业务校验不合格连带烧码
@Service
@RequiredArgsConstructor
public class EmailCodeService {

    /// 绑定流程 Redis 前缀(键为用户ID)
    public static final String BIND_SCOPE = "iam:email:bind:";

    /// 找回密码流程 Redis 前缀(键为流程ID)
    public static final String RESET_SCOPE = "iam:email:reset:";

    /// 解绑邮箱流程 Redis 前缀(键为用户ID)
    public static final String UNBIND_SCOPE = "iam:email:unbind:";

    private static final Duration EXPIRE = Duration.ofMinutes(5);

    /// 验证码最大错误次数, 达到后作废
    private static final int MAX_FAIL_COUNT = 5;

    private final StringRedisTemplate stringRedisTemplate;

    /// 验证码流程上下文(code 验证码 / failCount 已错误次数 / userId 关联用户 / email 目标邮箱)
    public record EmailCodeContext(String code, int failCount, Long userId, String email) {
    }

    /// 保存上下文(5 分钟过期)
    public void save(String scope, String key, EmailCodeContext context) {
        stringRedisTemplate.opsForValue().set(scope + key, JSONUtil.toJsonStr(context), EXPIRE);
    }

    /// 查询上下文(不消费)
    public EmailCodeContext get(String scope, String key) {
        String json = stringRedisTemplate.opsForValue().get(scope + key);
        return json == null ? null : JSONUtil.toBean(json, EmailCodeContext.class);
    }

    /// 删除上下文
    public void delete(String scope, String key) {
        stringRedisTemplate.delete(scope + key);
    }

    /// 校验验证码(不消费), 通过返回上下文
    ///
    /// 上下文不存在视为已过期; 校验失败递增计数回写(重新计时5分钟),
    /// 错误达到上限后作废需重新获取; 校验通过不删除上下文,
    /// 由调用方在后续业务校验全部通过后调用 [#consume] 消费
    public EmailCodeContext verify(String scope, String key, String inputCode) {
        EmailCodeContext context = this.get(scope, key);
        if (context == null) {
            // 邮箱: 验证码已过期或不存在
            throw new BizInfoException("error.iam.email.codeExpired");
        }
        if (context.code().equals(inputCode)) {
            return context;
        }
        this.delete(scope, key);
        int failCount = context.failCount() + 1;
        if (failCount >= MAX_FAIL_COUNT) {
            // 邮箱: 验证码错误次数过多已作废
            throw new BizInfoException("error.iam.email.codeMaxFail");
        }
        this.save(scope, key, new EmailCodeContext(context.code(), failCount, context.userId(), context.email()));
        // 邮箱: 验证码错误
        throw new BizInfoException("error.iam.email.codeError");
    }

    /// 消费验证码(删除上下文, 之后不可再次使用)
    public void consume(String scope, String key) {
        this.delete(scope, key);
    }

    /// 校验并消费验证码, 成功返回上下文
    ///
    /// 适用于校验通过即生效、无后续业务校验的场景;
    /// 校验与消费之间还有业务校验(如密码策略/唯一性)时,
    /// 应改用 [#verify] + [#consume] 组合, 业务校验不合格时不烧码
    public EmailCodeContext verifyAndConsume(String scope, String key, String inputCode) {
        EmailCodeContext context = this.verify(scope, key, inputCode);
        this.consume(scope, key);
        return context;
    }

    /// 生成6位数字验证码
    public String generateCode() {
        return RandomUtil.randomNumbers(6);
    }
}
