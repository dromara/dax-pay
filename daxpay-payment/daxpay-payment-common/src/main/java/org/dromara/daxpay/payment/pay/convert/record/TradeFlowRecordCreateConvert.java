package org.dromara.daxpay.payment.pay.convert.record;

import org.dromara.daxpay.payment.pay.entity.order.pay.PayOrder;
import org.dromara.daxpay.payment.pay.entity.order.refund.RefundOrder;
import org.dromara.daxpay.payment.pay.entity.order.transfer.TransferOrder;
import org.dromara.daxpay.payment.pay.entity.record.flow.TradeFlowRecord;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/// # 交易流水记录创建转换
///
@Mapper
public interface TradeFlowRecordCreateConvert {
    TradeFlowRecordCreateConvert CONVERT = Mappers.getMapper(TradeFlowRecordCreateConvert.class);

    TradeFlowRecord fromPayOrder(PayOrder payOrder);

    TradeFlowRecord fromRefundOrder(RefundOrder refundOrder);

    TradeFlowRecord fromTransferOrder(TransferOrder transferOrder);
}
