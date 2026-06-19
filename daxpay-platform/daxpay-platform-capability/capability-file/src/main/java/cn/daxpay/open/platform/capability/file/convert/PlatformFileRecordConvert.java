package cn.daxpay.open.platform.capability.file.convert;

import cn.daxpay.open.platform.capability.file.entity.PlatformFileRecord;
import cn.daxpay.open.platform.capability.file.result.PlatformFileRecordResult;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/// # 平台文件记录转换器
///
@Mapper
public interface PlatformFileRecordConvert {

    PlatformFileRecordConvert CONVERT = Mappers.getMapper(PlatformFileRecordConvert.class);

    PlatformFileRecordResult convert(PlatformFileRecord in);
}
