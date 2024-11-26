package org.dromara.daxpay.service.convert.config;

import org.dromara.daxpay.service.entity.config.cashier.CashierCodeTypeConfig;
import org.dromara.daxpay.service.param.config.cashier.CashierCodeTypeConfigParam;
import org.dromara.daxpay.service.result.config.cashier.CashierCodeTypeConfigResult;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * 收银台码牌配置
 * @author xxm
 * @since 2024/11/20
 */
@Mapper
public interface CashierCodeTypeConfigConvert {
    CashierCodeTypeConfigConvert CONVERT = Mappers.getMapper(CashierCodeTypeConfigConvert.class);

    CashierCodeTypeConfigResult toResult(CashierCodeTypeConfig in);

    CashierCodeTypeConfig toEntity(CashierCodeTypeConfigParam in);
}
