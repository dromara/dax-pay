package org.dromara.daxpay.service.convert.gateway;

import org.dromara.daxpay.service.entity.gateway.CashierCodeItemConfig;
import org.dromara.daxpay.service.param.gateway.CashierCodeItemConfigParam;
import org.dromara.daxpay.service.result.gateway.config.CashierCodeItemConfigResult;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * 码牌条码配置
 * @author xxm
 * @since 2025/4/1
 */
@Mapper
public interface CashierCodeItemConfigConvert {
    CashierCodeItemConfigConvert CONVERT = Mappers.getMapper(CashierCodeItemConfigConvert.class);

    CashierCodeItemConfig toEntity(CashierCodeItemConfigParam param);

    CashierCodeItemConfigResult toResult(CashierCodeItemConfig cashierCodeItemConfig);
}
