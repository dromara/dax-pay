package cn.daxpay.open.payment.device.speaker.convert;

import cn.daxpay.open.payment.device.speaker.entity.DeviceSpeaker;
import cn.daxpay.open.payment.device.speaker.result.DeviceSpeakerResult;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/// # 云音箱设备转换
///
@Mapper
public interface DeviceSpeakerConvert {
    DeviceSpeakerConvert CONVERT = Mappers.getMapper(DeviceSpeakerConvert.class);

    DeviceSpeakerResult toResult(DeviceSpeaker in);
}
