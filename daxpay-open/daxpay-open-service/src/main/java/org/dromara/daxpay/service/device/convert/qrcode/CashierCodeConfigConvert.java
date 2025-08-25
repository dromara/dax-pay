package org.dromara.daxpay.service.device.convert.qrcode;

import org.dromara.daxpay.service.device.entity.qrcode.config.CashierCodeConfig;
import org.dromara.daxpay.service.device.param.qrcode.config.CashierCodeConfigParam;
import org.dromara.daxpay.service.device.result.qrcode.config.CashierCodeConfigResult;
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
