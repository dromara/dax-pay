package cn.daxpay.multi.service.convert.constant;

import cn.daxpay.multi.service.entity.constant.MerchantNotifyConst;
import cn.daxpay.multi.service.result.constant.MerchantNotifyConstResult;
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
