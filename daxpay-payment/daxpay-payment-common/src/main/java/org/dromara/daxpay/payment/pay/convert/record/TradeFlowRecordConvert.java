package org.dromara.daxpay.payment.pay.convert.record;

import org.dromara.daxpay.payment.pay.entity.record.flow.TradeFlowRecord;
import org.dromara.daxpay.payment.pay.result.record.flow.TradeFlowRecordResult;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;


@Mapper
public interface TradeFlowRecordConvert {
    TradeFlowRecordConvert CONVERT = Mappers.getMapper(TradeFlowRecordConvert.class);

    TradeFlowRecordResult convert(TradeFlowRecord entity);
}
