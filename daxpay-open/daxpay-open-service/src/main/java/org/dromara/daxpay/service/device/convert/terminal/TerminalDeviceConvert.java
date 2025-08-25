package org.dromara.daxpay.service.device.convert.terminal;

import org.dromara.daxpay.service.device.entity.terminal.TerminalDevice;
import org.dromara.daxpay.service.device.entity.terminal.ChannelTerminal;
import org.dromara.daxpay.service.device.param.terminal.ChannelTerminalParam;
import org.dromara.daxpay.service.device.param.terminal.TerminalDeviceParam;
import org.dromara.daxpay.service.device.result.terminal.ChannelTerminalResult;
import org.dromara.daxpay.service.device.result.terminal.TerminalDeviceResult;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * 支付终端设备转换
 * @author xxm
 * @since 2025/3/8
 */
@Mapper
public interface TerminalDeviceConvert {
    TerminalDeviceConvert CONVERT = Mappers.getMapper(TerminalDeviceConvert.class);

    TerminalDeviceResult toResult(TerminalDevice entity);

    TerminalDevice toEntity(TerminalDeviceParam param);

    ChannelTerminalResult toResult(ChannelTerminal entity);

    ChannelTerminal toEntity(ChannelTerminalParam param);

}
