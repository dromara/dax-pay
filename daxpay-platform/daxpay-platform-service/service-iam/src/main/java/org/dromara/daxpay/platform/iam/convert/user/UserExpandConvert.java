package org.dromara.daxpay.platform.iam.convert.user;

import org.dromara.daxpay.platform.iam.entity.user.UserExpandInfo;
import org.dromara.daxpay.platform.iam.result.user.UserExpandInfoResult;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface UserExpandConvert {

    UserExpandConvert CONVERT = Mappers.getMapper(UserExpandConvert.class);

    UserExpandInfoResult convert(UserExpandInfo in);

}
