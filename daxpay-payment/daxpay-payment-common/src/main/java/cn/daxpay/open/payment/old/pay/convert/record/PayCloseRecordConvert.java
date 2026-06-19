package cn.daxpay.open.payment.old.pay.convert.record;

import cn.daxpay.open.payment.old.pay.entity.record.close.PayCloseRecord;
import cn.daxpay.open.payment.old.pay.result.record.close.PayCloseRecordResult;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;


@Mapper
public interface PayCloseRecordConvert {
    PayCloseRecordConvert CONVERT = Mappers.getMapper(PayCloseRecordConvert.class);

    /// 转换
    PayCloseRecordResult convert(PayCloseRecord in);
}
