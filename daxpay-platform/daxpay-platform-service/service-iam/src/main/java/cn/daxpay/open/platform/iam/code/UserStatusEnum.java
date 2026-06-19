package cn.daxpay.open.platform.iam.code;

import cn.daxpay.open.platform.core.i18n.I18nSupport;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/// # 用户状态
///
/// 字典 user_status
@Getter
@RequiredArgsConstructor
public enum UserStatusEnum implements I18nSupport {

    /// 正常
    NORMAL("normal"),

    /// 锁定 多次登录失败
    LOCK("lock"),

    /// 封禁
    BAN("ban");

    private final String code;

    @Override
    public String getI18nPrefix() {
        return "enum.user_status";
    }
}
