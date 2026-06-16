package org.dromara.daxpay.payment.old.pay.convert.record;

import org.dromara.daxpay.payment.old.pay.entity.record.callback.TradeCallbackRecord;
import org.dromara.daxpay.payment.old.pay.result.record.callback.TradeCallbackRecordResult;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/// # 回调记录
///
@Mapper
public interface TradeCallbackRecordConvert {
    TradeCallbackRecordConvert CONVERT = Mappers.getMapper(TradeCallbackRecordConvert.class);

    TradeCallbackRecordResult convert(TradeCallbackRecord record);
}
