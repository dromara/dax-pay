package cn.daxpay.open.payment.old.pay.convert.notice;

import cn.daxpay.open.payment.old.pay.entity.notice.callback.MerchantCallbackRecord;
import cn.daxpay.open.payment.old.pay.entity.notice.callback.MerchantCallbackTask;
import cn.daxpay.open.payment.old.pay.result.notice.callback.MerchantCallbackRecordResult;
import cn.daxpay.open.payment.old.pay.result.notice.callback.MerchantCallbackTaskResult;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/// # 商户回调消息
///
@Mapper
public interface MerchantCallbackConvert {

    MerchantCallbackConvert CONVERT = Mappers.getMapper(MerchantCallbackConvert.class);

    MerchantCallbackRecordResult toResult(MerchantCallbackRecord in);

    MerchantCallbackTaskResult toResult(MerchantCallbackTask in);
}
