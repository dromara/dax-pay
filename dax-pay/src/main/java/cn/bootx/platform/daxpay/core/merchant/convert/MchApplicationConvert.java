package cn.bootx.platform.daxpay.core.merchant.convert;

import cn.bootx.platform.daxpay.core.merchant.entity.MchApplication;
import cn.bootx.platform.daxpay.dto.merchant.MchApplicationDto;
import cn.bootx.platform.daxpay.param.merchant.MchApplicationParam;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * 商户应用
 *
 * @author xxm
 * @since 2023-05-19
 */
@Mapper
public interface MchApplicationConvert {

    MchApplicationConvert CONVERT = Mappers.getMapper(MchApplicationConvert.class);

    MchApplication convert(MchApplicationParam in);

    MchApplicationDto convert(MchApplication in);

}
