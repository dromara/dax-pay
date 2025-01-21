package cn.bootx.platform.iam.convert.user;

import cn.bootx.platform.iam.entity.user.UserExpandInfo;
import cn.bootx.platform.iam.result.user.UserExpandInfoResult;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface UserExpandConvert {

    UserExpandConvert CONVERT = Mappers.getMapper(UserExpandConvert.class);

    UserExpandInfoResult convert(UserExpandInfo in);

}
