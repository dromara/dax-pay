package cn.bootx.platform.daxpay.core.sync.record.convert;

import cn.bootx.platform.daxpay.core.sync.record.entity.PaySyncRecord;
import cn.bootx.platform.daxpay.dto.sync.PaySyncRecordDto;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * 支付同步记录同步
 * @author xxm
 * @since 2023/7/14
 */
@Mapper
public interface PaySyncRecordConvert {
    PaySyncRecordConvert CONVERT = Mappers.getMapper(PaySyncRecordConvert.class);

    PaySyncRecordDto convert(PaySyncRecord in);

}
