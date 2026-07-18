package cn.daxpay.open.payment.admin.param.device;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.experimental.Accessors;

/// # 系统终端与通道终端绑定参数
@Data
@Accessors(chain = true)
@Schema(title = "系统终端与通道终端绑定参数")
public class TerminalChannelBindParam {

    @Schema(description = "系统终端编码")
    // 终端编码不可为空(复用 type.notBlank 语义不够准, 有 terminalNo.size 无 notBlank 时用通用 type 键兜底)
    @NotBlank(message = "{validation.field.type.notBlank}")
    private String systemTerminalNo;

    @Schema(description = "通道终端主键")
    @NotNull(message = "{validation.field.id.notNull}")
    private Long channelTerminalId;
}
