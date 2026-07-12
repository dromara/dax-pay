package cn.daxpay.open.platform.system.convert.config.security;

import cn.daxpay.open.platform.system.entity.config.platform.security.*;
import cn.daxpay.open.platform.system.param.config.security.*;
import cn.daxpay.open.platform.system.result.config.security.*;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;

/// # 平台安全配置转换
///
/// 转换密码策略、登录安全、会话管理、双因素认证等安全类配置
@Mapper
public interface PlatformSecurityConfigConvert {
    PlatformSecurityConfigConvert CONVERT = Mappers.getMapper(PlatformSecurityConfigConvert.class);

    // ========== 密码策略配置转换 ==========
    PlatformPasswordPolicyConfigResult toPasswordPolicyResult(PlatformPasswordPolicyConfig data);

    void copy(PlatformPasswordPolicyConfigParam param, @MappingTarget PlatformPasswordPolicyConfig data);

    // ========== 登录安全配置转换 ==========
    PlatformLoginSecurityConfigResult toLoginSecurityResult(PlatformLoginSecurityConfig data);

    void copy(PlatformLoginSecurityConfigParam param, @MappingTarget PlatformLoginSecurityConfig data);

    // ========== 会话管理配置转换 ==========
    PlatformSessionManagementConfigResult toSessionManagementResult(PlatformSessionManagementConfig data);

    void copy(PlatformSessionManagementConfigParam param, @MappingTarget PlatformSessionManagementConfig data);

    // ========== 双因素认证配置转换 ==========
    PlatformTwoFactorAuthConfigResult toTwoFactorAuthResult(PlatformTwoFactorAuthConfig data);

    void copy(PlatformTwoFactorAuthConfigParam param, @MappingTarget PlatformTwoFactorAuthConfig data);
}
