package cn.daxpay.open.payment.old.pay.convert.record;

import cn.daxpay.open.payment.old.pay.entity.order.pay.PayOrder;
import cn.daxpay.open.payment.old.pay.entity.record.flow.TradeFlowRecord;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/// # 交易流水记录创建转换
///
@Mapper
public interface TradeFlowRecordCreateConvert {
    TradeFlowRecordCreateConvert CONVERT = Mappers.getMapper(TradeFlowRecordCreateConvert.class);

    TradeFlowRecord fromPayOrder(PayOrder payOrder);
}
