package cn.daxpay.open.payment.device.qrcode.convert;

import cn.daxpay.open.payment.device.qrcode.entity.DeviceQrCode;
import cn.daxpay.open.payment.device.qrcode.result.DeviceQrCodeResult;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/// # 支付码牌转换
///
@Mapper
public interface DeviceQrCodeConvert {
    DeviceQrCodeConvert CONVERT = Mappers.getMapper(DeviceQrCodeConvert.class);

    DeviceQrCodeResult toResult(DeviceQrCode in);
}
