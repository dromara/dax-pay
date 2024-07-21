package cn.daxpay.multi.service.convert.record;

import cn.daxpay.multi.service.entity.record.close.PayCloseRecord;
import cn.daxpay.multi.service.result.record.close.PayCloseRecordResult;
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
    PayCloseRecordResult convert(PayCloseRecord in);
}
