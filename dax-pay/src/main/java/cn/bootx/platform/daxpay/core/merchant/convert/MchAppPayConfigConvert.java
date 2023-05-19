package cn.bootx.platform.daxpay.core.merchant.convert;

import cn.bootx.platform.daxpay.core.merchant.entity.MchAppPayConfig;
import cn.bootx.platform.daxpay.dto.merchant.MchAppPayConfigDto;
import cn.bootx.platform.daxpay.param.merchant.MchAppPayConfigParam;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * 商户应用支付配置
 * @author xxm
 * @date 2023-05-19
 */
@Mapper
public interface MchAppPayConfigConvert {
    MchAppPayConfigConvert CONVERT = Mappers.getMapper(MchAppPayConfigConvert.class);

    MchAppPayConfig convert(MchAppPayConfigParam in);

    MchAppPayConfigDto convert(MchAppPayConfig in);

}