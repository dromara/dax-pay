package cn.daxpay.open.platform.iam.auth.service.twofactor;

import cn.daxpay.open.platform.capability.auth.authentication.AuthenticationChallengeException;
import cn.daxpay.open.platform.capability.auth.authentication.PostAuthenticationChallenge;
import cn.daxpay.open.platform.capability.auth.entity.AuthInfoResult;
import cn.daxpay.open.platform.capability.auth.entity.LoginAuthContext;
import cn.daxpay.open.platform.iam.exception.auth.TwoFactorRequiredException;
import cn.daxpay.open.platform.iam.service.twofactor.UserTwoFactorService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/// # 双因素认证挑战
///
/// 认证通过后, 若用户已启用 TOTP 双因素认证, 颁发一次性预认证令牌并返回挑战异常。
/// 令牌由 [TwoFactorPreAuthService] 存入 Redis, 5 分钟过期, 二次验证通过后单次消费。
///
@Slf4j
@Component
@RequiredArgsConstructor
public class TwoFactorAuthenticationChallenge implements PostAuthenticationChallenge {

    private final UserTwoFactorService userTwoFactorService;

    private final TwoFactorPreAuthService twoFactorPreAuthService;

    @Override
    public boolean required(LoginAuthContext context, AuthInfoResult authInfoResult) {
        Long userId = toLong(authInfoResult.getId());
        // 平台已开启且用户已绑定 2FA 时需要挑战
        return userId != null && userTwoFactorService.isTwoFactorRequired(userId);
    }

    @Override
    public AuthenticationChallengeException createChallenge(LoginAuthContext context, AuthInfoResult authInfoResult) {
        Long userId = toLong(authInfoResult.getId());
        String account = authInfoResult.getUserDetail() == null ? null : authInfoResult.getUserDetail().getAccount();
        String preAuthToken = twoFactorPreAuthService.create(userId, context.getClientCode(), context.getAuthLoginType());
        return new TwoFactorRequiredException(userId, account, preAuthToken);
    }

    /// 认证结果 id(Object) 转 Long, 无法转换返回 null
    private Long toLong(Object id) {
        if (id == null) {
            return null;
        }
        if (id instanceof Long l) {
            return l;
        }
        if (id instanceof Number n) {
            return n.longValue();
        }
        try {
            return Long.valueOf(id.toString());
        }
        catch (NumberFormatException e) {
            return null;
        }
    }
}
