package cn.bootx.platform.iam.core.client.convert;

import cn.bootx.platform.iam.param.client.LoginTypeParam;
import cn.bootx.platform.iam.core.client.entity.LonginType;
import cn.bootx.platform.iam.dto.client.LoginTypeDto;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * 认证终端
 *
 * @author bootx
 * @since 2022-02-20
 */
@Mapper
public interface LoginTypeConvert {

    LoginTypeConvert CONVERT = Mappers.getMapper(LoginTypeConvert.class);

    LonginType convert(LoginTypeParam in);

    LoginTypeDto convert(LonginType in);

}
