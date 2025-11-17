package org.dromara.daxpay.payment.isv.convert.gateway;

import org.dromara.daxpay.payment.isv.entity.gateway.IsvGatewayPayConfig;
import org.dromara.daxpay.payment.isv.param.gateway.IsvGatewayPayConfigParam;
import org.dromara.daxpay.payment.isv.result.gateway.IsvGatewayPayConfigResult;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * 聚合支付配置转换
 * @author xxm
 * @since 2025/6/30
 */
@Mapper
public interface IsvGatewayPayConfigConvert {
    IsvGatewayPayConfigConvert CONVERT = Mappers.getMapper(IsvGatewayPayConfigConvert.class);

    IsvGatewayPayConfig toEntity(IsvGatewayPayConfigParam param);

    IsvGatewayPayConfigResult toResult(IsvGatewayPayConfig isvGatewayPayConfig);
}
