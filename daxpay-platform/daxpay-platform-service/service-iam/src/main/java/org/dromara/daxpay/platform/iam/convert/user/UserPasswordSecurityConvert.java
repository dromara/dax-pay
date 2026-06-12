package org.dromara.daxpay.platform.iam.convert.user;

import org.dromara.daxpay.platform.iam.entity.user.UserPasswordSecurity;
import org.dromara.daxpay.platform.iam.result.user.UserPasswordSecurityResult;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/// # 用户密码安全信息转换
///
@Mapper
public interface UserPasswordSecurityConvert {

    UserPasswordSecurityConvert CONVERT = Mappers.getMapper(UserPasswordSecurityConvert.class);

    UserPasswordSecurityResult convert(UserPasswordSecurity entity);
}
