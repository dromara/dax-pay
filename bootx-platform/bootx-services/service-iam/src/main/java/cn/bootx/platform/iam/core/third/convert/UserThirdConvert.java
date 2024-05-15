package cn.bootx.platform.iam.core.third.convert;

import cn.bootx.platform.iam.core.third.entity.UserThird;
import cn.bootx.platform.iam.dto.user.UserThirdDto;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface UserThirdConvert {

    UserThirdConvert CONVERT = Mappers.getMapper(UserThirdConvert.class);

    UserThirdDto convert(UserThird in);

}
