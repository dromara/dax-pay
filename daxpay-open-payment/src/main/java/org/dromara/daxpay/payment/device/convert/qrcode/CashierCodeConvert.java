package org.dromara.daxpay.payment.device.convert.qrcode;

import org.dromara.daxpay.payment.device.entity.qrcode.info.CashierCode;
import org.dromara.daxpay.payment.device.param.qrcode.info.CashierCodeBatchParam;
import org.dromara.daxpay.payment.device.param.qrcode.info.CashierCodeUpdateParam;
import org.dromara.daxpay.payment.device.result.qrcode.info.CashierCodeResult;
import org.dromara.daxpay.payment.merchant.entity.gateway.CashierCodeConfig;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
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

    CashierCode toEntity(CashierCodeBatchParam param);

    void copy(CashierCodeConfig cashierCodeConfig, @MappingTarget CashierCode cashierCode);

    void copy(CashierCodeUpdateParam param, @MappingTarget CashierCode cashierCode);
}
