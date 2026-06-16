package org.dromara.daxpay.platform.system.convert;

import org.dromara.daxpay.platform.system.entity.config.platform.PlatformOssConfig;
import org.dromara.daxpay.platform.system.entity.config.platform.security.*;
import org.dromara.daxpay.platform.system.param.config.*;
import org.dromara.daxpay.platform.system.result.config.platform.*;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.factory.Mappers;

/// # 平台配置转换
///
@Mapper
public interface PlatformConfigConvert {
    PlatformConfigConvert CONVERT = Mappers.getMapper(PlatformConfigConvert.class);

    // ========== OSS配置转换（单配置模式） ==========
    PlatformOssConfigResult toOssResult(PlatformOssConfig data);

    PlatformOssConfig convert(PlatformOssConfigParam param);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void copy(PlatformOssConfigParam param, @MappingTarget PlatformOssConfig data);

    // ========== 异常登录检测配置转换 ==========
    PlatformAnomalyDetectionConfigResult toAnomalyDetectionResult(PlatformAnomalyDetectionConfig data);

    void copy(PlatformAnomalyDetectionConfigParam param, @MappingTarget PlatformAnomalyDetectionConfig data);

    // ========== 登录安全配置转换 ==========
    PlatformLoginSecurityConfigResult toLoginSecurityResult(PlatformLoginSecurityConfig data);

    void copy(PlatformLoginSecurityConfigParam param, @MappingTarget PlatformLoginSecurityConfig data);

    // ========== 密码策略配置转换 ==========
    PlatformPasswordPolicyConfigResult toPasswordPolicyResult(PlatformPasswordPolicyConfig data);

    void copy(PlatformPasswordPolicyConfigParam param, @MappingTarget PlatformPasswordPolicyConfig data);

    // ========== 会话管理配置转换 ==========
    PlatformSessionManagementConfigResult toSessionManagementResult(PlatformSessionManagementConfig data);

    void copy(PlatformSessionManagementConfigParam param, @MappingTarget PlatformSessionManagementConfig data);

    // ========== 双因素认证配置转换 ==========
    PlatformTwoFactorAuthConfigResult toTwoFactorAuthResult(PlatformTwoFactorAuthConfig data);

    void copy(PlatformTwoFactorAuthConfigParam param, @MappingTarget PlatformTwoFactorAuthConfig data);
}
