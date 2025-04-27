package org.dromara.daxpay.service.convert.gateway;

import org.dromara.daxpay.service.entity.gateway.GatewayPayConfig;
import org.dromara.daxpay.service.param.gateway.GatewayPayConfigParam;
import org.dromara.daxpay.service.result.gateway.config.GatewayPayConfigResult;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * 网关支付配置
 * @author xxm
 * @since 2025/3/19
 */
@Mapper
public interface GatewayPayConfigConvert {
    GatewayPayConfigConvert CONVERT = Mappers.getMapper(GatewayPayConfigConvert.class);


    GatewayPayConfigResult toResult(GatewayPayConfig gatewayPayConfig);

    GatewayPayConfig toEntity(GatewayPayConfigParam param);
}
