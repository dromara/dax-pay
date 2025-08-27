package org.dromara.daxpay.service.pay.strategy;

import org.dromara.daxpay.service.pay.bo.assist.ChannelTerminalBo;
import org.dromara.daxpay.service.device.entity.terminal.ChannelTerminal;
import org.dromara.daxpay.service.device.entity.terminal.TerminalDevice;

/**
 * 支付终端设备通道管理策略
 * @author xxm
 * @since 2025/3/8
 */
public abstract class AbsChannelTerminalStrategy implements PaymentStrategy{

    /**
     * 报备
     */
    public abstract ChannelTerminalBo submit(TerminalDevice terminalDevice, ChannelTerminal terminalChannel);

    /**
     * 注销
     */
    public abstract ChannelTerminalBo cancel(TerminalDevice terminal, ChannelTerminal terminalChannel);

    /**
     * 同步
     */
    public ChannelTerminalBo sync(TerminalDevice terminal, ChannelTerminal terminalChannel){
        throw new UnsupportedOperationException("该通道不支持终端报备同步");
    }

}
