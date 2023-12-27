package cn.bootx.platform.daxpay.core.order.sync.convert;

import cn.bootx.platform.daxpay.core.order.sync.entity.PaySyncOrder;
import cn.bootx.platform.daxpay.dto.order.sync.PaySyncOrderDto;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * 支付同步记录同步
 * @author xxm
 * @since 2023/7/14
 */
@Mapper
public interface PaySyncOrderConvert {
    PaySyncOrderConvert CONVERT = Mappers.getMapper(PaySyncOrderConvert.class);

    PaySyncOrderDto convert(PaySyncOrder in);

}
