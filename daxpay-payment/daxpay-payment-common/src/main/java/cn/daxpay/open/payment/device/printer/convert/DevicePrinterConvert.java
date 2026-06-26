package cn.daxpay.open.payment.device.printer.convert;

import cn.daxpay.open.payment.device.printer.entity.DevicePrinter;
import cn.daxpay.open.payment.device.printer.result.DevicePrinterResult;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/// # 云打印设备转换
///
@Mapper
public interface DevicePrinterConvert {
    DevicePrinterConvert CONVERT = Mappers.getMapper(DevicePrinterConvert.class);

    DevicePrinterResult toResult(DevicePrinter in);
}
