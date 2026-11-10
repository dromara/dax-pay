package cn.daxpay.open.payment.device.terminal.strategy;

import cn.daxpay.open.payment.device.terminal.bo.TerminalExtraField;
import cn.daxpay.open.payment.device.terminal.bo.TerminalOperateResult;
import cn.daxpay.open.payment.device.terminal.entity.DeviceChannelTerminal;
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

    /// 通道差异化报备参数字段定义(默认无, 运营端按此渲染结构化表单并存入通道终端 extra)
    public List<TerminalExtraField> getExtraFields() {
        return List.of();
    }

    /// 报备(调通道 API, 成功回执带通道侧终端号)
    ///
    /// @param systemTerminalNo 报备锚点的系统终端号(编排层取绑定关系中的首个系统终端)
    public abstract TerminalOperateResult submit(DeviceChannelTerminal terminal, String systemTerminalNo);

    /// 注销(默认不支持)
    public TerminalOperateResult cancel(DeviceChannelTerminal terminal, String systemTerminalNo) {
        throw new OperationFailException(CommonCode.FAIL_CODE, "error.device.terminal.cancelUnsupported");
    }

    /// 同步状态(默认不支持)
    public TerminalOperateResult sync(DeviceChannelTerminal terminal, String systemTerminalNo) {
        throw new OperationFailException(CommonCode.FAIL_CODE, "error.device.terminal.syncUnsupported");
    }

    /// 是否支持注销(默认不支持, 由实现了通道注销 API 的策略覆盖)
    ///
    /// 供端侧按通道能力置灰注销按钮, 避免用户点击后才拿到"不支持"的失败回执。
    public boolean supportsCancel() {
        return false;
    }

    /// 是否支持状态同步(默认不支持, 由实现了通道状态回读的策略覆盖)
    ///
    /// 供端侧按通道能力置灰同步按钮, 避免用户点击后才拿到"不支持"的失败回执。
    public boolean supportsSync() {
        return false;
    }
}
