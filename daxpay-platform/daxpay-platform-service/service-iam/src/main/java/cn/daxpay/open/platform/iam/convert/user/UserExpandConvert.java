package cn.daxpay.open.platform.iam.convert.user;

import cn.daxpay.open.platform.iam.entity.user.UserExpandInfo;
import cn.daxpay.open.platform.iam.result.user.UserExpandInfoResult;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface UserExpandConvert {

    UserExpandConvert CONVERT = Mappers.getMapper(UserExpandConvert.class);

    UserExpandInfoResult convert(UserExpandInfo in);

}
