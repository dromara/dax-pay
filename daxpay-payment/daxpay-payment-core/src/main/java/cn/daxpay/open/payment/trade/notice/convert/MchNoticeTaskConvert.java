package cn.daxpay.open.payment.trade.notice.convert;

import cn.daxpay.open.payment.trade.notice.entity.MchNoticeTask;
import cn.daxpay.open.payment.trade.notice.result.MchNoticeTaskResult;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/// # 商户出站通知任务转换
///
@Mapper
public interface MchNoticeTaskConvert {

    MchNoticeTaskConvert CONVERT = Mappers.getMapper(MchNoticeTaskConvert.class);

    MchNoticeTaskResult toResult(MchNoticeTask entity);
}
