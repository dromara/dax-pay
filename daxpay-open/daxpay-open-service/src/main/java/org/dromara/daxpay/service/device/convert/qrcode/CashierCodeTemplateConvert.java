package org.dromara.daxpay.service.device.convert.qrcode;

import org.dromara.daxpay.service.device.entity.qrcode.template.CashierCodeTemplate;
import org.dromara.daxpay.service.device.param.qrcode.template.CashierCodeTemplateParam;
import org.dromara.daxpay.service.device.result.qrcode.template.CashierCodeTemplateResult;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 *
 * @author xxm
 * @since 2025/7/1
 */
@Mapper
public interface CashierCodeTemplateConvert {
    CashierCodeTemplateConvert CONVERT = Mappers.getMapper(CashierCodeTemplateConvert.class);

    CashierCodeTemplateResult toResult(CashierCodeTemplate cashierCodeTemplate);

    CashierCodeTemplate toEntity(CashierCodeTemplateParam param);
}
