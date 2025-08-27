package cn.bootx.platform.baseapi.convert.protocol;

import cn.bootx.platform.baseapi.entity.protocol.UserProtocol;
import cn.bootx.platform.baseapi.param.protocol.UserProtocolParam;
import cn.bootx.platform.baseapi.result.protocol.UserProtocolResult;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 *用户协议管理
 * @author xxm
 * @since 2025/5/9
 */
@Mapper
public interface UserProtocolConvert {
    UserProtocolConvert CONVERT = Mappers.getMapper(UserProtocolConvert.class);

    UserProtocolResult toResult(UserProtocol userProtocol);

    UserProtocol toEntity(UserProtocolParam param);
}
