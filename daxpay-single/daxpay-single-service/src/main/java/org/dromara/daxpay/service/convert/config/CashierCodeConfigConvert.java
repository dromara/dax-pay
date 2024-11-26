package org.dromara.daxpay.service.convert.config;

import org.dromara.daxpay.service.entity.config.cashier.CashierCodeConfig;
import org.dromara.daxpay.service.param.config.cashier.CashierCodeConfigParam;
import org.dromara.daxpay.service.result.config.cashier.CashierCodeConfigResult;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * 收银台码牌配置
 * @author xxm
 * @since 2024/11/20
 */
@Mapper
public interface CashierCodeConfigConvert {
    CashierCodeConfigConvert CONVERT = Mappers.getMapper(CashierCodeConfigConvert.class);

    CashierCodeConfig toEntity(CashierCodeConfigParam in);

    CashierCodeConfigResult toResult(CashierCodeConfig in);

}
