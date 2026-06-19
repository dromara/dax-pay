package cn.daxpay.open.payment.old.pay.convert.record;

import cn.daxpay.open.payment.old.pay.entity.record.flow.TradeFlowRecord;
import cn.daxpay.open.payment.old.pay.result.record.flow.TradeFlowRecordResult;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;


@Mapper
public interface TradeFlowRecordConvert {
    TradeFlowRecordConvert CONVERT = Mappers.getMapper(TradeFlowRecordConvert.class);

    TradeFlowRecordResult convert(TradeFlowRecord entity);
}
