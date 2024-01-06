package cn.bootx.platform.daxpay.service.core.record.repair.convert;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 *
 * @author xxm
 * @since 2024/1/6
 */
@Mapper
public interface PayRepairRecordConvert {
    PayRepairRecordConvert CONVERT = Mappers.getMapper(PayRepairRecordConvert.class);
}
