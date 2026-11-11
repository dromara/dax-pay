package cn.daxpay.open.payment.device.terminal.convert;

import cn.daxpay.open.payment.device.terminal.entity.DeviceTerminal;
import cn.daxpay.open.payment.device.terminal.result.TerminalDeviceResult;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/// # 商户终端转换
@Mapper
public interface TerminalDeviceConvert {
    TerminalDeviceConvert CONVERT = Mappers.getMapper(TerminalDeviceConvert.class);

    TerminalDeviceResult toResult(DeviceTerminal in);
}
