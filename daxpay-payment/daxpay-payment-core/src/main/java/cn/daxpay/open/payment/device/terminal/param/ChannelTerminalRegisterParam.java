package cn.daxpay.open.payment.device.terminal.param;

import cn.daxpay.open.platform.core.enums.pay.channel.ChannelTerminalStatusEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.experimental.Accessors;

/// # 通道终端手动登记参数(多端共用)
///
/// 手动订正通道侧终端号与登记状态(不调通道), 状态合法性由服务层
/// [ChannelTerminalStatusEnum#findByCode] 校验。不承载商户号, 归属校验由调用方完成。
@Data
@Accessors(chain = true)
@Schema(title = "通道终端手动登记参数")
public class ChannelTerminalRegisterParam {

    @Schema(description = "通道终端主键")
    @NotNull(message = "{validation.field.id.notNull}")
    private Long id;

    @Schema(description = "通道侧终端号(可空=只改状态)")
    @Size(max = 64, message = "{validation.field.terminalNo.size}")
    private String outTerminalNo;

    /// 登记状态
    /// @see ChannelTerminalStatusEnum
    @Schema(description = "登记状态(必填, 须为合法枚举值)")
    private String status;
}
