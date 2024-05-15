package cn.bootx.platform.iam.core.security.user.convert;

import cn.bootx.platform.iam.core.security.user.entity.LoginSecurityConfig;
import cn.bootx.platform.iam.dto.security.LoginSecurityConfigDto;
import cn.bootx.platform.iam.param.security.LoginSecurityConfigParam;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * 登录安全策略
 * @author xxm
 * @since 2023-09-19
 */
@Mapper
public interface LoginSecurityConfigConvert {
    LoginSecurityConfigConvert CONVERT = Mappers.getMapper(LoginSecurityConfigConvert.class);

    LoginSecurityConfig convert(LoginSecurityConfigParam in);

    LoginSecurityConfigDto convert(LoginSecurityConfig in);

}
