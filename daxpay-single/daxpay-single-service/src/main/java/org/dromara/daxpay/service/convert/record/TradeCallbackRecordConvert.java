package org.dromara.daxpay.service.convert.record;

import org.dromara.daxpay.service.entity.record.callback.TradeCallbackRecord;
import org.dromara.daxpay.service.result.record.callback.TradeCallbackRecordResult;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * 回调记录
 * @author xxm
 * @since 2024/7/22
 */
@Mapper
public interface TradeCallbackRecordConvert {
    TradeCallbackRecordConvert CONVERT = Mappers.getMapper(TradeCallbackRecordConvert.class);

    TradeCallbackRecordResult convert(TradeCallbackRecord record);
}
