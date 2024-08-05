package cn.daxpay.multi.service.convert.notice;

import cn.daxpay.multi.service.entity.notice.notify.MerchantNotifyRecord;
import cn.daxpay.multi.service.entity.notice.notify.MerchantNotifyTask;
import cn.daxpay.multi.service.result.notice.notify.MerchantNotifyRecordResult;
import cn.daxpay.multi.service.result.notice.notify.MerchantNotifyTaskResult;
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
