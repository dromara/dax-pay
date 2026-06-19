package cn.daxpay.open.platform.iam.convert.user;

import cn.daxpay.open.platform.iam.entity.user.UserPasswordSecurity;
import cn.daxpay.open.platform.iam.result.user.UserPasswordSecurityResult;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/// # 用户密码安全信息转换
///
@Mapper
public interface UserPasswordSecurityConvert {

    UserPasswordSecurityConvert CONVERT = Mappers.getMapper(UserPasswordSecurityConvert.class);

    UserPasswordSecurityResult convert(UserPasswordSecurity entity);
}
