package org.dromara.daxpay.service.isv.convert.gateway;

import org.dromara.daxpay.service.isv.entity.gateway.IsvCashierGroupConfig;
import org.dromara.daxpay.service.isv.param.gateway.IsvCashierGroupConfigParam;
import org.dromara.daxpay.service.isv.result.gateway.IsvCashierGroupConfigResult;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * 网关收银台分组配置转换
 * @author xxm
 * @since 2025/3/19
 */
@Mapper
public interface IsvCashierGroupConfigConvert {
    IsvCashierGroupConfigConvert CONVERT = Mappers.getMapper(IsvCashierGroupConfigConvert.class);

    IsvCashierGroupConfig toEntity(IsvCashierGroupConfigParam param);

    IsvCashierGroupConfigResult toResult(IsvCashierGroupConfig cashierGroupConfig);
}
