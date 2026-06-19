package cn.daxpay.open.payment.old.pay.convert.record;

import cn.daxpay.open.payment.old.pay.entity.record.sync.TradeSyncRecord;
import cn.daxpay.open.payment.old.pay.result.record.sync.TradeSyncRecordResult;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/// # 支付同步记录同步
///
@Mapper
public interface TradeSyncRecordConvert {
    TradeSyncRecordConvert CONVERT = Mappers.getMapper(TradeSyncRecordConvert.class);

    TradeSyncRecordResult convert(TradeSyncRecord in);

}
