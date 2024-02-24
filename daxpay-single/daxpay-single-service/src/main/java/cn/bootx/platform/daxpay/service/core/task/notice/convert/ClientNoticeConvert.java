package cn.bootx.platform.daxpay.service.core.task.notice.convert;

import cn.bootx.platform.daxpay.service.core.task.notice.entity.ClientNoticeRecord;
import cn.bootx.platform.daxpay.service.core.task.notice.entity.ClientNoticeTask;
import cn.bootx.platform.daxpay.service.dto.record.notice.ClientNoticeRecordDto;
import cn.bootx.platform.daxpay.service.dto.record.notice.ClientNoticeTaskDto;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 *
 * @author xxm
 * @since 2024/2/23
 */
@Mapper
public interface ClientNoticeConvert {
    ClientNoticeConvert CONVERT = Mappers.getMapper(ClientNoticeConvert.class);

    ClientNoticeRecordDto convert(ClientNoticeRecord in);

    ClientNoticeTaskDto convert(ClientNoticeTask in);

}
