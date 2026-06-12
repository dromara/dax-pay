package org.dromara.daxpay.platform.system.enums;

import org.dromara.daxpay.platform.core.exception.BizException;
import org.dromara.daxpay.platform.core.i18n.I18nSupport;
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
    /// 会话管理配置
    SECURITY_SESSION("security_session"),
    /// 异常登录检测配置
    ANOMALY_DETECTION("anomaly_detection"),
    /// 双因素认证配置
    SECURITY_TWO_FACTOR_AUTH("security_two_factor_auth"),
    /// 系统访问地址配置
    URL("url");

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
            .orElseThrow(() -> new BizException("error.common.enumUnknown", "PlatformConfigType"));
    }
}
