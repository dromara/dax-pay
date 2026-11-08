package cn.daxpay.open.payment.device.terminal.result;

import cn.daxpay.open.payment.device.terminal.bo.TerminalExtraField;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

/// # 通道终端能力
///
/// 汇总通道对终端报备/注销/同步的支持情况, 供端侧按能力置灰操作按钮(避免点击后才拿到"不支持"的失败回执):
/// - [reportSupported] 等价于 [terminalTypes] 非空(通道注册了报备策略);
/// - [supportsCancel] / [supportsSync] 取自策略实现声明, 无策略时均为 false。
@Data
@Accessors(chain = true)
@Schema(title = "通道终端能力")
public class TerminalCapabilityResult {

    @Schema(description = "是否支持报备(通道支持至少一种报送类型)")
    private boolean reportSupported;

    @Schema(description = "通道支持的报送类型")
    private List<String> terminalTypes;

    @Schema(description = "是否支持注销")
    private boolean supportsCancel;

    @Schema(description = "是否支持状态同步")
    private boolean supportsSync;

    @Schema(description = "通道差异化报备参数字段定义")
    private List<TerminalExtraField> extraFields;
}
