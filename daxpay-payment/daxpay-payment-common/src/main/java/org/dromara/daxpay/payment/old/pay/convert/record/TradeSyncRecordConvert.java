package org.dromara.daxpay.payment.old.pay.convert.record;

import org.dromara.daxpay.payment.old.pay.entity.record.sync.TradeSyncRecord;
import org.dromara.daxpay.payment.old.pay.result.record.sync.TradeSyncRecordResult;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/// # 支付同步记录同步
///
@Mapper
public interface TradeSyncRecordConvert {
    TradeSyncRecordConvert CONVERT = Mappers.getMapper(TradeSyncRecordConvert.class);

    TradeSyncRecordResult convert(TradeSyncRecord in);

}
