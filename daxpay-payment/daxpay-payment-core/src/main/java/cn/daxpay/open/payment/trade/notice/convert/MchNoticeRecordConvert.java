package cn.daxpay.open.payment.trade.notice.convert;

import cn.daxpay.open.payment.trade.notice.entity.MchNoticeRecord;
import cn.daxpay.open.payment.trade.notice.result.MchNoticeRecordResult;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/// # 商户出站通知发送记录转换
///
@Mapper
public interface MchNoticeRecordConvert {

    MchNoticeRecordConvert CONVERT = Mappers.getMapper(MchNoticeRecordConvert.class);

    MchNoticeRecordResult toResult(MchNoticeRecord entity);
}
