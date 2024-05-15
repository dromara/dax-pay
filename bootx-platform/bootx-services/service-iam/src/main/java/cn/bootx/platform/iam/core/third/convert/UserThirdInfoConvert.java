package cn.bootx.platform.iam.core.third.convert;

import cn.bootx.platform.iam.core.third.entity.UserThirdInfo;
import cn.bootx.platform.iam.dto.user.UserThirdInfoDto;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * 用户三方登录绑定详情
 *
 * @author xxm
 * @since 2022-07-02
 */
@Mapper
public interface UserThirdInfoConvert {

    UserThirdInfoConvert CONVERT = Mappers.getMapper(UserThirdInfoConvert.class);

    UserThirdInfoDto convert(UserThirdInfo in);

}
