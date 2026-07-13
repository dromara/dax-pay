package cn.daxpay.open.platform.iam.exception.auth;

import cn.daxpay.open.platform.capability.auth.authentication.SecondaryAuthRequiredException;
import lombok.Getter;

/// # 需要双因素二次验证异常
///
/// 密码校验通过但用户已启用 TOTP 时抛出, 携带临时凭证 preAuthToken。
/// 不算登录失败, 不触发失败计数与失败日志, 由全局处理器返回二次验证数据给前端。
///
@Getter
public class TwoFactorRequiredException extends SecondaryAuthRequiredException {

    /// 需二次验证的响应码(前端据此切到二次验证界面)
    public static final int CODE = 40101;

    private final String preAuthToken;

    /// @param userId       用户ID
    /// @param account      账号
    /// @param preAuthToken 临时凭证
    public TwoFactorRequiredException(Long userId, String account, String preAuthToken) {
        // 使用 i18n messageKey
        super(userId, account, "error.auth.twoFactorRequired");
        this.preAuthToken = preAuthToken;
    }
}
