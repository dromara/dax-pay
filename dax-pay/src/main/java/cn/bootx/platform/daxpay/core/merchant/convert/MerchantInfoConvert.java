package cn.bootx.platform.daxpay.core.merchant.convert;

import cn.bootx.platform.daxpay.core.merchant.entity.MerchantInfo;
import cn.bootx.platform.daxpay.dto.merchant.MerchantInfoDto;
import cn.bootx.platform.daxpay.param.merchant.MerchantInfoParam;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * 商户
 *
 * @author xxm
 * @since 2023-05-17
 */
@Mapper
public interface MerchantInfoConvert {

    MerchantInfoConvert CONVERT = Mappers.getMapper(MerchantInfoConvert.class);

    MerchantInfo convert(MerchantInfoParam in);

    MerchantInfoDto convert(MerchantInfo in);

}
