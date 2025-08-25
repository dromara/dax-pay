package org.dromara.daxpay.service.device.convert.qrcode;

import org.dromara.daxpay.service.device.entity.qrcode.info.CashierCode;
import org.dromara.daxpay.service.device.param.qrcode.info.CashierCodeCreateParam;
import org.dromara.daxpay.service.device.result.qrcode.info.CashierCodeResult;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 *
 * @author xxm
 * @since 2025/7/1
 */
@Mapper
public interface CashierCodeConvert {
    CashierCodeConvert CONVERT = Mappers.getMapper(CashierCodeConvert.class);

    CashierCodeResult toResult(CashierCode cashierCode);

    CashierCode toEntity(CashierCodeCreateParam param);
}
