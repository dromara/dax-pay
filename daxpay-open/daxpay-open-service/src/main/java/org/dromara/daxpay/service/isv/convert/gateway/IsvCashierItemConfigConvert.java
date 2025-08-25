package org.dromara.daxpay.service.isv.convert.gateway;

import org.dromara.daxpay.service.isv.entity.gateway.IsvCashierItemConfig;
import org.dromara.daxpay.service.isv.param.gateway.IsvCashierItemConfigParam;
import org.dromara.daxpay.service.isv.result.gateway.IsvCashierItemConfigResult;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * 网关收银台配置项转换
 * @author xxm
 * @since 2025/3/19
 */
@Mapper
public interface IsvCashierItemConfigConvert {
    IsvCashierItemConfigConvert CONVERT = Mappers.getMapper(IsvCashierItemConfigConvert.class);

    IsvCashierItemConfig toEntity(IsvCashierItemConfigParam param);

    IsvCashierItemConfigResult toResult(IsvCashierItemConfig gatewayCashierItemConfig);
}
