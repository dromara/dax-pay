package cn.daxpay.open.platform.system.convert.config.security;

import cn.daxpay.open.platform.system.entity.config.platform.security.PlatformSocialAutoLoginConfig;
import cn.daxpay.open.platform.system.param.config.security.PlatformSocialAutoLoginConfigParam;
import cn.daxpay.open.platform.system.result.config.security.PlatformSocialAutoLoginConfigResult;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;

/// # 应用内社交自动登录配置转换
///
@Mapper
public interface PlatformSocialAutoLoginConfigConvert {

    PlatformSocialAutoLoginConfigConvert CONVERT = Mappers.getMapper(PlatformSocialAutoLoginConfigConvert.class);

    PlatformSocialAutoLoginConfigResult toResult(PlatformSocialAutoLoginConfig data);

    void copy(PlatformSocialAutoLoginConfigParam param, @MappingTarget PlatformSocialAutoLoginConfig data);
}
