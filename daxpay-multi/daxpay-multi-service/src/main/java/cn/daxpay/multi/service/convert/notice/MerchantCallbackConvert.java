package cn.daxpay.multi.service.convert.notice;

import cn.daxpay.multi.service.entity.notice.callback.MerchantCallbackRecord;
import cn.daxpay.multi.service.entity.notice.callback.MerchantCallbackTask;
import cn.daxpay.multi.service.result.notice.callback.MerchantCallbackRecordResult;
import cn.daxpay.multi.service.result.notice.callback.MerchantCallbackTaskResult;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * 商户回调消息
 * @author xxm
 * @since 2024/8/5
 */
@Mapper
public interface MerchantCallbackConvert {

    MerchantCallbackConvert CONVERT = Mappers.getMapper(MerchantCallbackConvert.class);

    MerchantCallbackRecordResult toResult(MerchantCallbackRecord in);

    MerchantCallbackTaskResult toResult(MerchantCallbackTask in);
}
