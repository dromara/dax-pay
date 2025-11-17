package org.dromara.daxpay.payment.merchant.convert.gateway;

import org.dromara.daxpay.payment.merchant.entity.gateway.GatewayPayReadConfig;
import org.dromara.daxpay.payment.merchant.param.gateway.GatewayPayReadConfigParam;
import org.dromara.daxpay.payment.merchant.result.gateway.GatewayPayReadConfigResult;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;

/**
 * 网关支付配置
 * @author xxm
 * @since 2025/10/14
 */
@Mapper
public interface GatewayPayReadConvert {
    GatewayPayReadConvert CONVERT = Mappers.getMapper(GatewayPayReadConvert.class);

    GatewayPayReadConfigResult toResult(GatewayPayReadConfig gatewayPayReadConfig);

    void copy(GatewayPayReadConfigParam entity, @MappingTarget GatewayPayReadConfig config);
}
