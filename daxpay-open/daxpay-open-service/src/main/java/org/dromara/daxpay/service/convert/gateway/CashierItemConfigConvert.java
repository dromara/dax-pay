package org.dromara.daxpay.service.convert.gateway;

import org.dromara.daxpay.service.entity.gateway.CashierItemConfig;
import org.dromara.daxpay.service.param.gateway.CashierItemConfigParam;
import org.dromara.daxpay.service.result.gateway.config.CashierItemConfigResult;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * 网关收银台配置项转换
 * @author xxm
 * @since 2025/3/19
 */
@Mapper
public interface CashierItemConfigConvert {
    CashierItemConfigConvert CONVERT = Mappers.getMapper(CashierItemConfigConvert.class);

    CashierItemConfig toEntity(CashierItemConfigParam param);

    CashierItemConfigResult toResult(CashierItemConfig gatewayCashierItemConfig);
}
