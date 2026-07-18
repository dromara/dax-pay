package cn.daxpay.open.payment.device.terminal.convert;

import cn.daxpay.open.payment.device.terminal.entity.ChannelTerminal;
import cn.daxpay.open.payment.device.terminal.result.ChannelTerminalResult;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/// # 通道终端转换
@Mapper
public interface ChannelTerminalConvert {
    ChannelTerminalConvert CONVERT = Mappers.getMapper(ChannelTerminalConvert.class);

    ChannelTerminalResult toResult(ChannelTerminal in);
}
