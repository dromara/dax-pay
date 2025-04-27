package org.dromara.daxpay.service.strategy;

import org.dromara.daxpay.service.bo.assist.ChannelTerminalBo;
import org.dromara.daxpay.service.entity.assist.ChannelTerminal;
import org.dromara.daxpay.service.entity.assist.TerminalDevice;

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

}
