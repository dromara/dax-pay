package cn.daxpay.open.platform.iam.exception.auth;

import cn.daxpay.open.platform.capability.auth.authentication.AuthenticationChallengeException;
import lombok.Getter;

/// # 需要双因素认证异常
///
/// 密码校验通过但用户已启用 TOTP 双因素认证时抛出, 携带一次性预认证令牌。
/// 该异常不属于登录失败, 不应触发失败计数与失败日志, 由全局处理器返回挑战结果给前端。
///
@Getter
public class TwoFactorRequiredException extends AuthenticationChallengeException {

    /// 需要双因素认证的响应码(前端据此识别并切换到二次验证界面)
    public static final int CODE = 40101;

    private final String preAuthToken;

    /// @param userId       用户ID
    /// @param account      账号
    /// @param preAuthToken 一次性预认证令牌
    public TwoFactorRequiredException(Long userId, String account, String preAuthToken) {
        // 使用 i18n messageKey
        super(userId, account, "error.auth.twoFactorRequired");
        this.preAuthToken = preAuthToken;
    }
}
