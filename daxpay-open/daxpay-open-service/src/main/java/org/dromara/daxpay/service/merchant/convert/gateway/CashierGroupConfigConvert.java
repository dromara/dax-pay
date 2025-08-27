package org.dromara.daxpay.service.merchant.convert.gateway;

import org.dromara.daxpay.service.merchant.entity.gateway.CashierGroupConfig;
import org.dromara.daxpay.service.merchant.param.gateway.CashierGroupConfigParam;
import org.dromara.daxpay.service.merchant.result.gateway.CashierGroupConfigResult;
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
