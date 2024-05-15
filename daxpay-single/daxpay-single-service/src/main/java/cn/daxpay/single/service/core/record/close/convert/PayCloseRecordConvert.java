package cn.daxpay.single.service.core.record.close.convert;

import cn.daxpay.single.service.core.record.close.entity.PayCloseRecord;
import cn.daxpay.single.service.dto.record.close.PayCloseRecordDto;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 *
 * @author xxm
 * @since 2024/1/4
 */
@Mapper
public interface PayCloseRecordConvert {
    PayCloseRecordConvert CONVERT = Mappers.getMapper(PayCloseRecordConvert.class);

    /**
     * 转换
     */
    PayCloseRecordDto convert(PayCloseRecord in);
}
