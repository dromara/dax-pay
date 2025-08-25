package org.dromara.daxpay.service.merchant.convert.info;

import org.dromara.daxpay.service.merchant.entity.info.Merchant;
import org.dromara.daxpay.service.merchant.param.info.MerchantCreateParam;
import org.dromara.daxpay.service.merchant.param.info.MerchantParam;
import org.dromara.daxpay.service.merchant.result.info.MerchantResult;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 *
 * @author xxm
 * @since 2024/6/24
 */
@Mapper
public interface MerchantConvert {
    MerchantConvert CONVERT = Mappers.getMapper(MerchantConvert.class);

    Merchant toEntity(MerchantParam param);

    MerchantResult toResult(Merchant entity);

    Merchant toEntity(MerchantCreateParam param);
}
