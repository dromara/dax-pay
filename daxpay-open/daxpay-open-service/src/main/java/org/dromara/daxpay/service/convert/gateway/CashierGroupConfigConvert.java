package org.dromara.daxpay.service.convert.gateway;

import org.dromara.daxpay.service.entity.gateway.CashierGroupConfig;
import org.dromara.daxpay.service.param.gateway.CashierGroupConfigParam;
import org.dromara.daxpay.service.result.gateway.config.CashierGroupConfigResult;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * 网关收银台分组配置转换
 * @author xxm
 * @since 2025/3/19
 */
@Mapper
public interface CashierGroupConfigConvert {
    CashierGroupConfigConvert CONVERT = Mappers.getMapper(CashierGroupConfigConvert.class);

    CashierGroupConfig toEntity(CashierGroupConfigParam param);

    CashierGroupConfigResult toResult(CashierGroupConfig cashierGroupConfig);
}
