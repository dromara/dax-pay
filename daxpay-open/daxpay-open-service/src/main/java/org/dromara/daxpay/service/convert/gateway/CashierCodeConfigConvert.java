package org.dromara.daxpay.service.convert.gateway;

import org.dromara.daxpay.service.entity.gateway.CashierCodeConfig;
import org.dromara.daxpay.service.param.gateway.CashierCodeConfigParam;
import org.dromara.daxpay.service.result.gateway.config.CashierCodeConfigResult;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * 收银码牌配置
 * @author xxm
 * @since 2025/4/1
 */
@Mapper
public interface CashierCodeConfigConvert {
    CashierCodeConfigConvert CONVERT = Mappers.getMapper(CashierCodeConfigConvert.class);

    CashierCodeConfig toEntity(CashierCodeConfigParam param);

    CashierCodeConfigResult toResult(CashierCodeConfig cashierCodeConfig);
}
