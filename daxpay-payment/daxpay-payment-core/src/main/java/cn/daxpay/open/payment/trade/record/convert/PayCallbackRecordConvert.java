package cn.daxpay.open.payment.trade.record.convert;

import cn.daxpay.open.payment.trade.record.entity.PayCallbackRecord;
import cn.daxpay.open.payment.trade.record.result.PayCallbackRecordResult;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/// # 通道入站回调记录转换
///
@Mapper
public interface PayCallbackRecordConvert {

    PayCallbackRecordConvert CONVERT = Mappers.getMapper(PayCallbackRecordConvert.class);

    PayCallbackRecordResult toResult(PayCallbackRecord entity);
}
