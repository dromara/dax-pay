package cn.daxpay.open.payment.device.terminal.bo;

import lombok.Data;
import lombok.experimental.Accessors;

/// # 通道终端报备操作结果
///
/// 各通道报备策略 [cn.daxpay.open.payment.device.terminal.strategy.AbsChannelTerminalStrategy] 的统一回执,
/// 编排层按结果回写状态机与通道侧终端号。
@Data
@Accessors(chain = true)
public class TerminalOperateResult {

    /// 是否成功
    private boolean success;

    /// 操作后的登记状态码
    /// @see cn.daxpay.open.platform.core.enums.pay.channel.ChannelTerminalStatusEnum
    private String status;

    /// 通道侧终端号(报备成功时通道返回)
    private String deviceNo;

    /// 失败原因
    private String errorMsg;

    /// 成功快捷构造
    public static TerminalOperateResult success(String status, String deviceNo) {
        return new TerminalOperateResult()
                .setSuccess(true)
                .setStatus(status)
                .setDeviceNo(deviceNo);
    }

    /// 失败快捷构造
    public static TerminalOperateResult fail(String errorMsg) {
        return new TerminalOperateResult()
                .setSuccess(false)
                .setErrorMsg(errorMsg);
    }
}
