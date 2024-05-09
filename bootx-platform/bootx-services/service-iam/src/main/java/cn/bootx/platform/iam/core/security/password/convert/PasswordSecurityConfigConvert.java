package cn.bootx.platform.iam.core.security.password.convert;

import cn.bootx.platform.iam.core.security.password.entity.PasswordSecurityConfig;
import cn.bootx.platform.iam.dto.security.PasswordSecurityConfigDto;
import cn.bootx.platform.iam.param.security.PasswordSecurityConfigParam;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * 密码安全策略
 * @author xxm
 * @since 2023-09-20
 */
@Mapper
public interface PasswordSecurityConfigConvert {
    PasswordSecurityConfigConvert CONVERT = Mappers.getMapper(PasswordSecurityConfigConvert.class);

    PasswordSecurityConfig convert(PasswordSecurityConfigParam in);

    PasswordSecurityConfigDto convert(PasswordSecurityConfig in);

}
