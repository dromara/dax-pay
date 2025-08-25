package org.dromara.daxpay.service.device.convert.qrcode;

import org.dromara.daxpay.service.device.entity.qrcode.config.CashierCodeSceneConfig;
import org.dromara.daxpay.service.device.param.qrcode.config.CashierCodeSceneConfigParam;
import org.dromara.daxpay.service.device.result.qrcode.config.CashierCodeSceneConfigResult;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * 码牌条码配置
 * @author xxm
 * @since 2025/4/1
 */
@Mapper
public interface CashierCodeSceneConfigConvert {
    CashierCodeSceneConfigConvert CONVERT = Mappers.getMapper(CashierCodeSceneConfigConvert.class);

    CashierCodeSceneConfig toEntity(CashierCodeSceneConfigParam param);

    CashierCodeSceneConfigResult toResult(CashierCodeSceneConfig cashierCodeSceneConfig);
}
