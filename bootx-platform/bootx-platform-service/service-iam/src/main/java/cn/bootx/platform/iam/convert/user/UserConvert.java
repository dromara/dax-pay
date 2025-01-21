package cn.bootx.platform.iam.convert.user;

import cn.bootx.platform.iam.entity.user.UserInfo;
import cn.bootx.platform.iam.param.user.UserInfoParam;
import cn.bootx.platform.iam.result.user.UserInfoResult;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface UserConvert {

    UserConvert CONVERT = Mappers.getMapper(UserConvert.class);

    UserInfo convert(UserInfoParam in);

    UserInfoResult convert(UserInfo in);

}
