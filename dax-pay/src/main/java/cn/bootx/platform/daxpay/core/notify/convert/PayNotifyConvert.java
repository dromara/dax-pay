package cn.bootx.platform.daxpay.core.notify.convert;

import cn.bootx.platform.daxpay.core.notify.entity.PayNotifyRecord;
import cn.bootx.platform.daxpay.dto.notify.PayNotifyRecordDto;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * 转换
 *
 * @author xxm
 * @date 2021/6/22
 */
@Mapper
public interface PayNotifyConvert {

    PayNotifyConvert CONVERT = Mappers.getMapper(PayNotifyConvert.class);

    PayNotifyRecordDto convert(PayNotifyRecord in);

}
