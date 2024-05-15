package cn.bootx.platform.iam.core.user.convert;

import cn.bootx.platform.iam.dto.online.OnlineUserInfoDto;
import cn.bootx.platform.iam.param.user.UserInfoParam;
import cn.bootx.platform.iam.core.user.entity.UserExpandInfo;
import cn.bootx.platform.iam.core.user.entity.UserInfo;
import cn.bootx.platform.iam.dto.user.UserExpandInfoDto;
import cn.bootx.platform.iam.dto.user.UserInfoDto;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface UserConvert {

    UserConvert CONVERT = Mappers.getMapper(UserConvert.class);

    UserInfo convert(UserInfoParam in);

    UserInfoDto convert(UserInfo in);

    UserExpandInfoDto convert(UserExpandInfo in);

    OnlineUserInfoDto toOnline(UserInfo in);

}
