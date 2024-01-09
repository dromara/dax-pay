package cn.bootx.platform.daxpay.service.core.record.repair.convert;

import cn.bootx.platform.daxpay.service.core.record.repair.entity.PayRepairRecord;
import cn.bootx.platform.daxpay.service.dto.order.repair.PayRepairRecordDto;
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

    PayRepairRecordDto convert(PayRepairRecord in);
}
