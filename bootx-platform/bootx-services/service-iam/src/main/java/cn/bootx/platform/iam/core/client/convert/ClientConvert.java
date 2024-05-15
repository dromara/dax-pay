package cn.bootx.platform.iam.core.client.convert;

import cn.bootx.platform.iam.param.client.ClientParam;
import cn.bootx.platform.iam.core.client.entity.Client;
import cn.bootx.platform.iam.dto.client.ClientDto;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * 认证应用
 *
 * @author xxm
 * @since 2022-06-27
 */
@Mapper
public interface ClientConvert {

    ClientConvert CONVERT = Mappers.getMapper(ClientConvert.class);

    Client convert(ClientParam in);

    ClientDto convert(Client in);

}
