package org.dromara.daxpay.service.pay.convert.notice;

import org.dromara.daxpay.service.pay.entity.notice.notify.MerchantNotifyRecord;
import org.dromara.daxpay.service.pay.entity.notice.notify.MerchantNotifyTask;
import org.dromara.daxpay.service.pay.result.notice.notify.MerchantNotifyRecordResult;
import org.dromara.daxpay.service.pay.result.notice.notify.MerchantNotifyTaskResult;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * 商户订阅通知
 * @author xxm
 * @since 2024/8/5
 */
@Mapper
public interface MerchantNotifyConvert {

    MerchantNotifyConvert CONVERT = Mappers.getMapper(MerchantNotifyConvert.class);

    MerchantNotifyRecordResult toResult(MerchantNotifyRecord in);

    MerchantNotifyTaskResult toResult(MerchantNotifyTask in);
}
