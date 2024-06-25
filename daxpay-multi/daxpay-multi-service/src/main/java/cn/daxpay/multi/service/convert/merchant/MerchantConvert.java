package cn.daxpay.multi.service.convert.merchant;

import cn.daxpay.multi.service.entity.merchant.Merchant;
import cn.daxpay.multi.service.param.merchant.MerchantParam;
import cn.daxpay.multi.service.result.merchant.MerchantResult;
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
}
