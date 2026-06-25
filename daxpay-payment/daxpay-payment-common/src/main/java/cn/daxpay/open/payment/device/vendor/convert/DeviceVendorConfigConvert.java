package cn.daxpay.open.payment.device.vendor.convert;

import cn.daxpay.open.payment.device.vendor.entity.DeviceVendorConfig;
import cn.daxpay.open.payment.device.vendor.result.DeviceVendorConfigResult;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/// # 设备厂商配置转换
///
@Mapper
public interface DeviceVendorConfigConvert {
    DeviceVendorConfigConvert CONVERT = Mappers.getMapper(DeviceVendorConfigConvert.class);

    DeviceVendorConfigResult toResult(DeviceVendorConfig in);
}