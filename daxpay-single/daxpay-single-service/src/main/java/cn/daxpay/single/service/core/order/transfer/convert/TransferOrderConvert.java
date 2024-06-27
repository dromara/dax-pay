package cn.daxpay.single.service.core.order.transfer.convert;

import cn.daxpay.single.core.result.order.TransferOrderResult;
import cn.daxpay.single.service.core.order.transfer.entity.TransferOrder;
import cn.daxpay.single.service.dto.order.transfer.TransferOrderDto;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 *
 * @author xxm
 * @since 2024/3/21
 */
@Mapper
public interface TransferOrderConvert {
    TransferOrderConvert CONVERT = Mappers.getMapper(TransferOrderConvert.class);

    TransferOrderDto convert(TransferOrder in);


    TransferOrderResult convertResult(TransferOrder in);
}
