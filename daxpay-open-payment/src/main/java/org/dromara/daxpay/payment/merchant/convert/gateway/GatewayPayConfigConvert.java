package org.dromara.daxpay.payment.merchant.convert.gateway;

import org.dromara.daxpay.payment.isv.result.gateway.IsvGatewayPayConfigResult;
import org.dromara.daxpay.payment.merchant.entity.gateway.GatewayPayConfig;
import org.dromara.daxpay.payment.merchant.param.gateway.GatewayPayConfigParam;
import org.dromara.daxpay.payment.merchant.result.gateway.GatewayPayConfigResult;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
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

    void copy(GatewayPayConfigParam param, @MappingTarget GatewayPayConfig config);

    void copy(IsvGatewayPayConfigResult isvGatewayPayConfig, @MappingTarget  GatewayPayConfig config);

    GatewayPayConfig toEntity(IsvGatewayPayConfigResult isvGatewayPayConfig);
}
