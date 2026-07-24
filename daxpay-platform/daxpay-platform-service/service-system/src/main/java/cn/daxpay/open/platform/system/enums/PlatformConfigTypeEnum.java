package cn.daxpay.open.platform.system.enums;

import cn.daxpay.open.platform.core.exception.BizException;
import cn.daxpay.open.platform.core.i18n.I18nSupport;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;

/// # 平台配置类型枚举
///
@Getter
@RequiredArgsConstructor
public enum PlatformConfigTypeEnum implements I18nSupport {

    /// 密码策略配置
    SECURITY_PASSWORD_POLICY("security_password_policy"),
    /// 登录安全配置
    SECURITY_LOGIN("security_login"),
    /// 应用内社交自动登录配置(按 admin/merchant 分端)
    SECURITY_SOCIAL_AUTO("security_social_auto"),
    /// 会话管理配置
    SECURITY_SESSION("security_session"),
    /// 双因素认证配置
    SECURITY_TWO_FACTOR_AUTH("security_two_factor_auth"),
    /// API安全配置（开放支付接口防重放）
    API_SECURITY("api_security"),
    /// 支付安全配置（支付风控开关）
    PAY_SECURITY("pay_security"),
    /// IAM域防重放配置（登录/注册/改密等敏感操作）
    IAM_REPLAY_PROTECT("iam_replay_protect"),
    /// 系统访问地址配置
    URL("url"),
    /// 站点显示内容配置(系统名/Logo/备案/版权等)
    WEBSITE("website"),
    /// 微信消息通知模板配置(非敏感, 仅模板Id)
    WECHAT_NOTIFY("wechat_notify"),
    /// 敏感词策略配置
    SENSITIVE_WORD("sensitive_word");

    /// 编码
    private final String code;

    @Override
    public String getI18nPrefix() {
        return "enum.platform_config_type";
    }

    /// 根据编码获取枚举
    public static PlatformConfigTypeEnum findByCode(String code) {
        return Arrays.stream(values())
            .filter(e -> e.getCode().equals(code))
            .findFirst()
            // 通用: 未知的平台配置类型
            .orElseThrow(() -> new BizException("error.common.enumUnknown", "PlatformConfigType"));
    }
}
