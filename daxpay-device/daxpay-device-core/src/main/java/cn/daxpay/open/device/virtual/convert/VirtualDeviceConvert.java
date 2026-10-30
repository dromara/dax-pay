package cn.daxpay.open.device.virtual.convert;

import cn.daxpay.open.device.virtual.entity.VirtualDevice;
import cn.daxpay.open.device.virtual.result.VirtualDeviceResult;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

/// # 虚拟设备转换
@Mapper
public interface VirtualDeviceConvert {
    VirtualDeviceConvert CONVERT = Mappers.getMapper(VirtualDeviceConvert.class);

    /// vendor 为派生标识(开源版仅 virtual 一条产品线, 台账无独立列), 常量填充供前端区分设备形态
    @Mapping(target = "vendor", constant = "virtual")
    VirtualDeviceResult toResult(VirtualDevice in);
}
