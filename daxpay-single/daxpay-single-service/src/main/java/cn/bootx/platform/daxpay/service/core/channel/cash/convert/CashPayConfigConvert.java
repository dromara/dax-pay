package cn.bootx.platform.daxpay.service.core.channel.cash.convert;

import cn.bootx.platform.daxpay.service.core.channel.cash.entity.CashConfig;
import cn.bootx.platform.daxpay.service.dto.channel.cash.CashPayConfigDto;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 *
 * @author xxm
 * @since 2024/2/17
 */
@Mapper
public interface CashPayConfigConvert {
    CashPayConfigConvert CONVERT = Mappers.getMapper(CashPayConfigConvert.class);

    CashPayConfigDto convert(CashConfig in);

}
