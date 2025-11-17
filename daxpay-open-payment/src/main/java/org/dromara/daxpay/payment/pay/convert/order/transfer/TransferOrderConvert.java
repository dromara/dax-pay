package org.dromara.daxpay.payment.pay.convert.order.transfer;

import org.dromara.daxpay.payment.pay.entity.order.transfer.TransferOrder;
import org.dromara.daxpay.payment.pay.result.order.transfer.TransferOrderVo;
import org.dromara.daxpay.payment.unipay.result.trade.transfer.TransferOrderResult;
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
