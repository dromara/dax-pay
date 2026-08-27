package cn.daxpay.open.platform.notify.convert.mail;

import cn.daxpay.open.platform.notify.entity.mail.NotifyMailRecord;
import cn.daxpay.open.platform.notify.result.mail.NotifyMailRecordResult;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/// 邮件发送记录转换
@Mapper
public interface NotifyMailRecordConvert {
    NotifyMailRecordConvert CONVERT = Mappers.getMapper(NotifyMailRecordConvert.class);

    NotifyMailRecordResult convert(NotifyMailRecord entity);
}
