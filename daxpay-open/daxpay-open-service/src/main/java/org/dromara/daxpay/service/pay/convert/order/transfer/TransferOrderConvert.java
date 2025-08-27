package org.dromara.daxpay.service.pay.convert.order.transfer;

import org.dromara.daxpay.core.result.trade.transfer.TransferOrderResult;
import org.dromara.daxpay.service.pay.entity.order.transfer.TransferOrder;
import org.dromara.daxpay.service.pay.result.order.transfer.TransferOrderVo;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 *
 * @author xxm
 * @since 2024/7/20
 */
@Mapper
public interface TransferOrderConvert {
    TransferOrderConvert CONVERT = Mappers.getMapper(TransferOrderConvert.class);

    TransferOrderVo toVo(TransferOrder in);

    TransferOrderResult toResult(TransferOrder in);

}
