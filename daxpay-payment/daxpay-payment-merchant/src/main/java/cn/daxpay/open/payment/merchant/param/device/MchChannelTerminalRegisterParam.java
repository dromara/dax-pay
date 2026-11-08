package cn.daxpay.open.payment.merchant.param.device;

import cn.daxpay.open.platform.core.enums.pay.channel.ChannelTerminalStatusEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.experimental.Accessors;

/// # 通道终端手动调整参数(商户端)
///
/// 手工订正通道侧终端号与登记状态(不调通道), 状态合法性由服务层
/// [ChannelTerminalStatusEnum#findByCode] 校验。归属校验由 Service 按主键完成。
@Data
@Accessors(chain = true)
@Schema(title = "通道终端手动调整参数(商户端)")
public class MchChannelTerminalRegisterParam {

    @Schema(description = "通道终端主键")
    @NotNull(message = "{validation.field.id.notNull}")
    private Long id;

    @Schema(description = "通道侧终端号(可空=只改状态)")
    @Size(max = 64, message = "{validation.field.terminalNo.size}")
    private String outTerminalNo;

    /// 目标状态
    /// @see ChannelTerminalStatusEnum
    @Schema(description = "目标状态(必填, 须为合法枚举值)")
    private String status;
}
