package cn.daxpay.open.channel.mock.strategy.terminal;

import cn.daxpay.open.channel.mock.service.MockTerminalService;
import cn.daxpay.open.payment.device.terminal.bo.TerminalOperateResult;
import cn.daxpay.open.payment.device.terminal.entity.ChannelTerminal;
import cn.daxpay.open.payment.device.terminal.strategy.AbsChannelTerminalStrategy;

import java.util.List;

/// # Mock 终端报备策略公共基类
///
/// mock 通道双产品(mock_pay 直连 / mock_isv 服务商), 策略工厂按 product 建索引,
/// 因此两个产品必须各注册一个 [org.springframework.stereotype.Service] Bean:
/// [MockPayTerminalStrategy] 与 [MockIsvTerminalStrategy](本基类只承载共用行为)。
/// 报备/注销/同步的通道调用与回执转换统一由 [MockTerminalService] 完成。
///
/// 回执状态必须显式给出(编排层成功分支无条件回写台账状态):
/// 报备/同步成功 → submit + 通道侧终端号; 注销成功 → logged。
public abstract class MockTerminalStrategySupport extends AbsChannelTerminalStrategy {

    /// 终端业务服务(转发 mock 子应用)
    private final MockTerminalService mockTerminalService;

    /// 子类注入终端业务服务
    protected MockTerminalStrategySupport(MockTerminalService mockTerminalService) {
        this.mockTerminalService = mockTerminalService;
    }

    /// mock 支持标准/微信/支付宝/银联四类报送类型(对齐主应用 TerminalTypeEnum)
    @Override
    public List<String> getTerminalTypes() {
        return List.of("common", "wechat", "alipay", "union");
    }

    /// 报备: 转发 mock 子应用终端注册, 成功回写 submit 与通道侧终端号
    @Override
    public TerminalOperateResult submit(ChannelTerminal terminal, String systemTerminalNo) {
        return mockTerminalService.submit(terminal, systemTerminalNo);
    }

    /// 注销: 转发 mock 子应用终端注销, 成功回写 logged
    @Override
    public TerminalOperateResult cancel(ChannelTerminal terminal, String systemTerminalNo) {
        return mockTerminalService.cancel(terminal, systemTerminalNo);
    }

    /// 同步: 转发 mock 子应用终端状态回读, 成功回写 submit 与当前通道侧终端号
    @Override
    public TerminalOperateResult sync(ChannelTerminal terminal, String systemTerminalNo) {
        return mockTerminalService.sync(terminal, systemTerminalNo);
    }

    /// mock 子应用实现了终端注销, 支持注销
    @Override
    public boolean supportsCancel() {
        return true;
    }

    /// mock 子应用实现了终端状态回读, 支持同步
    @Override
    public boolean supportsSync() {
        return true;
    }
}
