package org.dromara.daxpay.service.merchant.convert.gateway;

import org.dromara.daxpay.service.merchant.entity.gateway.GatewayPayConfig;
import org.dromara.daxpay.service.merchant.param.gateway.GatewayPayConfigParam;
import org.dromara.daxpay.service.merchant.result.gateway.GatewayPayConfigResult;
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
