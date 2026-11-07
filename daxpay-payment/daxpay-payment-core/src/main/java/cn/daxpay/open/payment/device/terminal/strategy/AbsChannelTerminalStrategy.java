package cn.daxpay.open.payment.device.terminal.strategy;

import cn.daxpay.open.payment.device.terminal.bo.TerminalOperateResult;
import cn.daxpay.open.payment.device.terminal.entity.ChannelTerminal;
import cn.daxpay.open.payment.strategy.PaymentStrategy;
import cn.daxpay.open.platform.core.code.CommonCode;
import cn.daxpay.open.platform.core.exception.operation.OperationFailException;

import java.util.List;

/// # 通道终端报备策略
///
/// 生命周期: submit 报备 → cancel 注销 → sync 同步。
/// 按支付产品注册(策略工厂按 product 索引); 无实现产品的通道不支持自动报备,
/// 台账走人工登记。回执统一为 [TerminalOperateResult], 状态机回写由编排层完成。
///
/// 通道 SDK 在子应用, 实现类为编排壳: 组装凭证与参数后经各通道
/// ChannelClient(HTTP+AES-GCM) 转发子应用执行。
public abstract class AbsChannelTerminalStrategy implements PaymentStrategy {

    /// 该通道可用的报送类型(默认标准上报)
    /// @see cn.daxpay.open.payment.device.enums.TerminalTypeEnum
    public List<String> getTerminalTypes() {
        return List.of("common");
    }

    /// 报备(调通道 API, 成功回执带通道侧终端号)
    ///
    /// @param systemTerminalNo 报备锚点的系统终端号(编排层取绑定关系中的首个系统终端)
    public abstract TerminalOperateResult submit(ChannelTerminal terminal, String systemTerminalNo);

    /// 注销(默认不支持)
    public TerminalOperateResult cancel(ChannelTerminal terminal, String systemTerminalNo) {
        throw new OperationFailException(CommonCode.FAIL_CODE, "error.device.terminal.cancelUnsupported");
    }

    /// 同步状态(默认不支持)
    public TerminalOperateResult sync(ChannelTerminal terminal, String systemTerminalNo) {
        throw new OperationFailException(CommonCode.FAIL_CODE, "error.device.terminal.syncUnsupported");
    }
}
