package org.dromara.daxpay.payment.old.pay.convert.record;

import org.dromara.daxpay.payment.old.pay.entity.record.close.PayCloseRecord;
import org.dromara.daxpay.payment.old.pay.result.record.close.PayCloseRecordResult;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;


@Mapper
public interface PayCloseRecordConvert {
    PayCloseRecordConvert CONVERT = Mappers.getMapper(PayCloseRecordConvert.class);

    /// 转换
    PayCloseRecordResult convert(PayCloseRecord in);
}
