package org.dromara.daxpay.payment.pay.convert.order.transfer;

import org.dromara.daxpay.payment.unipay.result.trade.transfer.TransferOrderResult;
import org.dromara.daxpay.payment.pay.entity.order.transfer.TransferOrder;
import org.dromara.daxpay.payment.pay.result.order.transfer.TransferOrderVo;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;


@Mapper
public interface TransferOrderConvert {
    TransferOrderConvert CONVERT = Mappers.getMapper(TransferOrderConvert.class);

    TransferOrderVo toVo(TransferOrder in);

    TransferOrderResult toResult(TransferOrder in);

}
