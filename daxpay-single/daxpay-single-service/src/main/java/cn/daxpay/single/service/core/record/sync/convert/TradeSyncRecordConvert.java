package cn.daxpay.single.service.core.record.sync.convert;

import cn.daxpay.single.service.core.record.sync.entity.TradeSyncRecord;
import cn.daxpay.single.service.dto.record.sync.SyncRecordDto;
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

    SyncRecordDto convert(TradeSyncRecord in);

}
