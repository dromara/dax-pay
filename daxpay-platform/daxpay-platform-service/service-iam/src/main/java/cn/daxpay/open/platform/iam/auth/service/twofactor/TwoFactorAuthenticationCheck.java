package cn.daxpay.open.platform.iam.auth.service.twofactor;

import cn.daxpay.open.platform.capability.auth.authentication.PostAuthenticationCheck;
import cn.daxpay.open.platform.capability.auth.code.AuthLoginTypeCode;
import cn.daxpay.open.platform.capability.auth.authentication.SecondaryAuthRequiredException;
import cn.daxpay.open.platform.capability.auth.entity.AuthInfoResult;
import cn.daxpay.open.platform.capability.auth.entity.LoginAuthContext;
import cn.daxpay.open.platform.iam.exception.auth.TwoFactorRequiredException;
import cn.daxpay.open.platform.iam.service.twofactor.UserTwoFactorService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/// # 双因素二次验证检查
///
/// 认证通过后, 若用户已启用 TOTP, 下发临时凭证并要求二次验证。
/// 凭证由 [TwoFactorPreAuthService] 存 Redis, 5 分钟过期, 验证通过后单次消费。
///
@Slf4j
@Component
@RequiredArgsConstructor
public class TwoFactorAuthenticationCheck implements PostAuthenticationCheck {

    private final UserTwoFactorService userTwoFactorService;

    private final TwoFactorPreAuthService twoFactorPreAuthService;

    @Override
    public boolean required(LoginAuthContext context, AuthInfoResult authInfoResult) {
        // 通行密钥登录豁免 TOTP 两步验证: passkey 本身即「凭据持有 + 用户验证(生物识别/PIN)」的强认证,
        // 不再叠加动态码(业界通行做法, 2026-08-24 拍板)
        if (AuthLoginTypeCode.PASSKEY.equals(context.getAuthLoginType())) {
            return false;
        }
        Long userId = toLong(authInfoResult.getId());
        // 平台开启且用户已绑定 2FA 时, 登录前需二次验证
        return userId != null && userTwoFactorService.isTwoFactorRequired(userId);
    }

    @Override
    public SecondaryAuthRequiredException createException(LoginAuthContext context, AuthInfoResult authInfoResult) {
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
