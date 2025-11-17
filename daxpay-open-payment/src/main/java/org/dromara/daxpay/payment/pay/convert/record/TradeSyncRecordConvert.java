package org.dromara.daxpay.payment.pay.convert.record;

import org.dromara.daxpay.payment.pay.entity.record.sync.TradeSyncRecord;
import org.dromara.daxpay.payment.pay.result.record.sync.TradeSyncRecordResult;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * 支付同步记录同步
 * @author xxm
 * @since 2023/7/14
 */
@Mapper
public interface TradeSyncRecordConvert {
    TradeSyncRecordConvert CONVERT = Mappers.getMapper(TradeSyncRecordConvert.class);

    TradeSyncRecordResult convert(TradeSyncRecord in);

}
