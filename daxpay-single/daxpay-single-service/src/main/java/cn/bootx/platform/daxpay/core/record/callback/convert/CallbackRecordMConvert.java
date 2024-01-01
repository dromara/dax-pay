package cn.bootx.platform.daxpay.core.record.callback.convert;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * 回调通知转换
 * @author xxm
 * @since 2023/12/18
 */
@Mapper
public interface CallbackRecordMConvert {
    CallbackRecordMConvert CONVERT = Mappers.getMapper(CallbackRecordMConvert.class);
}
