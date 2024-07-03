package cn.bootx.platform.iam.convert.client;

import cn.bootx.platform.iam.entity.client.Client;
import cn.bootx.platform.iam.param.client.ClientParam;
import cn.bootx.platform.iam.result.client.ClientResult;
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

    ClientResult convert(Client in);

}
