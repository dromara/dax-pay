package cn.daxpay.multi.service.convert.record;

import cn.daxpay.multi.service.entity.record.callback.CallbackRecord;
import cn.daxpay.multi.service.result.record.callback.CallbackRecordResult;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * 回调记录
 * @author xxm
 * @since 2024/7/22
 */
@Mapper
public interface CallbackRecordConvert {
    CallbackRecordConvert CONVERT = Mappers.getMapper(CallbackRecordConvert.class);

    CallbackRecordResult convert(CallbackRecord record);
}
