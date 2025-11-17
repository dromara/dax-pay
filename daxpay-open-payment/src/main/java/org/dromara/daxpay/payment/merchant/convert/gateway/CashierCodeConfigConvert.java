package org.dromara.daxpay.payment.merchant.convert.gateway;

import org.dromara.daxpay.payment.merchant.entity.gateway.CashierCodeConfig;
import org.dromara.daxpay.payment.merchant.param.gateway.CashierCodeConfigParam;
import org.dromara.daxpay.payment.merchant.result.gateway.CashierCodeConfigResult;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;

/**
 * 收银码牌配置转换
 * @author xxm
 * @since 2024/11/20
 */
@Mapper
public interface CashierCodeConfigConvert {
    CashierCodeConfigConvert CONVERT = Mappers.getMapper(CashierCodeConfigConvert.class);

    CashierCodeConfig toEntity(CashierCodeConfigParam param);

    CashierCodeConfigResult toResult(CashierCodeConfig entity);

    void copy(CashierCodeConfigParam param, @MappingTarget CashierCodeConfig target);

}
