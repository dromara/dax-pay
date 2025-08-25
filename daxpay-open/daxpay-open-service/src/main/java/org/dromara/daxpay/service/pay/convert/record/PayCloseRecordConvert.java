package org.dromara.daxpay.service.pay.convert.record;

import org.dromara.daxpay.service.pay.entity.record.close.PayCloseRecord;
import org.dromara.daxpay.service.pay.result.record.close.PayCloseRecordResult;
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
