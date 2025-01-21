package org.dromara.daxpay.service.convert.constant;

import org.dromara.daxpay.service.entity.constant.MerchantNotifyConst;
import org.dromara.daxpay.service.result.constant.MerchantNotifyConstResult;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 *
 * @author xxm
 * @since 2024/8/5
 */
@Mapper
public interface MerchantNotifyConstConvert {
    MerchantNotifyConstConvert CONVERT = Mappers.getMapper(MerchantNotifyConstConvert.class);

    MerchantNotifyConstResult toResult(MerchantNotifyConst model);
}
