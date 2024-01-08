package cn.bootx.platform.daxpay.service.core.system.payinfo.convert;

import cn.bootx.platform.daxpay.service.core.system.payinfo.entity.PayWayInfo;
import cn.bootx.platform.daxpay.service.dto.system.payinfo.PayWayInfoDto;
import cn.bootx.platform.daxpay.service.param.system.payinfo.PayWayInfoParam;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 *
 * @author xxm
 * @since 2024/1/8
 */
@Mapper
public interface PayWayInfoConvert {
    PayWayInfoConvert CONVERT = Mappers.getMapper(PayWayInfoConvert.class);

    PayWayInfo convert(PayWayInfoParam in);

    PayWayInfoDto convert(PayWayInfo in);
}
